package com.iolma.registrar.authorization;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.iolma.registrar.IConfiguration;
import com.iolma.registrar.RegistrarException;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DatabaseService {
	
	protected static Logger log = Logger.getLogger(DatabaseService.class.getName());
	private ComboPooledDataSource cpds = null;
	private IConfiguration config = null;

	public DatabaseService(IConfiguration config) {
		this.config = config;
	}
	
	public void startup() throws PropertyVetoException, RegistrarException, SQLException {
		
		shutdown();
		
		cpds = new ComboPooledDataSource();
		cpds.setDriverClass(config.get("connection.driver"));
		cpds.setJdbcUrl(config.get("connection.url")); 
		cpds.setUser(config.get("connection.username")); 
		cpds.setPassword(config.get("connection.password"));

		cpds.setMinPoolSize(config.getInteger("pool.min_size")); 
		cpds.setMaxPoolSize(config.getInteger("pool.max_size")); 
		cpds.setMaxStatements(config.getInteger("pool.max_statements")); 
		cpds.setMaxIdleTime(config.getInteger("pool.timeout")); 
		cpds.setAcquireIncrement(config.getInteger("pool.acquire_increment"));
		cpds.setIdleConnectionTestPeriod(config.getInteger("pool.idle_test_period"));
		cpds.setTestConnectionOnCheckin("true".equals(config.get("pool.validate")));

		test();
			
	}

	public void shutdown() {
		if (cpds != null) {
			cpds.close();
		}
		
	}
	
	private void test() throws SQLException, RegistrarException {
		Connection conn = aquire();
		Statement stm;
		stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(config.get("pool.test"));
		rs.close();
		stm.close();
		conn.close();
		log.info("Database pool initialised OK.");
	}
	
	public synchronized Connection aquire() throws SQLException, DatabaseException {
		return cpds.getConnection();
	}

}
