package com.iolma.studio.call.agent;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class ResponseDispatcher {

	private ICallAgentConfig config = null;
	private ILogger logger = null;
	private CallContext callContext = null;
	private Request ackRequest = null;
	
	public ResponseDispatcher(ICallAgentConfig config, CallContext callContext, ILogger logger) {
		this.config = config;
		this.callContext = callContext;
		this.logger = logger;
	}
	
	public void process(ResponseEvent responseEvent) {
		
        Response response = (Response) responseEvent.getResponse();     
        ClientTransaction clientTransaction = responseEvent.getClientTransaction();
        CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        
        if (clientTransaction == null) {
            if (ackRequest != null && callContext.getCallClientTransaction() != null && callContext.getCallClientTransaction().getDialog() != null) {
               try {
            	   //logger.debug(ackRequest.toString());
            	   callContext.getCallClientTransaction().getDialog().sendAck(ackRequest);
               } catch (SipException se) {
                  se.printStackTrace();
               }
            }
            return;
        }

        try {
            if (response.getStatusCode() == Response.OK) {
                if (cseq.getMethod().equals(Request.INVITE)) {

                    ackRequest = callContext.getCallClientTransaction().getDialog().createAck( ((CSeqHeader)response.getHeader(CSeqHeader.NAME)).getSeqNumber() );
                    //logger.debug(ackRequest.toString());
                    callContext.getCallClientTransaction().getDialog().sendAck(ackRequest);

                    FromHeader fromHeader = (FromHeader)response.getHeader("From");
            		callContext.setRemoteAddress(fromHeader.getAddress());
                    ContactHeader contactHeader = (ContactHeader)response.getHeader("Contact");
                    callContext.setRemotePublicAddress(contactHeader.getAddress());

                    String remoteSdp = new String((byte[])response.getContent());
        			RTPConfigManager rtpConfigManager = new RTPConfigManager(config, logger, callContext);
        			rtpConfigManager.parseRemoteSDP(remoteSdp);
                	
        	        callContext.setCallState(ICallAgent.CALLSTATE_TALK);	        
                    callContext.getListenersNotificator().fireCallAnswered(callContext.getRtpConfig());

                } else if(cseq.getMethod().equals(Request.REGISTER)) {

                	ClientTransaction registerTransaction = callContext.getUserTransaction();
    				int expires = registerTransaction.getRequest().getExpires().getExpires();
    				ContactHeader contact = (ContactHeader)response.getHeader("Contact");
    				if (contact != null) {
    					SipURI contactURI = (SipURI)contact.getAddress().getURI();
    					callContext.setPublicAddress(contactURI.getHost(), contactURI.getPort());
    				}
    				if (expires == 0) {
    					callContext.setUserState(ICallAgent.USERSTATE_UNREGISTERED);
    					callContext.getListenersNotificator().firePhoneUnRegistered();
    				} else {
    					callContext.setUserState(ICallAgent.USERSTATE_REGISTERED);
    					callContext.getListenersNotificator().firePhoneRegistered();
    				}
    				callContext.setUserTransaction(null);

                } else if (cseq.getMethod().equals(Request.CANCEL)) {
                	
                	callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
                    callContext.getListenersNotificator().fireCallTerminated();

                } else if (cseq.getMethod().equals(Request.BYE)) {
                	
                	callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
                    callContext.getListenersNotificator().fireCallTerminated();
                    
                }
            } else if (response.getStatusCode() == Response.NOT_FOUND 
            		|| response.getStatusCode()== Response.REQUEST_TIMEOUT
            		|| response.getStatusCode()== Response.TEMPORARILY_UNAVAILABLE){

            	callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
                callContext.getListenersNotificator().fireError("Not available or busy");
                callContext.getListenersNotificator().fireCallTerminated();
                
            } else if (response.getStatusCode() == Response.DECLINE) {
            	
            	callContext.setCallState(ICallAgent.CALLSTATE_IDLE);
                callContext.getListenersNotificator().fireError("Call declined");
                callContext.getListenersNotificator().fireCallTerminated();
                
            } else if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                    || response.getStatusCode() == Response.UNAUTHORIZED) {

            	if (cseq.getMethod().equals(Request.REGISTER)) {
	    			ClientTransaction registerTransaction = callContext.getUserTransaction();
	    			if (registerTransaction != null) {
	    				int expires = registerTransaction.getRequest().getExpires().getExpires();
		    			RequestBuilder builder = new RequestBuilder(config, callContext, logger);
		    			Request request = builder.makeRegisterResponse(response, expires);
	    				registerTransaction = callContext.getSipProvider().getNewClientTransaction(request);
	    				//logger.debug(request.toString());
	    				registerTransaction.sendRequest();
	    			}
            	}
            	if (cseq.getMethod().equals(Request.INVITE)) {
	    			RequestBuilder builder = new RequestBuilder(config, callContext, logger);
	    			Request request = builder.makeInviteResponse(response, callContext.getRemoteAddress(), 3600);

	    			RTPConfigManager rtpConfigManager = new RTPConfigManager(config, logger, callContext);
	    	        String localSdp = rtpConfigManager.getLocalSDP();			
	    	        ContentTypeHeader contentTypeHeader = callContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	    	        request.setContent(localSdp.getBytes(), contentTypeHeader);
	    			
	    			ClientTransaction inviteTransaction = callContext.getSipProvider().getNewClientTransaction(request);
    				//logger.debug(request.toString());
	    			inviteTransaction.sendRequest();
            	}
            	
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       		
	}
	
	
}
