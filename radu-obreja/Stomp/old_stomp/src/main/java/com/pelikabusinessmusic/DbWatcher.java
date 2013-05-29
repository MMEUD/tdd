package com.pelikabusinessmusic;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * DbWatcher monitors user updates 
 * in the database and notifies
 * message server of those.
 * 
 * Runs as a separate thread.
 * 
 * @author eeroki
 *
 */

public class DbWatcher implements Runnable {
	private long pollIntervalMs = 15;
	private ServerManager serverManager = null;
	private DbConnect dbConnect = null;
	private boolean run = false;
	protected static Logger log = Logger.getLogger(DbWatcher.class.getName());
	public DbWatcher(ServerManager serverManager, DbConnect dbConnection, long pollIntervallMs) {
		this.serverManager = serverManager;
		this.dbConnect = dbConnection;
		// Seconds to milliseconds
		this.pollIntervalMs = pollIntervallMs * 1000;
	}
	// Polls database for updates
	public void run() {
		run = true;
		try {
			while(run) {
				// map contains user to notify (key) and specified message (value)
				Map<String, List<String>>notifications = dbConnect.getNotifications();
				if(notifications != null && !notifications.isEmpty()) {
					// Execute all notification actions
					for(Map.Entry<String, List<String>>notification : notifications.entrySet()) {

						for(String message : notification.getValue()) {
						doNotificationAction(notification.getKey(), message);
						}
					}
				}
				// NOTIFY  DOWNLOADER FOR JUKEBOX PLAY  SONG 
				Map<String, List<String>>notificationsJukebox = dbConnect.getNotificationsJukebox();
				if(notificationsJukebox!= null && !notificationsJukebox.isEmpty()) {
					// Execute all notification actions
					for(Map.Entry<String, List<String>>notificationJukebox : notificationsJukebox.entrySet()) {
						for(String message :notificationJukebox.getValue()) {
						doNotificationActionJukebox(notificationJukebox.getKey(), message);
						}
					}
				}
				
				Thread.sleep(pollIntervalMs);
			}

		} catch (InterruptedException e) {
			log.severe("Sending notification failed");
		}
	}

	public void stop() {
		run = false;
	}

	// Function to handle notification action to correct user
	private void doNotificationAction(String custLoginName, String action) {

		try{
			Customer customer = dbConnect.getCustomer(custLoginName);
			String customerName = customer.getName();

			// User created
			if(action.equals("Create")) {
				serverManager.createUsers(customerName, customer.getContactNameList());
				serverManager.createQueue(customerName);
			}
			// Delete user
			else if(action.equals("Delete")) {
				serverManager.removeUser(customerName);
			}
			else if(action.equals("Update")) {
				serverManager.createUsers(customerName, customer.getContactNameList());
			}
			// Normal notification
			else {
				serverManager.sendMessage(action, custLoginName);
			}

		} catch(Exception e) {
			log.severe("Failed to execute action caused by database event");
		}
	}


	
	//Function to  handle notification  action  JUKEBOX to correct user 
	
	private void doNotificationActionJukebox(String custLoginName, String action) {

		try{
			Customer customer = dbConnect.getCustomer(custLoginName);
			String customerName = customer.getName();

			// User created
			if(action.equals("Create")) {
				serverManager.createUsers(customerName, customer.getContactNameList());
				serverManager.createQueue(customerName);
			}
			// Delete user
			else if(action.equals("Delete")) {
				serverManager.removeUser(customerName);
			}
			else if(action.equals("Update")) {
				serverManager.createUsers(customerName, customer.getContactNameList());
			}
			// Normal notification
			else {
				serverManager.sendMessage(action, custLoginName);
			}

		} catch(Exception e) {
			log.severe("Failed to execute action caused by database event");
		}
	}


}
