package com.iolma.registrar.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.servlet.ServletContext;

import com.iolma.registrar.IConfiguration;


public class Configuration implements IConfiguration {
	
	private Properties properties = new Properties();
	
	public Configuration(ServletContext context) throws ConfigurationException {
		try {
			configureLogger(context.getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "logger.properties");
			configurePool(context.getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "connection.properties");
		} catch (IOException e) {
			 throw new ConfigurationException("Configuration ERROR : " + e.getMessage());
		}
	}
	
	private void configurePool(String filePath) throws IOException {
		InputStream in = new FileInputStream(filePath);
    	properties.load(in);
	}
	
	private void configureLogger(String filePath) throws IOException {
		InputStream in = new FileInputStream(filePath);
		LogManager.getLogManager().readConfiguration(in);
	}

	public String get(String key) throws ConfigurationException {
		if (!properties.containsKey(key)) {
			throw new ConfigurationException("Configuration ERROR : Property '" + key + "' not found.");
		}
		return properties.getProperty(key);
	}
	
	public int getInteger(String key) throws ConfigurationException {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			throw new ConfigurationException("Configuration ERROR : Property '" + key + "' is not an integer.");
		}
	}
	
}
