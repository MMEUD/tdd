package com.pelikabusinessmusic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * DbConnect provides connection to database
 * and functions to fetch data from db.
 * 
 * @author eeroki
 *
 */

public class DbConnect {
	
	private static Connection connection = null;
	
	private String dbAddress = null;
	private String dbName = null;
	private String userName = null;
	private String password = null;
	
	protected static Logger log = Logger.getLogger(DbConnect.class.getName());
	
	public DbConnect() {
	}
	
	public DbConnect(String dbAddress, String dbName, String userName, String password) throws SQLException {
		if(dbAddress != null && dbName != null &&
			userName != null && password != null) {
			this.dbAddress = dbAddress;
			this.dbName = dbName;
			this.userName = userName;
			this.password = password;
		}
		else {
			throw new SQLException("Connection parameters must contain non-null values");
		}
	}
	
	// shut down dbconnection
	protected void finalize() throws Throwable
	{
		disconnect();
	} 
	
	// Establish database connection
	public void connect() {
		try {
			if(connection == null || connection.isClosed()) {
				
				String url = "jdbc:mysql://" + dbAddress + "/" + dbName;
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(url, userName, password);
			}
		} catch (Exception e) {
			log.severe("Cannot connect to database server");
			e.printStackTrace();
		}
	}
	
	// Closes database connection
	public void disconnect() {
		if(connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				log.severe("Error closing database connection");
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		try {
			if(connection != null && !connection.isClosed()) {
				return true;
			} 
			else {
				return false;
			}
		} catch(SQLException e) {
			log.severe("Connection error");
			return false;
		}
	}
	
	// Finds user with given name from database
	public Customer getCustomer(String loginName) throws SQLException {

		connect();
		
		Statement statement = connection.createStatement();
			
		String query = "SELECT CustLoginName, ContLoginName, Passwd FROM Customers, Contacts, CustCont " +  
					   "WHERE Customers.CustLoginName='" + loginName + "' " +
					   "AND CustCont.Removed IS NULL " +
					   "AND Customers.CustID=CustCont.CustID " +
					   "AND Contacts.ContID=CustCont.ContID ";
		
		ResultSet rs = statement.executeQuery(query);

		Customer customer = new Customer();
		
		// Read customer and contacts from resultset
		while(rs.next()) {
			if(customer.getName() == null) {
				customer.setName(rs.getString("CustLoginName"));
			}
	
			customer.addContact(rs.getString("ContLoginName"), rs.getString("Passwd"));
		}
			
		rs.close();
		statement.close();
			
		disconnect();
		
		return customer;		
	}
	
	public boolean isValidUser(String contactName, String password) throws SQLException {
		
		connect();
		
		Statement statement = connection.createStatement();
			
		String query = "SELECT COUNT(*) FROM Contacts ,CustCont WHERE ContLoginName='" + contactName +
			"' AND Contacts.ContID=CustCont.ContID AND Passwd='" + password + "' AND Removed IS NULL";
	
		ResultSet resultSet = statement.executeQuery(query);
		
		int count = 0;
		if(resultSet.next()) {
			count = resultSet.getInt(1);
		}
		
		// close db connection
		resultSet.close();
		statement.close();
		disconnect();
		
		if(count > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// Lists users from the database
	public Map<String, Customer> getCustomers() {
		
		connect();
		
		Map<String, Customer> customerMap = null;
		
		try {
			Statement statement = connection.createStatement();
			
			String query = "SELECT Customers.CustID, CustLoginName, ContLoginName, Passwd FROM Customers, Contacts, CustCont " +  
			   "WHERE CustCont.Removed IS NULL " +
			   "AND Customers.CustLoginName<>'' " +
			   "AND Customers.CustID=CustCont.CustID " +
			   "AND Contacts.ContID=CustCont.ContID ";
			ResultSet resultSet = statement.executeQuery(query);
			int count = 0;
			
			customerMap = new TreeMap<String, Customer>();
			
			int custId = -1;
			String custLoginName = null;
			String contLoginName = null;
			String passwd = null;
			Customer added = null;
			
			while(resultSet.next()) {
				custId = resultSet.getInt("CustID");
				custLoginName = resultSet.getString("CustLoginName");
				contLoginName = resultSet.getString("contLoginName");
				passwd = resultSet.getString("passwd");
				
				// Add contacts to existing customer
				if(customerMap.containsKey(custLoginName)) {
					customerMap.get(custLoginName).addContact(contLoginName, passwd);
				}
				// Add new customer and contact info
				else {
					added = new Customer(custId, custLoginName);
					added.addContact(contLoginName, passwd);
					customerMap.put(added.getName(), added);
				}
				count++;
			}
			
			resultSet.close();
			statement.close();
			
		} catch (SQLException e) {
			log.severe("Could not retrieve usernames");
		}
		
		disconnect();
		
		return customerMap;
	}
	
	
	//GET ALL JUKEBOX MESSAGE COMMAND  FROM  DATABASE 
	
	public List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {		
		ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

	    while (rs.next()) {
	        HashMap<String,Object> row = new HashMap<String, Object>(columns);
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        list.add(row);
	    }

	    return list;
	}
	
	// Gets all update notifications jukebox from db
		public Map<String, List<String>> getNotificationsJukebox() {
			Map<String, List<String>> updateNotifications = new HashMap<String, List<String>>();
			connect();
			try {
				Statement statement = connection.createStatement();
				// Notified = false
				String query = "SELECT Customers.CustID, Type, CustLoginName, Message FROM Customers, NotificationJukebox " +
						"WHERE Notified=" + false + " AND NotificationJukebox.CustID=Customers.CustID";
				ResultSet resultSet = statement.executeQuery(query);
				int count = 0;	
				// Read target queue, message and CustID of notified customer from database
				while(resultSet.next()) {
					String custLoginName = resultSet.getString("CustLoginName");
					String message = resultSet.getString("Message");		
					// User already has some messages
					if(updateNotifications.containsKey(custLoginName)) {	
						updateNotifications.get(custLoginName).add(message);
					} else {
						List<String>messages = new ArrayList<String>();
						messages.add(message);
						updateNotifications.put(custLoginName, messages);
					}
					count++;
				}
				statement.close();
			} catch (SQLException e) {
				log.severe("Could not retrieve notifications");
				e.printStackTrace();
			}
			disconnect();
			return updateNotifications;
		}

	
	
	// Gets all update notifications from db
	public Map<String, List<String>> getNotifications() {
		Map<String, List<String>> updateNotifications = new HashMap<String, List<String>>();
		connect();
		try {
			Statement statement = connection.createStatement();
			// Notified = false
			String query = "SELECT Customers.CustID, Type, CustLoginName, Message FROM Customers, Notification " +
					        "WHERE Notified=" + false + " AND Notification.CustID=Customers.CustID";
			ResultSet resultSet = statement.executeQuery(query);
			int count = 0;
			// Read targetqueue, message and CustID of notified customer from database
			while(resultSet.next()) {
				String custLoginName = resultSet.getString("CustLoginName");
				String message = resultSet.getString("Message");
				// User already has some messages
				if(updateNotifications.containsKey(custLoginName)) {
					updateNotifications.get(custLoginName).add("Message");
				} else {
					List<String>messages = new ArrayList<String>();
					messages.add(message);
					updateNotifications.put(custLoginName, messages);
				}
				markNotified(resultSet.getInt("CustID"), resultSet.getString("Type"));
				count++;
			}
			statement.close();
		} catch (SQLException e) {
			log.severe("Could not retrieve notifications");
			e.printStackTrace();
		}
		disconnect();		
		return updateNotifications;
	}
	
	// Marks the given user notified in db
	private void markNotified(int custId, String type) {
		connect();
		try {
			Statement statement = connection.createStatement();
			// Sets user notified
			String query = "UPDATE Notification SET Notified=" + true +
							" WHERE CustID=" + custId + " AND Type='" + type + "'";
			statement.executeUpdate(query);
			statement.close();
			log.info("NOTIFICATION UPDATE");
		} catch (SQLException e) {
			log.severe("Could not mark notified");
			e.printStackTrace();
		}
	}
}
