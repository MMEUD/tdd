package com.iolma.studio.network.sip;

import java.text.ParseException;
import java.util.ArrayList;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class RequestBuilder {
	
	private Configuration configuration = null;
	private SipContext sipContext = null;
	private ILogger logger = null;
	
	private String sipIPAddress = null;
	private int sipPort = 0;
	private FromHeader fromHeader = null;
	private ToHeader toHeader = null;
	private SipURI requestURI = null;
	private ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
	private MaxForwardsHeader maxForwards = null;
	private CallIdHeader callIdHeader = null;
	private ContactHeader contactHeader = null; 
	private CSeqHeader cSeqHeader = null;
	private UserAgentHeader userAgentHeader = null;
	private ExpiresHeader expiresHeader = null;
	private RouteHeader routeHeader = null;
	private AuthorizationHeader authorizationHeader = null;

	public RequestBuilder(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		if (sipContext.getAgent().getPublicHost() != null) {
			sipIPAddress = sipContext.getAgent().getPublicHost();
			sipPort = sipContext.getAgent().getPublicPort();
		} else {
			sipIPAddress = sipContext.getSipProvider().getListeningPoint(configuration.getSipTransport()).getIPAddress();
			sipPort = sipContext.getSipProvider().getListeningPoint(configuration.getSipTransport()).getPort();
		}
	}
	
	public void addFromHeader() {
		SipURI fromAddress = null;
		try {
			fromAddress = sipContext.getAddressFactory().createSipURI(configuration.getFromUser(), configuration.getSipDomain());
			Address fromNameAddress = sipContext.getAddressFactory().createAddress(fromAddress);
			fromNameAddress.setDisplayName(configuration.getFromName());
			fromHeader = sipContext.getHeaderFactory().createFromHeader(fromNameAddress, "12345");
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addFromHeader(String fromUser, String fromName) {
		SipURI fromAddress = null;
		try {
			fromAddress = sipContext.getAddressFactory().createSipURI(fromUser, configuration.getSipDomain());
			Address fromNameAddress = sipContext.getAddressFactory().createAddress(fromAddress);
			fromNameAddress.setDisplayName(fromName);
			fromHeader = sipContext.getHeaderFactory().createFromHeader(fromNameAddress, "12345");
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addToHeader() {
		SipURI toAddress = null;
		try {
			toAddress = sipContext.getAddressFactory().createSipURI(configuration.getToUser(), configuration.getSipDomain());
			Address toNameAddress = sipContext.getAddressFactory().createAddress(toAddress);
			toNameAddress.setDisplayName(configuration.getToName());
			toHeader = sipContext.getHeaderFactory().createToHeader(toNameAddress, null);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}

	public void addToHeader(String toUser, String toName) {
		SipURI toAddress = null;
		try {
			toAddress = sipContext.getAddressFactory().createSipURI(toUser, configuration.getSipDomain());
			Address toNameAddress = sipContext.getAddressFactory().createAddress(toAddress);
			toNameAddress.setDisplayName(toName);
			toHeader = sipContext.getHeaderFactory().createToHeader(toNameAddress, null);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addRequestURI() {
		try {
			requestURI = sipContext.getAddressFactory().createSipURI(null, configuration.getSipProxy());
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}
	
	public void addRequestURI(String userId) {
		try {
			requestURI = sipContext.getAddressFactory().createSipURI(userId, configuration.getSipProxy());
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}
	
	public void addViaHeader() {
		ViaHeader viaHeader = null;
		try {
			viaHeader = sipContext.getHeaderFactory().createViaHeader(sipIPAddress, sipPort, configuration.getSipTransport(),null);
			viaHeaders.add(viaHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
	}
	
	public void addMaxForwards() {
		try {
			maxForwards = sipContext.getHeaderFactory().createMaxForwardsHeader(70);
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
	}
	
	public void addCallIdHeader() {
		callIdHeader = sipContext.getSipProvider().getNewCallId();
	}
	
	public void addCallIdHeader(String callId) {
		callIdHeader = sipContext.getSipProvider().getNewCallId();
		if (callId.trim().length() > 0) {
			try {
				callIdHeader.setCallId(callId);
			} catch (ParseException e) {
				logger.error("ParseException : " + e.getMessage());
			}
		}
	}
	
	public void addContactHeader() {
		SipURI contactURI = null;
		try {
			contactURI = sipContext.getAddressFactory().createSipURI(configuration.getFromUser(), sipIPAddress);
			contactURI.setPort(sipPort);
			Address contactAddress = sipContext.getAddressFactory().createAddress(contactURI);
			contactAddress.setDisplayName(configuration.getFromName());
			contactHeader = sipContext.getHeaderFactory().createContactHeader(contactAddress);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addCSeqHeader(String method) {
		try {
			cSeqHeader = sipContext.getHeaderFactory().createCSeqHeader(sipContext.getNextNumSeq(), method);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}		
	}
	
	public void addUserAgentHeader() {
		ArrayList<String> agents = new ArrayList<String>();
		agents.add("IOLMA Video Call v1.0 2013");
		try {
			userAgentHeader = sipContext.getHeaderFactory().createUserAgentHeader(agents);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addExpiresHeader(int timeout) {
		try {
			expiresHeader = sipContext.getHeaderFactory().createExpiresHeader(timeout);
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
		
	}

	public void addRouteHeader() {
		SipURI addressRouter = null;
		try {
			addressRouter = sipContext.getAddressFactory().createSipURI(null, configuration.getSipProxy() + ";lr=on");
			Address address = sipContext.getAddressFactory().createAddress(addressRouter);
			routeHeader = sipContext.getHeaderFactory().createRouteHeader(address);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addAuthorizationHeader(Response response) {	
		URI uriReq = null;
		try {
			
			uriReq = sipContext.getAddressFactory().createURI("sip:" + configuration.getSipProxy());
			WWWAuthenticateHeader wwwAuthHeader = (WWWAuthenticateHeader) response.getHeader("WWW-Authenticate");
			String schema = wwwAuthHeader.getScheme();
			String nonce = wwwAuthHeader.getNonce();
			String realm = wwwAuthHeader.getRealm();
			DigestClientAuthenticationMethod digest = new DigestClientAuthenticationMethod();

			digest.initialize(realm, configuration.getFromUser(), uriReq.toString(), nonce, configuration.getFromPassword(), ((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod(), null, "MD5");

			String respuestaM = digest.generateResponse();
			authorizationHeader = sipContext.getHeaderFactory().createAuthorizationHeader(schema);
			authorizationHeader.setUsername(configuration.getFromUser());
			authorizationHeader.setRealm(realm);
			authorizationHeader.setNonce(nonce);
			authorizationHeader.setURI(uriReq);
			authorizationHeader.setResponse(respuestaM);
			authorizationHeader.setAlgorithm("MD5");
			
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
	
	public Request makeRegisterRequest(int timeout) {
		
		// get read of params I think
		addFromHeader(configuration.getFromUser(), configuration.getFromName());
		addToHeader(configuration.getFromUser(), configuration.getFromName());
		addRequestURI();
		addViaHeader();
		addMaxForwards();
		addCallIdHeader();
		addContactHeader();
		addCSeqHeader(Request.REGISTER);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		
		Request request = null;
		try {
			request = sipContext.getMessageFactory().createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeRegisterResponse(Response response, int timeout) {
		
		String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
		
		// get read of params I think
		addFromHeader(configuration.getFromUser(), configuration.getFromName());
		addToHeader(configuration.getFromUser(), configuration.getFromName());
		addRequestURI();
		addViaHeader();
		addMaxForwards();
		addCallIdHeader(callId);
		addContactHeader();
		addCSeqHeader(Request.REGISTER);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		addAuthorizationHeader(response);
		
		Request request = null;
		try {
			request = sipContext.getMessageFactory().createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
			request.addHeader(authorizationHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeInviteRequest(String userId, int timeout) {
		
		// get read of params I think
		addFromHeader(configuration.getFromUser(), configuration.getFromName());
		addToHeader(userId, null);
		addRequestURI(userId);
		addViaHeader();
		addMaxForwards();
		addCallIdHeader();
		addContactHeader();
		addCSeqHeader(Request.INVITE);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		
		Request request = null;
		try {
			request = sipContext.getMessageFactory().createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeInviteResponse(Response response, String userId, int timeout) {
		
		String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
		
		// get read of params I think
		addFromHeader(configuration.getFromUser(), configuration.getFromName());
		addToHeader(userId, null);
		addRequestURI(userId);
		addViaHeader();
		addMaxForwards();
		addCallIdHeader(callId);
		addContactHeader();
		addCSeqHeader(Request.INVITE);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		addAuthorizationHeader(response);
		
		Request request = null;
		try {
			request = sipContext.getMessageFactory().createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
			request.addHeader(authorizationHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}
	
	public Request makeByeRequest(String userId, int timeout) {
		String callId = sipContext.getInviteDialog().getCallId().getCallId();

		// get read of params I think
		addFromHeader(configuration.getFromUser(), null);
		addToHeader(userId, null);
		addRequestURI(userId);
		addViaHeader();
		addMaxForwards();
		addCallIdHeader(callId);
		addContactHeader();
		addCSeqHeader(Request.BYE);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		addRouteHeader();
		
		Request request = null;
		try {
			request = sipContext.getMessageFactory().createRequest(requestURI, Request.BYE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
			request.addHeader(routeHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	
}
