package com.mood.stomp.server.pool;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = -1253604254683860339L;

	public DatabaseException() {}

	public DatabaseException(String message) {
    	super(message);
	}

}
