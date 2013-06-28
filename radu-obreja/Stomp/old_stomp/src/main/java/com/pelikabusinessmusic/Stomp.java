package com.pelikabusinessmusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.JournalType;

/**
 * Class configures messaging server
 * and starts it up.
 * 
 * @author eeroki
 *
 */

public class Stomp {
	
	private static final String CONF_PATH = "/conf/";
	private static final String CONF_FILE = "stomp.properties";
	
	protected static Logger log = Logger.getLogger(Stomp.class.getName());
	
	private static DbConnect dbConnect = null;
	
	public static void main(String[] args) {		
		try {
			ServerManager serverManager = initializeServer();
			serverManager.initialize();
			serverManager.start();
		} catch (Exception e) {
			log.severe("Failed to start the server");
			log.severe(e.getMessage());
			System.exit(0);
		}	
	}
	
	public static ServerManager initializeServer() throws Exception {
		FileConfiguration configuration = null;
		SecurityManager security = null;
		
		// configuration start
		configuration = new FileConfiguration();
		configuration.setSecurityEnabled(true);
		configuration.setPersistenceEnabled(true);
		configuration.setJournalType(JournalType.NIO);
		configuration.setClusterUser("clusterAdmin");
		configuration.setClusterPassword("H0rnN4tGoo");
		configuration.setConfigurationUrl("hornetq-configuration.xml");
		
		// load user set properties from file
		Properties properties = loadProperties();
		
		// set acceptor based on config file
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("protocol", "stomp");
		parameters.put("host", properties.getProperty("server.address"));
		parameters.put("port", properties.getProperty("server.port"));
		parameters.put("use-nio", "true");
		TransportConfiguration tpConf = new TransportConfiguration(NettyAcceptorFactory.class.getName(), parameters, "stomp-acceptor");
		configuration.getAcceptorConfigurations().add(tpConf);
		
		configuration.start();
		
		// Initialize dbconnection
		dbConnect = new DbConnect(properties.getProperty("database.address"),
								  properties.getProperty("database.name"),
								  properties.getProperty("database.username"),
								  properties.getProperty("database.password"));
		
		// Create securitymanager
		security = new SecurityManager(dbConnect); 
		
		// we create the HornetQ server using this config
		HornetQServer hornetqServer = HornetQServers.newHornetQServer(
				configuration, null, security);

		hornetqServer.start();
		
		ServerManager serverManager = new ServerManagerImpl(hornetqServer, dbConnect, properties);
		return serverManager;
	}
	
	// Loads configuration file and
	// sets configuration to properties
	private static Properties loadProperties() throws IOException {
		// get location where program is run
		URL url = Stomp.class.getProtectionDomain()
        .getCodeSource().getLocation();
		
		String path = url.getPath();
		
		if(path.contains("jar")) {
			// remove filename to get directory
			path = path.substring(0, path.lastIndexOf("/"));
		}
		// when running from eclipse
		else {
			path = ".";
		}
		
		path+=CONF_PATH;
		
		//Load logger settings from conf path
		InputStream logPropFile = Stomp.class.getResourceAsStream("/logger.properties");
		
		System.out.println("logprop " + logPropFile.toString());
		
		LogManager.getLogManager().readConfiguration(logPropFile);
		
		path+=CONF_FILE;
		
		Properties properties = new Properties();
		
		try {
			File propertiesFile = new File(path);
	    	path = propertiesFile.getAbsolutePath();
	    	properties.load(new FileInputStream(propertiesFile));
		} catch (IOException ioe) {
			log.severe("Failed to read configuration file " + path);
			throw ioe;
		}
		return properties;
	}
}
