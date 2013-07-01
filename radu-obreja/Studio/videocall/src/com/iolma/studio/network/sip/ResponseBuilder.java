package com.iolma.studio.network.sip;

import java.text.ParseException;
import java.util.ArrayList;

import javax.sip.address.Address;
import javax.sip.header.ContactHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class ResponseBuilder {

	private Configuration configuration = null;
	private SipContext sipContext = null;
	private ILogger logger = null;

	private String sipIPAddress = null;
	private int sipPort = 0;
	private Response response = null;
	private ToHeader toHeader = null;
	private ContactHeader contactHeader = null; 
	private UserAgentHeader userAgentHeader = null;

	public ResponseBuilder(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		this.logger = logger;
		if (sipContext.getAgent().getPublicHost() != null) {
			sipIPAddress = sipContext.getAgent().getPublicHost();
			sipPort = sipContext.getAgent().getPublicPort();
		} else {
			sipIPAddress = sipContext.getSipProvider().getListeningPoint(configuration.getSipTransport()).getIPAddress();
			sipPort = sipContext.getSipProvider().getListeningPoint(configuration.getSipTransport()).getPort();
		}
	}

	public void addRequest(Request request, int method) {
		try {
			response = sipContext.getMessageFactory().createResponse(Response.OK, request);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addToHeader() {
		try {
			toHeader = (ToHeader) response.getHeader(ToHeader.NAME);	         
			toHeader.setTag("4321");
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}
	
	public void addContactHeader() {
        Address address = null;
		try {
			address = sipContext.getAddressFactory().createAddress(configuration.getFromUser() + " <sip:"+ sipIPAddress + ":" + sipPort + ">");
	        contactHeader = sipContext.getHeaderFactory().createContactHeader(address);
			response.addHeader(contactHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addUserAgentHeader() {
		ArrayList<String> agents = new ArrayList<String>();
		agents.add("IOLMA Video Call v1.0 2013");
		try {
			userAgentHeader = sipContext.getHeaderFactory().createUserAgentHeader(agents);
			response.addHeader(userAgentHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public Response makeOkResponse(Request request) {
		addRequest(request, Response.OK);
		addToHeader();
		addContactHeader();
		addUserAgentHeader();	
		return response;
	}
	
	public Response makeBusyResponse(Request request) {
		addRequest(request, Response.TEMPORARILY_UNAVAILABLE);
		addToHeader();
		addContactHeader();
		addUserAgentHeader();
		return response;
	}
	
}
