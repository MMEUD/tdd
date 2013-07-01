package com.iolma.studio.network.sip;

import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class CallActions {
	
	private Configuration configuration = null;
	private ILogger logger = null;
	private SipContext sipContext = null;

	public CallActions(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		this.logger = logger;
	}
		
	public void call(String userId) {
		sipContext.setUserId(userId);
		try {
			RequestBuilder builder = new RequestBuilder(configuration, sipContext, logger);
			Request request = builder.makeInviteRequest(userId, 3600);
			
	        String sdp = sipContext.getAgent().getLocalSDP();			
	        ContentTypeHeader contentTypeHeader = sipContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	        request.setContent(sdp.getBytes(), contentTypeHeader);

			ClientTransaction inviteTransaction = sipContext.getSipProvider().getNewClientTransaction(request);
			sipContext.setInviteDialog(inviteTransaction.getDialog());
			inviteTransaction.sendRequest();
			//logger.debug("CallActions -> invite\n");
			//logger.debug(request.toString());				
			
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void answer() {
		try {
			ResponseBuilder builder = new ResponseBuilder(configuration, sipContext, logger);
			Response okResponse = builder.makeOkResponse(sipContext.getInviteRequest());

            String localSDP = sipContext.getAgent().getLocalSDP(); 
	        ContentTypeHeader contentTypeHeader = sipContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	        okResponse.setContent(localSDP.toString().getBytes(), contentTypeHeader);
        	sipContext.getInviteTransaction().sendResponse(okResponse);
			//logger.debug("CallActions -> answer\n");
			//logger.debug(okResponse.toString());
			
            String remoteSDP = new String((byte[])sipContext.getInviteRequest().getContent());
	        sipContext.getAgent().setControlling(true);	        
            sipContext.getAgent().startConnectivityEstablishment(remoteSDP);

		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (SipException e) {
			logger.error("SipException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException : " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("InterruptedException : " + e.getMessage());
		}

	}

	public void terminate() {
		try {

			if (sipContext.getInviteDialog() != null) {
	            Request byeRequest = sipContext.getInviteDialog().createRequest(Request.BYE);
				//logger.debug("CallActions -> caller bye\n");
				//logger.debug(byeRequest.toString());
	            ClientTransaction ct = sipContext.getSipProvider().getNewClientTransaction(byeRequest);
	            sipContext.getInviteDialog().sendRequest(ct);
			}
            
			if (sipContext.getInviteTransaction().getDialog() != null) {
	            Request byeRequest = sipContext.getInviteTransaction().getDialog().createRequest(Request.BYE);
				//logger.debug("CallActions -> calee bye\n");
				//logger.debug(byeRequest.toString());
	            ClientTransaction ct = sipContext.getSipProvider().getNewClientTransaction(byeRequest);
	            sipContext.getInviteTransaction().getDialog().sendRequest(ct);
			}
			
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
	
}
