package com.iolma.studio.call.agent;

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
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class RequestBuilder {
	
	private ICallAgentConfig config = null;
	private CallContext callContext = null;
	private ILogger logger = null;
	
	private String localUser = null;
	private String localDisplayName = null;
	
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
	private AuthorizationHeader authorizationHeader = null;

	public RequestBuilder(ICallAgentConfig config, CallContext callContext, ILogger logger) {
		this.config = config;
		this.callContext = callContext;
		localUser = ((SipURI)config.getLocalAddress().getURI()).getUser();
		localDisplayName = config.getLocalAddress().getDisplayName();
	}
	
	public void addFromHeader(Address localAddress) {
		try {
			fromHeader = callContext.getHeaderFactory().createFromHeader(localAddress, localUser);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addToHeader(Address remoteAddress) {
		try {
			toHeader = callContext.getHeaderFactory().createToHeader(remoteAddress, null);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addRequestURI() {
		try {
			requestURI = callContext.getAddressFactory().createSipURI(null, config.getSipProxyHost());
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}
	
	public void addRequestURI(Address remoteAddress) {
		requestURI = (SipURI) remoteAddress.getURI();
	}
	
	public void addViaHeader() {
		ViaHeader viaHeader = null;
		try {
			viaHeader = callContext.getHeaderFactory().createViaHeader(callContext.getPublicHost(), callContext.getPublicPort(), CallContext.SIP_TRANSPORT, null);
			viaHeaders.add(viaHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
	}
	
	public void addMaxForwards() {
		try {
			maxForwards = callContext.getHeaderFactory().createMaxForwardsHeader(70);
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
	}
	
	public void addCallIdHeader() {
		callIdHeader = callContext.getSipProvider().getNewCallId();
	}
	
	public void addCallIdHeader(String callId) {
		callIdHeader = callContext.getSipProvider().getNewCallId();
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
			contactURI = callContext.getAddressFactory().createSipURI(localUser, callContext.getPublicHost());
			contactURI.setPort(callContext.getPublicPort());
			Address contactAddress = callContext.getAddressFactory().createAddress(contactURI);
			contactAddress.setDisplayName(localDisplayName);
			contactHeader = callContext.getHeaderFactory().createContactHeader(contactAddress);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}		
	}
	
	public void addCSeqHeader(String method) {
		try {
			cSeqHeader = callContext.getHeaderFactory().createCSeqHeader(callContext.getNextNumSeq(), method);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}		
	}
	
	public void addUserAgentHeader() {
		ArrayList<String> agents = new ArrayList<String>();
		agents.add("IOLMA Video Call");
		try {
			userAgentHeader = callContext.getHeaderFactory().createUserAgentHeader(agents);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addExpiresHeader(int timeout) {
		try {
			expiresHeader = callContext.getHeaderFactory().createExpiresHeader(timeout);
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		}
		
	}

	public void addAuthorizationHeader(Response response) {	
		URI uriReq = null;
		try {
			
			uriReq = callContext.getAddressFactory().createURI("sip:" + config.getSipProxyHost());
			WWWAuthenticateHeader wwwAuthHeader = (WWWAuthenticateHeader) response.getHeader("WWW-Authenticate");
			String schema = wwwAuthHeader.getScheme();
			String nonce = wwwAuthHeader.getNonce();
			String realm = wwwAuthHeader.getRealm();
			DigestAuthentication digest = new DigestAuthentication();

			digest.initialize(realm, localUser, uriReq.toString(), nonce, config.getLocalPassword(), ((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod(), null, "MD5");

			String respuestaM = digest.generateResponse();
			authorizationHeader = callContext.getHeaderFactory().createAuthorizationHeader(schema);
			authorizationHeader.setUsername(localUser);
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
		
		addFromHeader(config.getLocalAddress());
		addToHeader(config.getLocalAddress());
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
			request = callContext.getMessageFactory().createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
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
		
		addFromHeader(config.getLocalAddress());
		addToHeader(config.getLocalAddress());
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
			request = callContext.getMessageFactory().createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
			request.addHeader(authorizationHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeInviteRequest(Address remoteAddress, int timeout) {
		
		addFromHeader(config.getLocalAddress());
		addToHeader(remoteAddress);
		addRequestURI(remoteAddress);
		addViaHeader();
		addMaxForwards();
		addCallIdHeader();
		addContactHeader();
		addCSeqHeader(Request.INVITE);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		
		Request request = null;
		try {
			request = callContext.getMessageFactory().createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeInviteResponse(Response response, Address remoteAddress, int timeout) {
		
		String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
		
		addFromHeader(config.getLocalAddress());
		addToHeader(remoteAddress);
		addRequestURI(remoteAddress);
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
			request = callContext.getMessageFactory().createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
			request.addHeader(authorizationHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

	public Request makeCancelRequest(Address remoteAddress, int timeout) {
		
		addFromHeader(config.getLocalAddress());
		addToHeader(remoteAddress);
		addRequestURI(remoteAddress);
		addViaHeader();
		addMaxForwards();
		addCallIdHeader();
		addContactHeader();
		addCSeqHeader(Request.CANCEL);
		addUserAgentHeader();
		addExpiresHeader(timeout);
		
		Request request = null;
		try {
			request = callContext.getMessageFactory().createRequest(requestURI, Request.CANCEL, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			request.addHeader(contactHeader);
			request.addHeader(userAgentHeader);
			request.setExpires(expiresHeader);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}			
		
		return request;
	}

}
