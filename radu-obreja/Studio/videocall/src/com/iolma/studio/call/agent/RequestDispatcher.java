package com.iolma.studio.call.agent;

import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class RequestDispatcher {

	private ICallAgentConfig config = null;
	private ILogger logger = null;
	private CallContext callContext = null;
	
	public RequestDispatcher(ICallAgentConfig config, CallContext callContext, ILogger logger) {
		this.config = config;
		this.callContext = callContext;
		this.logger = logger;
	}
	
	public void process(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		//logger.debug(request.toString());
		if (request.getMethod().equals(Request.INVITE)) {
			processInvite(requestEvent);
		} else if (request.getMethod().equals(Request.UPDATE)) {
			processUpdate(requestEvent);
		} else if (request.getMethod().equals(Request.CANCEL)) {
			processCancel(requestEvent);
		} else if (request.getMethod().equals(Request.BYE)) {
			processBye(requestEvent);
		}				
	}

	private void processInvite(RequestEvent requestEvent) {		
        Request request = requestEvent.getRequest();
        callContext.setCallInviteRequest(request);
        ServerTransaction inviteTransaction = requestEvent.getServerTransaction();
        Response response;
        try {            
            if (inviteTransaction == null) {
            	inviteTransaction = callContext.getSipProvider().getNewServerTransaction(request);
            }            
            callContext.setCallServerTransaction(inviteTransaction);
            response = callContext.getMessageFactory().createResponse(Response.RINGING, request);
			//logger.debug(response.toString());
            inviteTransaction.sendResponse(response);
                        
			if(callContext.getCallState() == ICallAgent.CALLSTATE_TALK) {        
				ResponseBuilder builder = new ResponseBuilder(config, callContext, logger);
				Response busyResponse = builder.makeBusyResponse(request);
				//logger.debug(busyResponse.toString());
				inviteTransaction.sendResponse(busyResponse);
			} else {
                FromHeader fromHeader = (FromHeader)request.getHeader("From");
        		callContext.setRemoteAddress(fromHeader.getAddress());
                ContactHeader contactHeader = (ContactHeader)request.getHeader("Contact");
                callContext.setRemotePublicAddress(contactHeader.getAddress());
				
	            callContext.setCallState(ICallAgent.CALLSTATE_RINGING);
	            callContext.getListenersNotificator().fireRinging((SipURI)fromHeader.getAddress().getURI());	            
			}
			
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private void processUpdate(RequestEvent requestEvent) {		
        Request request = requestEvent.getRequest();
        ServerTransaction updateTransaction = requestEvent.getServerTransaction();
        Response response = null;
        try {
            if (updateTransaction == null) {
            	updateTransaction = callContext.getSipProvider().getNewServerTransaction(request);
            }
            response = callContext.getMessageFactory().createResponse(Response.OK, request);
            //logger.debug(response.toString());
            updateTransaction.sendResponse(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private void processCancel(RequestEvent requestEvent) {		
        Request request = requestEvent.getRequest();
        ServerTransaction cancelTransaction = requestEvent.getServerTransaction();
        Response response = null;
        try {
            if (cancelTransaction == null) {
            	cancelTransaction = callContext.getSipProvider().getNewServerTransaction(request);
            }
            response = callContext.getMessageFactory().createResponse(Response.OK, request);
            //logger.debug(response.toString());
            cancelTransaction.sendResponse(response);
        
            callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
            callContext.getListenersNotificator().fireError("Call canceled");
            callContext.getListenersNotificator().fireCallTerminated();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private void processBye(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
		if (serverTransactionId != null) {
			Response response = null;
			try {
				response = callContext.getMessageFactory().createResponse(Response.OK, request);
				//logger.debug(response.toString());
				serverTransactionId.sendResponse(response);
				
                callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
				callContext.getListenersNotificator().fireCallTerminated();
			} catch (ParseException e) {
				logger.error(e.getStackTrace().toString());
			} catch (SipException e) {
				logger.error(e.getStackTrace().toString());
			} catch (InvalidArgumentException e) {
				logger.error(e.getStackTrace().toString());
			}
		}
	}

}
