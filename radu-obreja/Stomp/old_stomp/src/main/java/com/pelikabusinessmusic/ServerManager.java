package com.pelikabusinessmusic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientSession.QueueQuery;

public interface ServerManager {
	
	static final String SERVER_IN = "server_in";
	static final String SERVER_OUT = "server_out";
	
	// Initializes the server for use
	public abstract void initialize();
	
	// Start watching database
	public abstract void start();

	// Shuts the server down
	public abstract void shutdown();

	// Sends a message to specified address
	public abstract void sendMessage(String message, String user) throws HornetQException;
	
	// Sends message to server_out
	public void sendLogMessage(String message);

	// Sends cache invalidation request through REST 
	public abstract void sendCacheInvalidate(String user);
	
	// Creates a user with roles
	public abstract void createUser(String user, String password, String role);

	// Creates users listening rights for customers queue
	public void createUsers(String customerName, List<String>users) throws Exception;
	
	// Creates a new queue with given name if one does not exist already.
	// listen tells if server needs to listen the queue
	public abstract void createQueue(String addressName) throws Exception;

	// Adds a listener to given address
	public void addListenRights(String addressName, String neededRole) throws Exception;

	// Initializes users and their queues based on db
	public abstract void initQueues();
	
	// Tries to remove all users from server
	public abstract void removeQueues();
	
	// Returns address of given user
	public String getAddress(String addressName);
	
	// Returns set of users that has been initialized
	public Set<String> getUsers();
	
	// Returns queueId of queueName
	public int getQueueId(String queueName);
	
	// Returns Map of queues and and their listeners
	public Map<String, Customer> getQueues();
	
	// Returns information about specific queueu
	// Null if queue for given user does not exist
	public QueueQuery getQueueInfo(String user);
	
	// Removes queue from given address
	public void removeQueue(String addressName) throws Exception;
	
	// Removes user from server
	public void removeUser(String user) throws Exception;
}