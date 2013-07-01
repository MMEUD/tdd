package com.iolma.registrar;

import java.util.List;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;

public interface IRegistration {
	
	public void setBindings(SipServletRequest request) throws RegistrarException;

	public List<URI> getBindings(URI addressOfRecord);
	
}
