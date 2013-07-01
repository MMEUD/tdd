package com.iolma.studio.network.sip;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;

public class RegisterActions {

	private Configuration configuration = null;
	private ILogger logger = null;
	private SipContext sipContext = null;

	public RegisterActions(Configuration configuration, SipContext sipContext, ILogger logger) {
		this.configuration = configuration;
		this.sipContext = sipContext;
		this.logger = logger;
	}
	
	public void register() {
		try {
			RequestBuilder builder = new RequestBuilder(configuration, sipContext, logger);
			Request request = builder.makeRegisterRequest(3600);
			logger.debug("RegisterActions -> register\n");
			logger.debug(request.toString());				
			if (request != null) {
				ClientTransaction registerTransaction = sipContext.getSipProvider().getNewClientTransaction(request);
				sipContext.setRegisterTransaction(registerTransaction);
				registerTransaction.sendRequest();
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
	
	public void unregister() {
		try {
			RequestBuilder builder = new RequestBuilder(configuration, sipContext, logger);
			Request request = builder.makeRegisterRequest(0);
			logger.debug("RegisterActions -> unregister\n");
			logger.debug(request.toString());				
			if (request != null) {
				ClientTransaction registerTransaction = sipContext.getSipProvider().getNewClientTransaction(request);
				sipContext.setRegisterTransaction(registerTransaction);
				registerTransaction.sendRequest();
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
	
}
