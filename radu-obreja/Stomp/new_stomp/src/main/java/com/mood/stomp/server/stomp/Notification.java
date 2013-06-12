package com.mood.stomp.server.stomp;


public class Notification {

	private Customer customer = null;
	private String action = null;
	private int priority = 0;	

	public Notification(Customer customer, String action, int priority) {
		this.customer = customer;
		this.action = action;
		this.priority = priority;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getAction() {
		return action;
	}

	public int getPriority() {
		return priority;
	}	
	
}
