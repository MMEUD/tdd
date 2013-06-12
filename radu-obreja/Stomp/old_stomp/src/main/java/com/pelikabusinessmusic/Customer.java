package com.pelikabusinessmusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single 
 * messagebroker user.
 * 
 * @author eeroki
 *
 */

public class Customer {
	
	private int id = 0;
	private String name = null;
	private Map<String, String> contacts = new HashMap<String, String>(); 
	
	public Customer(){
	}
	
	public Customer(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addContact(String name, String password) {
		getContacts().put(name, password);
	}
	
	public boolean hasContact(String contactName) {
		return contacts.containsKey(contactName);
	}
	
	public boolean hasPassword(String password) {
		return contacts.containsValue(password);
	}
	
	public String getContactPassword(String contactName) {
		return contacts.get(contactName);
	}
	
	public void setContacts(Map<String, String> contacts) {
		this.contacts = contacts;
	}

	public Map<String, String> getContacts() {
		return contacts;
	}
	
	public List<String> getContactNameList() {
		List<String> contactNameList = new ArrayList<String>();
		
		for(String contactName : contacts.keySet()) {
			contactNameList.add(contactName);
		}
		
		return contactNameList;
	}
}

