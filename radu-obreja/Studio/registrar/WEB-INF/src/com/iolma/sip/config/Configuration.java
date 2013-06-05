package com.iolma.sip.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

public class Configuration {
	
	private Properties properties = new Properties();
	
	public void configurePool(String filePath) throws IOException {
		InputStream in = new FileInputStream(filePath);
		try {
	    	properties.load(in);
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void configureLogger(String filePath) throws IOException {
		InputStream in = new FileInputStream(filePath);
		try {
			LogManager.getLogManager().readConfiguration(in);
		} catch (IOException e) {
			throw e;
		}
	}

	public String get(String key) throws ConfigurationException {
		if (!properties.containsKey(key)) {
			throw new ConfigurationException("Property '" + key + "' not found.");
		}
		return properties.getProperty(key);
	}
	
	public int getInteger(String key) throws ConfigurationException {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			throw new ConfigurationException("Property '" + key + "' is not an integer.");
		}
	}
	
}
