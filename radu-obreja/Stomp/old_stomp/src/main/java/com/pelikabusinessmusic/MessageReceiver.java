package com.pelikabusinessmusic;

import java.util.Set;
import java.util.logging.Logger;

import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession.QueueQuery;
import org.hornetq.api.core.client.MessageHandler;

/**
 * Handles received messages 
 * 
 * @author eeroki
 *
 */

public class MessageReceiver implements MessageHandler {
	
	private static final String CREATE_USER = "CREATE_USER";
	private static final String CREATE_QUEUE = "CREATE_QUEUE";
	private static final String DELETE_QUEUE = "DELETE_QUEUE";
	private static final String LOAD_QUEUES = "LOAD_QUEUES";
	private static final String DELETE_QUEUES = "DELETE_QUEUES";
	private static final String LIST_CONNECTED = "LIST_CONNECTED";
	private static final String HELP = "HELP";
	private static final String SEND = "SEND";
	private static final String SHUTDOWN = "SHUTDOWN";
	
	
	private final String HELPTEXT = "Available commands are: \n" +
						   CREATE_USER + " <username> <queuename> - creates a user with access rights to <queuename>, missing queue will be created\n" +
						   CREATE_QUEUE + " <queuename> - creates a queue with given name\n" +
						   DELETE_QUEUE + " <queuename> - tries to delete <queuename>\n" +
						   LOAD_QUEUES + " - loads queue and its users' information from database\n" +
						   DELETE_QUEUES + " - tries to delete all queues on server\n" +
						   LIST_CONNECTED + " - lists queues which have listeners\n" +
						   HELP + " - prints this help text\n" +
						   SEND + " <queuename> <message> - sends <message> to <queuename>\n" +
						   SHUTDOWN   + " - shuts down the server";
	
	private ServerManager serverManager = null;
	
	protected static Logger log = Logger.getLogger(MessageReceiver.class.getName());
	
	public MessageReceiver(ServerManager serverManager) {
		this.serverManager = serverManager; 
	}

	public void onMessage(ClientMessage message) {
		
		SimpleString text = message.getBodyBuffer().readNullableSimpleString();
		String trimmed = text.toString().trim();
		
		String[] textParts = trimmed.split(" ");
		
		// Command without parameter
		String command = null;
		
		if(textParts.length > 0) {
			command = textParts[0];
			command = command.toUpperCase();
		}
		
		try {
			message.acknowledge();	
			
			if(textParts.length == 1) {
				if(command.equals(LOAD_QUEUES)) {
					serverManager.initQueues();
				}
				// Tries to remove all users
				if(command.equals(DELETE_QUEUES)) {
					serverManager.removeQueues();
				}
				// Shuts down server
				else if (command.equals(SHUTDOWN)) {
					serverManager.shutdown();
				}
				// Prints help to console
				else if (command.equals(HELP)) {
					serverManager.sendLogMessage(HELPTEXT);
				}
				else if (command.equals(LIST_CONNECTED)) {
					Set<String> users = serverManager.getQueues().keySet();
					
					String headline = "Connected users and addresses";
					String userList = headline;
					
					for(String user : users) {
						
						QueueQuery qq = serverManager.getQueueInfo(user);
						
						if(qq.getConsumerCount() > 0) {
							userList += "\n";
							userList += qq.getAddress() + " with " + qq.getConsumerCount() + " listeners";
						}
					}
					
					serverManager.sendLogMessage(userList);
				}
			}
			// Commands with 1 parameter
			else if(textParts.length == 2) {
				
				String parameter = textParts[1];
				
				// Create queue
				if(command.equals(CREATE_QUEUE)){
					serverManager.createQueue(parameter);
				}
				// Remove queue
				else if(command.equals(DELETE_QUEUE)) {
					serverManager.removeQueue(parameter);
				}			
			}
			// Commands with 2 parameters
			else if(textParts.length == 3) {
				String parameter1 = textParts[1];
				String parameter2 = textParts[2];
				
				// Sends a message to given user
				if(command.equals(SEND)) {
					serverManager.sendMessage(parameter2, parameter1);				
				}
				// Creates user without password (validated against db)
				// with parameters username, role
				else if(command.equals(CREATE_USER)) {
					serverManager.addListenRights(parameter2, parameter2);
					serverManager.createQueue(parameter2);
					serverManager.createUser(parameter1, null, parameter2);
				}
			}
			else if(textParts.length == 4) {
				String parameter1 = textParts[1];
				String parameter2 = textParts[2];
				String parameter3 = textParts[3];
				
				// Sends a message to given user
				if(command.equals(SEND)) {
					serverManager.sendMessage(parameter2 + " " + parameter3, parameter1);				
				}
				// Creates user with password (validated against server)
				// with parameters username, password, role
				if(command.equals(CREATE_USER)) {
					serverManager.addListenRights(parameter3, parameter3);
					serverManager.createQueue(parameter3);
					serverManager.createUser(parameter1, parameter2, parameter3);
				}
			}
			else if(textParts.length == 5) {
				String parameter1 = textParts[1];
				String parameter2 = textParts[2];
				String parameter3 = textParts[3];
				String parameter4 = textParts[4];
				
				// Sends a message to given user
				if(command.equals(SEND)) {
					serverManager.sendMessage(parameter2 + " " + parameter3 + " " + parameter4, parameter1);				
				}
			}
			else if(textParts.length == 6) {
				String parameter1 = textParts[1];
				String parameter2 = textParts[2];
				String parameter3 = textParts[3];
				String parameter4 = textParts[4];
				String parameter5 = textParts[5];
				
				// Sends a message to given user
				if(command.equals(SEND)) {
					serverManager.sendMessage(parameter2 + " " + parameter3 + 
							" " + parameter4 + " " + parameter5, parameter1 );				
				}
			}
		} 
		catch (Exception e) {
			log.severe("Failed to execute given command " + command);
			log.severe(e.getMessage());
		}
		
	}
}
