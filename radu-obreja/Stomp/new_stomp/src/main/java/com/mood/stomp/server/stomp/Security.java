package com.mood.stomp.server.stomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hornetq.core.security.CheckType;
import org.hornetq.core.security.Role;
import org.hornetq.spi.core.security.HornetQSecurityManager;

public class Security extends Thread implements HornetQSecurityManager {

	private ConcurrentHashMap<String, String> users = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, List<String>> roles = new ConcurrentHashMap<String, List<String>>();
	private StompDao dao = null;

	
	public Security(StompDao dao) {
		this.dao = dao;
	}
		
	public boolean isStarted() {
		return true;
	}

	public void addRole(String user, String role) {
		if(!roles.containsKey(user)) {
			roles.put(user, new ArrayList<String>());
		}
		roles.get(user).add(role);
	}

	public void addUser(String user, String password) throws IllegalArgumentException  {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if(password == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}		
		users.put(user, password);		
	}

	public void removeRole(String user, String role) {
		if(roles.containsKey(user)) {
			roles.get(user).remove(role);
		}
	}

	public void removeUser(String user) {
		users.remove(user);
		roles.remove(user);
	}

	public void setDefaultUser(String user) {		
	}

	public boolean validateUser(String user, String password) {
		return dao.isUserValid(user, password);
	}

	public boolean validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType) {
		if (!validateUser(user, password)) {
			return false;
		}
		
		List<String> userRoles = this.roles.get(user);

		if(userRoles == null) {
			return false;
		}
		
		for (String userRole : userRoles) {
			if (roles != null) {
				
				for (Role role : roles) {
					// User has the role and role is allowed to perform action
					if (role.getName().equals(userRole) && checkType.hasRole(role)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
