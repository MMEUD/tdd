package com.agenda.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.agenda.entity.UsersEntity;

public class LoginDao {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public UsersEntity loginAction(String username, String password) throws SQLException {
		connection = new OracleDbConn().getConnection();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("ExceptionGetConn: " + e.toString());
		}
		String sqlStatement = "SELECT * FROM utilisateur WHERE id_utilisateur='" + username + "' AND utilisateur_password='" + password + "' ";
		rs = statement.executeQuery(sqlStatement);
		UsersEntity userEntity = null;
		userEntity = new UsersEntity();
	
		boolean val = rs.next();
		if (val == false) {
			userEntity.setId_utilisateur("null");
			userEntity.setUtilisateur_prenom("null");
		} else {
			userEntity.setId_utilisateur(rs.getString("id_utilisateur"));
		    userEntity.setUtilisateur_prenom(rs.getString("utilisateur_prenom"));
			userEntity.setUtilisateur_password(rs.getString("utilisateur_password"));
		}
		statement.close();
		connection.close();
		return userEntity;
	}
}
