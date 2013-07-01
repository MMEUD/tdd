package com.iolma.registrar.registration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

import com.iolma.registrar.IRegistration;
import com.iolma.registrar.RegistrarException;

public class RegistrationService implements IRegistration {

	private static Logger logger = Logger.getLogger(RegistrationService.class.getSimpleName());
	
	private SipFactory sipFactory = null;
	private ConcurrentHashMap<URI, ConcurrentHashMap<URI, Binding>> registrations = new ConcurrentHashMap<URI, ConcurrentHashMap<URI, Binding>>();

	public RegistrationService(SipFactory sipFactory) {
		this.sipFactory = sipFactory;
	}
	
	public void setBindings(SipServletRequest request) throws RegistrarException {
		try {
			int expires = getExpires(request);
			URI addressOfRecord = getAddressOfRecord(request);
			Address publicContact = getPublicContact(request);

			String callID = getCallId(request);
			int seq = getSeq(request);
			URI bindingKey = publicContact.getURI();
			
			ConcurrentHashMap<URI, Binding> bindings = registrations.get(addressOfRecord);
			if (bindings == null) {
				bindings = new ConcurrentHashMap<URI, Binding>();
				registrations.put(addressOfRecord, bindings);
			}

			if (expires == 0) {
				logger.fine("RegistrationServlet: doRegister -> UNREGISTER");
				removeBindings(addressOfRecord);
			} else {
				logger.fine("RegistrationServlet: doRegister -> REGISTER");				
				if (!publicContact.isWildcard()) {
					Binding currentBinding = bindings.get(bindingKey);
					if (currentBinding != null) {
						if (!currentBinding.getCallID().equals(callID) || currentBinding.getSeq() < seq) {
							if (expires != 0) {
								currentBinding.update(callID, seq, expires);
							} else {
								removeBindings(addressOfRecord);
							}
						}
					} else {
						if (expires > 0) {
							bindings.put(bindingKey, new Binding(publicContact, callID, seq, expires));
						}
					}
				}
				
			}
		} catch (Exception e) {			
			throw new RegistrarException("setBindings Exception : " + e.getMessage());
		}
		
	}

	private int getExpires(SipServletRequest request) {
		String expStr = request.getHeader("Expires");
		if (expStr != null) {
			return Integer.parseInt(expStr);	
		}
		return 0;
	}

	private URI getAddressOfRecord(SipServletRequest request) {
		return request.getTo().getURI();
	}
	
	private String getCallId(SipServletRequest request) {
		return request.getCallId();
	}
	
	private Address getPublicContact(SipServletRequest request) throws ServletParseException {
		Address contact = request.getAddressHeader("Contact");
		SipURI publicContactURI = sipFactory.createSipURI(((SipURI)contact.getURI()).getUser(), request.getRemoteHost());
		publicContactURI.setPort(request.getRemotePort());			
		return sipFactory.createAddress(publicContactURI);							
	}
	
	private int getSeq(SipServletRequest request) {
		return Integer.parseInt(new StringTokenizer(request.getHeader("CSeq").trim()).nextToken());
	}

	private synchronized void removeBindings(URI addressOfRecord) {
		ConcurrentHashMap<URI, Binding> bindings = registrations.get(addressOfRecord);
		if (bindings != null) {
			bindings.clear();
		}
	}
	
	public List<URI> getBindings(URI addressOfRecord) {
		ConcurrentHashMap<URI, Binding> map = registrations.get(addressOfRecord);
		if (map != null) {
			List<URI> endpoints = new LinkedList<URI>();
			List<URI> expires = new LinkedList<URI>();
			for (Binding binding : map.values()) {
				URI contact = binding.getContact();
				if (binding.isExpired()) {
					expires.add(contact);
				} else {
					endpoints.add(contact);
				}
			}
			for (URI uri : expires) {
				map.remove(uri);
			}
			return endpoints;
		}
		return Collections.emptyList();
	}

}
