package com.pelikabusinessmusic;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSession.QueueQuery;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.spi.core.security.HornetQSecurityManager;

/**
 * Class for managing server actions
 * 
 * @author eeroki
 *
 */


public class ServerManagerImpl implements ServerManager {
	
	private String adminName = null;
	private String adminPassword = null;
	private String cacheUrl = null;
	
	private HornetQServer server = null;
	private HornetQServerControl serverControl = null;
	private Set<String> users = new HashSet<String>();
	private Map<String, Customer> queues = new TreeMap<String, Customer>();
	private DbConnect dbConnect = null;
	

	// Client variables for messaging
	private ClientSessionFactory csf = null;
	private ClientSession coreSession = null;
	private ClientProducer producer = null;

	private boolean logToServer_out = false;
	protected static Logger log = Logger.getLogger(ServerManagerImpl.class.getName());
	private DbWatcher dbw = null;
	
	// Constructor
	
	public ServerManagerImpl(HornetQServer hornetQServer, DbConnect dbConnect, Properties properties) {
		server = hornetQServer;
		
		this.dbConnect = dbConnect;
		
		this.cacheUrl = properties.getProperty("server.myweb.cacheUrl");
		this.adminName = properties.getProperty("server.admin.name");
		this.adminPassword = properties.getProperty("server.admin.password");
		
		long pollIntervall = Long.parseLong(properties.getProperty("server.pollintervall"));
		
		dbw = new DbWatcher(this, dbConnect, pollIntervall);
	}
	
	protected void finalize() throws Throwable
	{
		
	} 
	
	// Initializes the server for use
	public void initialize() {
		try {
			// Get servercontrol
			serverControl = server.getHornetQServerControl();

			// Initializing hornetq core messaging
			csf = HornetQClient.createClientSessionFactory(new TransportConfiguration(
							NettyConnectorFactory.class.getName())); 
			
			// Admin user created separately
			HornetQSecurityManager securityManager = server.getSecurityManager();
			securityManager.addRole(adminName, adminName);
			securityManager.addUser(adminName, adminPassword);
			
			csf.setBlockOnAcknowledge(false);
			csf.setBlockOnDurableSend(false);
			coreSession = csf.createSession(adminName, adminPassword, false, true, true, true, 0);
			coreSession.start();

			// Using one producer for sending
			producer = coreSession.createProducer();
			
			// Create and bind adminqueues in and out
			addListenRights(SERVER_IN, adminName);
			createQueue(SERVER_IN);
			setServerToConsume(getAddress(SERVER_IN));
			
			addListenRights(SERVER_OUT, adminName);
			createQueue(SERVER_OUT);
			
					
			// Create customers and their queues
			initQueues();
			
			log.info("Startup complete");
			
		} catch (Exception e) {
			log.severe("Server initialization failed");
			e.printStackTrace();
			shutdown();
		}
	}
	
	// Start watching database
	public void start() {
		dbw.run();
	}
	
	// Shuts the server down
	public void shutdown() {
		log.info("Server shutting down");
		
		// Close all resources
		try {
			if(coreSession != null) {
				coreSession.close();
			}
			
			if(csf != null) {
				csf.close();
			}
			
			if(server.isStarted()) {
				server.stop();
			}
	        
			dbw.stop();
			
	    } catch (Exception e) {
	    	log.warning("Could not close all resources at shutdown");
	    }
		
		System.exit(0);
	}
	
	// Sends a message to specified address
	public void sendMessage(String message, String addressName) throws HornetQException {
		
		try {
			if(message != null && addressName != null && 
				(queues.containsKey(addressName) || addressName.equals(SERVER_OUT))) {
				
				QueueQuery qq = getQueueInfo( addressName );
				
				int cc = qq.getConsumerCount();
				
				for ( int i=0; i<cc; i++ ) {
				
					ClientMessage notification = coreSession.createMessage(true);
		
					notification.getBodyBuffer().writeNullableSimpleString(SimpleString.toSimpleString(message));			
					
					String address = getAddress(addressName);
					
					producer.send(address, notification);
					log.info("Message sent to address " + address + ", " + message);
				
				}
			}
		} catch (HornetQException e) {
			log.severe("Could not send the message");
			throw e;
		}		
	}
	
	// Sends message to server_out
	public void sendLogMessage(String message) {
		if(logToServer_out) {
			log.info(message);
			try {
				sendMessage(message, SERVER_OUT);
			} catch (HornetQException e) {
				log.severe("Failed to send log message");
			}
		}
	}
	
	// Sends cache invalidation request through REST 
	public void sendCacheInvalidate(String username) {
		
		int userId = queues.get(username).getId();
		
		try{
			
			URL url = new URL(cacheUrl + userId);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	
			conn.setRequestMethod("GET");
			
			// Just check that there was no error connecting the server 
			conn.getResponseMessage();
			
		} catch(IOException ioe) {
			log.severe("Failed to call invalidation");
		}
	}
	
	// Creates a user with roles
	public void createUser(String user, String password, String role) {
		HornetQSecurityManager securityManager = server.getSecurityManager();
		
		if((user != null) && (role != null)) {
			if(!users.contains(user)) {
				
				securityManager.addRole(user, role);
				
				//No need to create users which are validated against db
				if(password != null) {
					securityManager.addUser(user, password);	
				}
				
				users.add(user);
				
				sendLogMessage("User " + user + " created with role " + role);
			} else {
				log.info("User " + user + " already has subscribing rights for queue " + role);
			}
		}	
	}
	
	public void createUsers(String customerName, List<String>users) throws Exception {
		for(String user: users) {
			addListenRights(customerName, customerName);
			createUser(user, null, customerName);
		}
	}
	
	// Creates a new queue with given name if one does not exist already.
	public void createQueue(String addressName) throws Exception {
		try {
			String queueAddress = getAddress(addressName);
			
			// Check if queue already exists
			if(!coreSession.queueQuery(new SimpleString(queueAddress)).isExists()) {
				coreSession.createQueue(queueAddress, queueAddress, true);
				sendLogMessage("Queue created to address " + queueAddress);
			}
			else {
				sendLogMessage("Queue " + queueAddress + " already exists");
			}	
			
			// Check if queue is known by server and add if needed
			if(!queues.containsKey(addressName)) {
				Customer cust = dbConnect.getCustomer(addressName);
			
				// No customer in database
				if(cust == null) {
					cust = new Customer(0, addressName);
				}
			
				queues.put(addressName, cust);
			}
			
		} catch (Exception e) {
			log.severe("Failed to create a queue.");
			throw e;
		}
	}
	
	// Adds a addresses listening rights for specified role
	public void addListenRights(String addressName, String neededRole) throws Exception {
		
		String address = getAddress(addressName);
			
		// Give subscribing rights to listener
		serverControl.addSecuritySettings(
					address, adminName, neededRole, adminName, adminName,
					adminName, adminName, adminName);
	}
	
	// Initializes users and their queues based on db
	public void initQueues() {		

		sendLogMessage("Initializing queues");
		logToServer_out = false;
		
		queues = dbConnect.getCustomers();
		
		int customerCount = queues.size();
		Customer cust = null;
		
		for(Map.Entry<String, Customer>user : queues.entrySet()) {
			
			String username = null;
			
			try {
				cust = user.getValue();
		
				username = cust.getName();
				
				// Create user from each contact
				for(String contactName : cust.getContactNameList()) {
					// Users role is same as username
					addListenRights(username, username);
					createUser(contactName, null, username);
				}

				// Queue for each customer
				createQueue(username);
				
				
			} catch (Exception e) {
				log.severe("Failed to create queue " + username);
				removeUserSecurity(cust.getName());
			}
		}
		logToServer_out = true;
		sendLogMessage(customerCount + " queues created");
	}
	
	// Tries to remove all queues and users
	public void removeQueues() {
		
		sendLogMessage("Removing all queues and users");
		int queueCount = queues.size();
		
		logToServer_out = false;
		
		// Remove their queues
		Iterator<Map.Entry<String, Customer>> it = queues.entrySet().iterator();
		Customer cust = null;
		String addressName = null;
		
		while(it.hasNext()) {
			try {
				cust = it.next().getValue();
				addressName = cust.getName();
				
				String address = getAddress(addressName);
				serverControl.destroyQueue(address);
				serverControl.removeSecuritySettings(address);
				
				List<String> userList = cust.getContactNameList();
				
				for(String user : userList) {
					removeUser(user);	
				}
				cust.getContacts().clear();
				it.remove();
				
			}
			catch(Exception e) {
				log.severe("Failed to remove queue " + addressName);
			}
		}
		
		queues.clear();
		
		logToServer_out = true;
		sendLogMessage("Removed " + queueCount + " queues");
	}
	
	// Constructs address from addressName
	public String getAddress(String addressName) {
		return "queue.pbm." + addressName; 
	}
	
	
	public Set<String> getUsers() {
		return users;
	}
	
	public Map<String, Customer> getQueues() {
		return queues;
	}
	
	public int getQueueId(String queueName) {
		return queues.get(queueName).getId();
	}
	
	// Returns information about specific queueu
	public QueueQuery getQueueInfo(String user) {
		String address = getAddress(user);
		QueueQuery queueInfo = null;
		
		try {
			queueInfo = coreSession.queueQuery(SimpleString.toSimpleString(address));
		} catch (HornetQException e) {
			sendLogMessage("No information found for address " + address);
		}
		
		return queueInfo; 
	}
	
	// Removes specific queue from server and users of it
	public void removeQueue(String addressName) throws Exception {	
		String address = getAddress(addressName);
		serverControl.destroyQueue(address);
		serverControl.removeSecuritySettings(address);
		
		List<String> userList = queues.get(addressName).getContactNameList();
		
		for(String user : userList) {
			removeUser(user);	
		}
		queues.get(addressName).getContacts().clear();
		
		queues.remove(addressName);
		
		sendLogMessage("Queue " + addressName + " removed");
	}
	
	// Removes user from server
	public void removeUser(String user) {	
		if(!user.equals(adminName)) {
			
			removeUserSecurity(user);
			users.remove(user);
			sendLogMessage("User " + user + " removed from server");
		}
	}
	
	private void setServerToConsume(String address) throws Exception {
		// Queue exists
		if(coreSession.queueQuery(SimpleString.toSimpleString(address)).isExists()) {
			// Create consumer for SERVER_IN so that server can receive messages
			if(address.equals(getAddress(SERVER_IN))) {
				// Creating a listener for created queue
				ClientConsumer consumer = coreSession.createConsumer(address);
				consumer.setMessageHandler(new MessageReceiver(this));
			}
		}
		else {
			throw new Exception("Address " + address + " not found");
		}
	}
	
	// Removes user security info from server
	private void removeUserSecurity(String user) {
		HornetQSecurityManager securityManager = server.getSecurityManager();
		securityManager.removeUser(user);
	}
}
