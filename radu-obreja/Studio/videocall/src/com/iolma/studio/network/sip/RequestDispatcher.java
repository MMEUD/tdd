package com.iolma.studio.network.sip;

import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class RequestDispatcher {

	private Configuration configuration = null;
	private ILogger logger = null;
	private SipContext sipContext = null;
	
	public RequestDispatcher(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		this.logger = logger;
	}
	
	public void process(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();

		logger.debug("SipListener -> processRequest event notification [transaction ID = " + serverTransactionId + "] " + request.getMethod());
		if (request.getMethod().equals(Request.BYE)) {
			//logger.debug(request.toString());			
			processBye(requestEvent); // fara context
		} else if (request.getMethod().equals(Request.INVITE)) {
			//logger.debug(request.toString());			
			processInvite(requestEvent); // context dialog, request, fire ringing
		} else if (request.getMethod().equals(Request.OPTIONS)) {
			processOptions(requestEvent); // context
		} else if (request.getMethod().equals(Request.CANCEL)) {
			//logger.debug(request.toString());			
			processCancel(requestEvent); // context
		}		
		
	}

	public void processBye(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
		if (serverTransactionId != null) {
			Response response = null;
			try {
				response = sipContext.getMessageFactory().createResponse(Response.OK, request);
				serverTransactionId.sendResponse(response);
                sipContext.setRinging(false);
                sipContext.setConnected(false);
				sipContext.getAgent().fireCallTerminated();
			} catch (ParseException e) {
				logger.error(e.getStackTrace().toString());
			} catch (SipException e) {
				logger.error(e.getStackTrace().toString());
			} catch (InvalidArgumentException e) {
				logger.error(e.getStackTrace().toString());
			}
		}
	}

	public void processInvite(RequestEvent requestEvent) {
		
        Request request = requestEvent.getRequest();
        sipContext.setInviteRequest(request);
        ServerTransaction inviteTransaction = requestEvent.getServerTransaction();
        try {
            Response response = sipContext.getMessageFactory().createResponse(Response.RINGING, request);
            
            if (inviteTransaction == null) {
            	inviteTransaction = sipContext.getSipProvider().getNewServerTransaction(request);
            }
            sipContext.setInviteTransaction(inviteTransaction);
            inviteTransaction.sendResponse(response);
                        
			if(sipContext.isConnected()) {        
				ResponseBuilder builder = new ResponseBuilder(configuration, sipContext, logger);
				Response busyResponse = builder.makeBusyResponse(request);
				inviteTransaction.sendResponse(busyResponse);
			} else {
	            sipContext.setRinging(true);
	            FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
	            SipURI fromURI = (SipURI)fromHeader.getAddress().getURI();
	            sipContext.getAgent().fireRinging(fromURI.getUser(), fromHeader.getAddress().getDisplayName());
			}
			
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }

    }
	
	public void processOptions(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
		Response response = null;
		try {
			if (serverTransactionId == null) {
				serverTransactionId = sipContext.getSipProvider().getNewServerTransaction(request);
			}
			response = sipContext.getMessageFactory().createResponse(200, request);
			serverTransactionId.sendResponse(response);
		} catch (ParseException e) {
			logger.error(e.getStackTrace().toString());
		} catch (SipException e) {
			logger.error(e.getStackTrace().toString());
		} catch (InvalidArgumentException e) {
			logger.error(e.getStackTrace().toString());
		}
	}
	
	public void processCancel(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
		Response response = null;
		try {
			if (serverTransactionId == null) {
				serverTransactionId = sipContext.getSipProvider().getNewServerTransaction(request);
			}
			response = sipContext.getMessageFactory().createResponse(200, request);
			serverTransactionId.sendResponse(response);
			
            sipContext.setConnected(false);
            sipContext.setRinging(false);
            sipContext.getAgent().fireCallTerminated();
		} catch (ParseException e) {
			logger.error(e.getStackTrace().toString());
		} catch (SipException e) {
			logger.error(e.getStackTrace().toString());
		} catch (InvalidArgumentException e) {
			logger.error(e.getStackTrace().toString());
		}
	}
	
}
