package com.iolma.registrar;


public interface IConfiguration {
	
	public String get(String parameterName) throws RegistrarException;
	
	public int getInteger(String parameterName) throws RegistrarException;
	
}
