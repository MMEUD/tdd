package com.iolma.studio.network.sip;

import javax.sip.ClientTransaction;
import javax.sip.DialogState;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class ResponseDispatcher {

	private Configuration configuration = null;
	private ILogger logger = null;
	private SipContext sipContext = null;
	private Request ackRequest = null;
	
	public ResponseDispatcher(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		this.logger = logger;
	}
	
	public void process(ResponseEvent responseEvent) {
		
        Response response = (Response) responseEvent.getResponse();     
        ClientTransaction clientTransactionId = responseEvent.getClientTransaction();
        CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        
        //logger.debug("SipListener -> processResponse event notification [status = " + response.getStatusCode() + ", cseq = " + cseq.getSeqNumber() + "]\n");
		//logger.debug(response.toString());				

        if (clientTransactionId == null) {
            // RFC3261: MUST respond to every 2xx
            if (ackRequest != null && sipContext.getInviteDialog() != null) {
               //System.out.println("re-sending ACK");
               try {
            	   sipContext.getInviteDialog().sendAck(ackRequest);
               } catch (SipException se) {
                  se.printStackTrace();
               }
            }
            return;
        }

        try {
            if (response.getStatusCode() == Response.OK) {
                if (cseq.getMethod().equals(Request.INVITE)) {

                    ackRequest = sipContext.getInviteDialog().createAck( ((CSeqHeader)response.getHeader(CSeqHeader.NAME)).getSeqNumber() );         
                    sipContext.getInviteDialog().sendAck(ackRequest);        	            

                    String sdp = new String((byte[])response.getContent());
        	        sipContext.getAgent().startConnectivityEstablishment(sdp);

                } else if(cseq.getMethod().equals(Request.REGISTER)) {

                	ClientTransaction registerTransaction = sipContext.getRegisterTransaction();
    				int expires = registerTransaction.getRequest().getExpires().getExpires();
    				ContactHeader contact = (ContactHeader)response.getHeader("Contact");
    				if (contact != null) {
    					SipURI contactURI = (SipURI)contact.getAddress().getURI();
    					sipContext.getAgent().setPublicAddress(contactURI.getHost(), contactURI.getPort());
    				}
    				if (expires == 0) {
    					sipContext.setRegistered(false);
    					sipContext.getAgent().firePhoneUnRegistered();
    				} else {
    					sipContext.setRegistered(true);
    					sipContext.getAgent().firePhoneRegistered();
    				}
    				sipContext.setRegisterTransaction(null);

                } else if (cseq.getMethod().equals(Request.CANCEL)) {
                    
                	if (sipContext.getInviteDialog().getState() == DialogState.CONFIRMED) {
                        Request byeRequest = sipContext.getInviteDialog().createRequest(Request.BYE);
                        ClientTransaction ct = sipContext.getSipProvider().getNewClientTransaction(byeRequest);
                        sipContext.getInviteDialog().sendRequest(ct);
                    }
                    sipContext.setConnected(false);
                    sipContext.setRinging(false);
                    sipContext.getAgent().fireCallTerminated();
                    
                } else if (cseq.getMethod().equals(Request.BYE)) {
                    sipContext.setConnected(false);
                    sipContext.setRinging(false);
                    sipContext.getAgent().fireCallTerminated();
                }
            } else if (response.getStatusCode() == Response.NOT_FOUND 
            		|| response.getStatusCode()== Response.REQUEST_TIMEOUT
            		|| response.getStatusCode()== Response.TEMPORARILY_UNAVAILABLE){

            	sipContext.getAgent().fireError("Endpoint not available or busy");
                sipContext.setConnected(false);
            	sipContext.getAgent().fireCallTerminated();
            } else if (response.getStatusCode() == Response.DECLINE){
            	sipContext.getAgent().fireError("Endpoint is not answering");
                sipContext.setConnected(false);
                sipContext.setRinging(false);
            	sipContext.getAgent().fireCallTerminated();
            } else if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                    || response.getStatusCode() == Response.UNAUTHORIZED) {

            	if (cseq.getMethod().equals(Request.REGISTER)) {
	    			ClientTransaction registerTransaction = sipContext.getRegisterTransaction();
	    			if (registerTransaction != null) {
	    				int expires = registerTransaction.getRequest().getExpires().getExpires();
		    			RequestBuilder builder = new RequestBuilder(configuration, sipContext, logger);
		    			Request request = builder.makeRegisterResponse(response, expires);
	    				registerTransaction = sipContext.getSipProvider().getNewClientTransaction(request);
	    				registerTransaction.sendRequest();
						//logger.debug("RegisterActions -> authorize\n");
						//logger.debug(request.toString());				
	    			}
            	}
            	if (cseq.getMethod().equals(Request.INVITE)) {
	    			RequestBuilder builder = new RequestBuilder(configuration, sipContext, logger);
	    			Request request = builder.makeInviteResponse(response, sipContext.getUserId(), 3600);

	    	        String localSDP = sipContext.getAgent().getLocalSDP();
	    	        ContentTypeHeader contentTypeHeader = sipContext.getHeaderFactory().createContentTypeHeader("application","sdp");
	    	        request.setContent(localSDP.getBytes(), contentTypeHeader);
	    			
	    			ClientTransaction inviteTransaction = sipContext.getSipProvider().getNewClientTransaction(request);
	    			inviteTransaction.sendRequest();
	    			sipContext.setInviteDialog(inviteTransaction.getDialog());
        			//logger.debug("CallActions -> invite authorize\n");
        			//logger.debug(request.toString());
            	}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
       		
	}
	
	
}
