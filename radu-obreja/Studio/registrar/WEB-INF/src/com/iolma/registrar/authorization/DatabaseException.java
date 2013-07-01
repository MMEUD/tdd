package com.iolma.registrar.authorization;

import com.iolma.registrar.RegistrarException;

public class DatabaseException extends RegistrarException {

	private static final long serialVersionUID = -1253604254683860339L;

	public DatabaseException() {}

	public DatabaseException(String message) {
    	super(message);
	}

}
