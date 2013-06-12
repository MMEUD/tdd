package com.mood.stomp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

import com.mood.stomp.Stomp;

public class Configuration {
	
	private Properties properties = new Properties();
	
	public void configureStomp(String relativeFilePath) throws IOException {
		File path = new File(Stomp.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		try {
	    	properties.load(new FileInputStream(path.getAbsolutePath() + relativeFilePath));
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void configureLogger(String resourceFilePath) throws IOException {
		InputStream in = Stomp.class.getResourceAsStream(resourceFilePath);
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
