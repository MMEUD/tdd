package com.iolma.registrar.configuration;

import com.iolma.registrar.RegistrarException;

public class ConfigurationException extends RegistrarException {

	private static final long serialVersionUID = -1251604254683860339L;

	public ConfigurationException() {}

	public ConfigurationException(String message) {
    	super(message);
	}

}
