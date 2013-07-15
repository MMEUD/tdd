package com.iolma.studio.call.agent;

import java.text.ParseException;
import java.util.ArrayList;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class ResponseBuilder {

	private CallContext callContext = null;
	private ILogger logger = null;

	private String localUser = null;
	private String localDisplayName = null;
	
	private Response response = null;
	private ToHeader toHeader = null;
	private ContactHeader contactHeader = null; 
	private UserAgentHeader userAgentHeader = null;

	public ResponseBuilder(ICallAgentConfig config, CallContext callContext, ILogger logger) {
		this.callContext = callContext;
		this.logger = logger;
		localUser = ((SipURI)config.getLocalAddress().getURI()).getUser();
		localDisplayName = config.getLocalAddress().getDisplayName();
	}

	public void addRequest(Request request, int method) {
		try {
			response = callContext.getMessageFactory().createResponse(method, request);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addToHeader() {
		try {
			toHeader = (ToHeader) response.getHeader(ToHeader.NAME);	         
			toHeader.setTag(localUser);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}
	
	public void addContactHeader() {
		SipURI contactURI = null;
		try {
			contactURI = callContext.getAddressFactory().createSipURI(localUser, callContext.getPublicHost());
			contactURI.setPort(callContext.getPublicPort());
			Address contactAddress = callContext.getAddressFactory().createAddress(contactURI);
			contactAddress.setDisplayName(localDisplayName);
			contactHeader = callContext.getHeaderFactory().createContactHeader(contactAddress);
			response.addHeader(contactHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addUserAgentHeader() {
		ArrayList<String> agents = new ArrayList<String>();
		agents.add("IOLMA Video Call");
		try {
			userAgentHeader = callContext.getHeaderFactory().createUserAgentHeader(agents);
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

	public Response makeDeclineResponse(Request request) {
		addRequest(request, Response.DECLINE);
		addToHeader();
		addContactHeader();
		addUserAgentHeader();
		return response;
	}
	
}
