package com.mood.stomp.server.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mood.stomp.config.Configuration;
import com.mood.stomp.config.ConfigurationException;
import com.mood.stomp.server.IService;


public class DatabaseService implements IService {
	
	protected static Logger log = Logger.getLogger(DatabaseService.class.getName());
	private ComboPooledDataSource cpds = null;
	private Configuration config = null;

	public DatabaseService(Configuration config) {
		this.config = config;
	}
	
	public void startup() {
		
		try {
			
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
			
		} catch (PropertyVetoException e) {
			log.severe(e.getMessage());
		} catch (SQLException e) {
			log.severe(e.getMessage());
		} catch (ConfigurationException e) {
			log.severe(e.getMessage());
		} catch (DatabaseException e) {
			log.severe(e.getMessage());
		}		
		
	}

	public void shutdown() {
		if (cpds != null) {
			cpds.close();
		}
		
	}
	
	private void test() throws SQLException, DatabaseException, ConfigurationException {
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
