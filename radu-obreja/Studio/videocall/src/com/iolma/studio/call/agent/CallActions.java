package com.iolma.studio.call.agent;

import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class CallActions {

	private ICallAgentConfig config = null;
	private ILogger logger = null;
	private CallContext callContext = null;
	

	public CallActions(ICallAgentConfig config, CallContext callContext, ILogger logger) {
		this.config = config;
		this.callContext = callContext;
		this.logger = logger;
	}
		
	public void register() {
		try {
			RequestBuilder builder = new RequestBuilder(config, callContext, logger);
			Request request = builder.makeRegisterRequest(3600);
			if (request != null) {
				ClientTransaction userTransaction = callContext.getSipProvider().getNewClientTransaction(request);
				callContext.setUserTransaction(userTransaction);
				//logger.debug(request.toString());				
				userTransaction.sendRequest();
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
	
	public void unregister() {
		try {
			RequestBuilder builder = new RequestBuilder(config, callContext, logger);
			Request request = builder.makeRegisterRequest(0);
			if (request != null) {
				ClientTransaction userTransaction = callContext.getSipProvider().getNewClientTransaction(request);
				callContext.setUserTransaction(userTransaction);
				//logger.debug(request.toString());				
				userTransaction.sendRequest();
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void initiateCall(Address remoteAddress) {
		try {
			RequestBuilder builder = new RequestBuilder(config, callContext, logger);
			Request request = builder.makeInviteRequest(remoteAddress, 3600);
			
			RTPConfigManager rtpConfigManager = new RTPConfigManager(config, logger, callContext);
			rtpConfigManager.generateLocalRTPConfig();
	        String localSDP = rtpConfigManager.getLocalSDP();			
	        ContentTypeHeader contentTypeHeader = callContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	        request.setContent(localSDP.getBytes(), contentTypeHeader);

	        ClientTransaction inviteTransaction = callContext.getSipProvider().getNewClientTransaction(request);
			callContext.setCallClientTransaction(inviteTransaction);
			//logger.debug(request.toString());
			inviteTransaction.sendRequest();

			callContext.setRemoteAddress(remoteAddress);
			callContext.setCallState(ICallAgent.CALLSTATE_CALLING);	        
            callContext.getListenersNotificator().fireCalling();
			
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void answerCall() {
		try {
			RTPConfigManager rtpConfigManager = new RTPConfigManager(config, logger, callContext);
			rtpConfigManager.generateLocalRTPConfig();
			
			ResponseBuilder builder = new ResponseBuilder(config, callContext, logger);
			Response response = builder.makeOkResponse(callContext.getCallInviteRequest());
			
            String localSDP = rtpConfigManager.getLocalSDP(); 
	        ContentTypeHeader contentTypeHeader = callContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	        response.setContent(localSDP.toString().getBytes(), contentTypeHeader);
			//logger.debug(response.toString());				
        	callContext.getCallServerTransaction().getDialog().sendReliableProvisionalResponse(response);

        	String remoteSDP = new String((byte[])callContext.getCallInviteRequest().getContent());
        	rtpConfigManager.parseRemoteSDP(remoteSDP);
        	
	        callContext.setCallState(ICallAgent.CALLSTATE_TALK);	        
            callContext.getListenersNotificator().fireCallAnswered(callContext.getRtpConfig());

		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (SipException e) {
			logger.error("SipException : " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException : " + e.getMessage());
		}

	}

	public void updateCall() {
		try {
			if (callContext.getCallDialog() != null) {
	            Request request = callContext.getCallDialog().createRequest(Request.UPDATE);
	            request.setRequestURI(callContext.getRemotePublicAddress().getURI());
	            ClientTransaction ct = callContext.getSipProvider().getNewClientTransaction(request);
				//logger.debug(request.toString());
	            callContext.getCallDialog().sendRequest(ct);
			}            
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void terminateCall() {
		if (callContext.getCallState() == CallAgent.CALLSTATE_CALLING) {
			try {
				Request request = callContext.getCallClientTransaction().createCancel();
				ClientTransaction cancelTransaction = callContext.getSipProvider().getNewClientTransaction(request);
				//logger.debug(request.toString());				
				cancelTransaction.sendRequest();
			} catch (SipException e) {
				logger.error("SipException : " + e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error("IllegalArgumentException : " + e.getMessage());
			}
		}
		if (callContext.getCallState() == CallAgent.CALLSTATE_RINGING) {
			try {
				ResponseBuilder builder = new ResponseBuilder(config, callContext, logger);
				Response response = builder.makeDeclineResponse(callContext.getCallInviteRequest());				
				//logger.debug(response.toString());
				callContext.getCallServerTransaction().sendResponse(response);
				
		        callContext.setCallState(ICallAgent.CALLSTATE_IDLE);	        
	            callContext.getListenersNotificator().fireCallTerminated();				
			} catch (SipException e) {
				logger.error("SipException : " + e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error("IllegalArgumentException : " + e.getMessage());
			} catch (InvalidArgumentException e) {
				logger.error("InvalidArgumentException : " + e.getMessage());
			}
		}
		if (callContext.getCallState() == CallAgent.CALLSTATE_TALK) {
			try {
				if (callContext.getCallDialog() != null) {
		            Request request = callContext.getCallDialog().createRequest(Request.BYE);
		            request.setRequestURI(callContext.getRemotePublicAddress().getURI());
		            ClientTransaction ct = callContext.getSipProvider().getNewClientTransaction(request);
					//logger.debug(request.toString());				
		            callContext.getCallDialog().sendRequest(ct);
				}            
			} catch (Exception e) {
				logger.error("Exception : " + e.getMessage());
			}
		}
	}

}
