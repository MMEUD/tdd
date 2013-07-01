package com.iolma.registrar.authorization;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.HashMap;

import com.iolma.registrar.IAuthorization;
import com.iolma.registrar.IConfiguration;
import com.iolma.registrar.RegistrarException;

public class AuthorizationService implements IAuthorization {

	private DatabaseQuery query = null;
	
	public AuthorizationService(IConfiguration configuration) throws PropertyVetoException, RegistrarException, SQLException {
		DatabaseService databaseService = new DatabaseService(configuration);
		databaseService.startup();		
		query = new DatabaseQuery(databaseService);		
	}
	
	public boolean isUserAuthorized(String user, String password) {
		HashMap<String, Object> theUser = query.selectOne("select * from `user` where id_user='" + user + "'");		
		return (theUser.size() > 0);
	}

}
