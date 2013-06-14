package com.mood.stomp.server.stomp;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.mood.stomp.server.pool.DatabaseQuery;

public class StompDao {

	protected static Logger log = Logger.getLogger(StompDao.class.getName());
	private DatabaseQuery query = null; 
	
	public StompDao(DatabaseQuery query) {
		this.query = query;
	}

	public Customer getCustomer(String loginName) {
		String sql = "SELECT CustLoginName, ContLoginName, Passwd FROM Customers, Contacts, CustCont " +  
				"WHERE Customers.CustLoginName='" + loginName + "' " +
				"AND CustCont.Removed IS NULL " +
				"AND Customers.CustID=CustCont.CustID " +
				"AND Contacts.ContID=CustCont.ContID ";

		return null;
	}
	
	public Map<String, Customer> getCustomers() {
		String sql = "SELECT Customers.CustID, CustLoginName, ContLoginName, Passwd FROM Customers, Contacts, CustCont " +  
				"WHERE CustCont.Removed IS NULL " +
				"AND Customers.CustLoginName<>'' " +
				"AND Customers.CustID=CustCont.CustID " +
				"AND Contacts.ContID=CustCont.ContID ";
		return null;
	}
	
	public boolean isUserValid(String contactName, String password) {
		String sql = "SELECT COUNT(*) FROM Contacts ,CustCont WHERE ContLoginName='" + contactName +
				"' AND Contacts.ContID=CustCont.ContID AND Passwd='" + password + "' AND Removed IS NULL";
		
		return true;
	}
	
	public Map<String, List<String>> getNotificationsJukebox() {
		String sql = "SELECT Customers.CustID, Type, CustLoginName, Message FROM Customers, NotificationJukebox " +
				"WHERE Notified=" + false + " AND NotificationJukebox.CustID=Customers.CustID";
		return null;
	}
	
	public Map<String, List<String>> getNotifications() {
		String sql = "SELECT Customers.CustID, Type, CustLoginName, Message FROM Customers, Notification " +
		        "WHERE Notified=" + false + " AND Notification.CustID=Customers.CustID";
		return null;
	}
	
}
