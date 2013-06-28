package com.iolma.sip;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.SipSession.State;

import org.apache.log4j.Logger;

/**
 * This example shows a simple User agent that can any accept call and reply to BYE or initiate BYE 
 * depending on the sender.
 * 
 * @author Jean Deruelle
 *
 */
public class RegistrarSipServlet extends SipServlet implements TimerListener {
	private static Logger logger = Logger.getLogger(RegistrarSipServlet.class);
	private static final long serialVersionUID = 1L;
	private static final String CALLEE_SEND_BYE = "YouSendBye";
	//60 sec
	private static final int DEFAULT_BYE_DELAY = 60000;
	
	private int byeDelay = DEFAULT_BYE_DELAY;
	
	/** Creates a new instance of SimpleProxyServlet */
	public RegistrarSipServlet() {
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		logger.info("the simple sip servlet has been started");
		super.init(servletConfig);
		String byeDelayStr = getServletContext().getInitParameter("bye.delay");		
		try{
			byeDelay = Integer.parseInt(byeDelayStr);
		} catch (NumberFormatException e) {
			logger.error("Impossible to parse the bye delay : " + byeDelayStr, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRegister(SipServletRequest request) throws ServletException,
	IOException {
		if(logger.isInfoEnabled()) {
			logger.info("Simple Servlet: Got request:\n"
				+ request.getMethod());
		}
		logger.info("REGISTER !!!");
		SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
		sipServletResponse.send();
	}

	protected void doInvite(SipServletRequest request) throws ServletException,
			IOException {
		
		if(logger.isInfoEnabled()) {
			logger.info("Simple Servlet: Got request:\n"
				+ request.getMethod());
		}
		SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
		sipServletResponse.send();
		sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
		sipServletResponse.send();
		if(CALLEE_SEND_BYE.equalsIgnoreCase(((SipURI)request.getTo().getURI()).getUser())) {
			TimerService timer = (TimerService) getServletContext().getAttribute(TIMER_SERVICE);			
			timer.createTimer(request.getApplicationSession(), byeDelay, false, request.getSession().getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doBye(SipServletRequest request) throws ServletException,
			IOException {
		if(logger.isInfoEnabled()) {
			logger.info("SimpleProxyServlet: Got BYE request:\n" + request);
		}
		SipServletResponse sipServletResponse = request.createResponse(200);
		sipServletResponse.send();
		SipApplicationSession sipApplicationSession = sipServletResponse.getApplicationSession(false);
		if(sipApplicationSession != null && sipApplicationSession.isValid()) {
			sipApplicationSession.invalidate();
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doResponse(SipServletResponse response)
			throws ServletException, IOException {
		if(logger.isInfoEnabled()) {
			logger.info("SimpleProxyServlet: Got response:\n" + response);
		}
		if(SipServletResponse.SC_OK == response.getStatus() && "BYE".equalsIgnoreCase(response.getMethod())) {
			SipApplicationSession sipApplicationSession = response.getApplicationSession(false);
			if(sipApplicationSession != null && sipApplicationSession.isValid()) {
				sipApplicationSession.invalidate();
			}			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.sip.TimerListener#timeout(javax.servlet.sip.ServletTimer)
	 */
	public void timeout(ServletTimer servletTimer) {
		SipSession sipSession = servletTimer.getApplicationSession().getSipSession((String)servletTimer.getInfo());
		if(!State.TERMINATED.equals(sipSession.getState())) {
			try {
				sipSession.createRequest("BYE").send();
			} catch (IOException e) {
				logger.error("An unexpected exception occured while sending the BYE", e);
			}				
		}
	}
}