package com.agenda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConn {
	Connection connection = null;
	public OracleDbConn(){
		this.connection=getConnection();
	}	
	public Connection getConnection() {	
		String driverName = "com.mysql.jdbc.Driver";
		String conectionURI = "jdbc:mysql://localhost:3306/agenda";	
		//String driverName = "oracle.jdbc.driver.OracleDriver";
		//String conectionURI = "jdbc:oracle:thin:@oracle1.mmr.local:1521:oracle1l";
		String username = "root";//"mwtshopi2";
		String password ="mcse2003";// "mwtshopi2";
		try {
			Class.forName(driverName);
			try {
				connection = DriverManager.getConnection(conectionURI,username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}
}