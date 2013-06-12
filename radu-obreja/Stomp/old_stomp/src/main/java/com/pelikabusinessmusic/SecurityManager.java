package com.pelikabusinessmusic;

/**
 * Handles security management
 * of the messaging server
 * 
 * @author eeroki
 *
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.hornetq.core.security.CheckType;
import org.hornetq.core.security.Role;
import org.hornetq.spi.core.security.HornetQSecurityManager;

public class SecurityManager implements HornetQSecurityManager {

	// valid users
	private final Map<String, String> users = new HashMap<String, String>();
	
	// roles for users
	private final Map<String, List<String>> roles = new HashMap<String, List<String>>();
	
	// database connection
	private DbConnect dbConnect = null;
	
	protected static Logger log = Logger.getLogger(SecurityManager.class.getName());
	
	public SecurityManager() {
	}
	
	public SecurityManager(DbConnect dbConnect) {
		this.dbConnect = dbConnect;
	}

	public boolean isStarted() {
		// Always running
		return true;
	}

	public void start() throws Exception {
		// Nothing to do at start
	}

	public void stop() throws Exception {
		users.clear();
		roles.clear();
		dbConnect.disconnect();
	}

	public void addRole(final String user, final String role) {
	
		if(roles.get(user) == null) {
			roles.put(user, new ArrayList<String>());
		}
		roles.get(user).add(role);
	}
	
	public void addUser(final String user, final String password) throws IllegalArgumentException {
		
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if(password == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		
		users.put(user, password);
	}

	public void removeRole(final String user, final String role) {
		
		if(roles.get(user) != null) {
			roles.get(user).remove(role);
		}
	}

	public void removeUser(final String user) {
		users.remove(user);
		roles.remove(user);
	}

	public void setDefaultUser(final String user) {
		
		// no need for defaultUser

	}
	
	// Verifies that user is a valid user
	public boolean validateUser(final String user, final String password) {
		
		// Need both values for validation
		if(user == null || password == null) {
			return false;
		}
		
		// Validate user against server
		if(users.containsKey(user) && users.get(user).equals(password)) {
				return true;
		}
		// Need to validate user against db
		// so fetch user info from db
		else {
			try {
				return dbConnect.isValidUser(user, password);
				
			} catch (SQLException e) {
				log.warning("Validation of user " + user + " failed. " + e.getMessage());
				return false;
			}
		}		
	}

	// Checks if user is allowed for requested action
	public boolean validateUserAndRole(String user, String password, Set<Role> roles,
			CheckType checkType) {
		
		if(!validateUser(user, password)) {
			return false;
		}
		
		List<String> userRoles = this.roles.get(user);

		// Check if user has required role
		if(userRoles == null) {
			return false;
		}
		
		for(String userRole : userRoles) {
			if(roles != null) {
				
				for(Role role : roles) {
					// User has the role and role is allowed to perform action
					if(role.getName().equals(userRole) && checkType.hasRole(role)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
