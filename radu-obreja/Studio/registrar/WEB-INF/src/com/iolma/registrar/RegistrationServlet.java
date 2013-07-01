package com.iolma.sip;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;

import com.iolma.sip.config.Configuration;
import com.iolma.sip.pool.DatabaseQuery;
import com.iolma.sip.pool.DatabaseService;

public class RegistrarSipServlet extends SipServlet implements TimerListener {

	private static final long serialVersionUID = 4260962358503280827L;
	private static Logger logger = Logger.getLogger(RegistrarSipServlet.class.getSimpleName());
	
	private DatabaseQuery query = null;

	/** Creates a new instance of SimpleProxyServlet */
	public RegistrarSipServlet() {
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		Configuration config = new Configuration();
		try {
			config.configureLogger(getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "logger.properties");
			config.configurePool(getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "connection.properties");
			DatabaseService databaseService = new DatabaseService(config);
			databaseService.startup();
			query = new DatabaseQuery(databaseService);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("RegistrarSipServlet: servlet has been started");
		logger.info("Real path = " + this.getServletContext().getRealPath("/"));
		
	}

	protected void doRegister(SipServletRequest request) throws ServletException, IOException {
		logger.info(request.toString());
		logger.info("RegistrarSipServlet: Got request:\n" + request.getMethod());
		logger.info("RegistrarSipServlet: from isSipURI = " + request.getFrom().getURI().isSipURI());		
		logger.info("RegistrarSipServlet: from username = " + ((SipURI)request.getFrom().getURI()).getUser());		
		logger.info("RegistrarSipServlet: from password = " + ((SipURI)request.getFrom().getURI()).getUserPassword());		

		String expStr = request.getHeader("Expires");
		int expires = 0;
		if (expStr != null) {
			expires = Integer.parseInt(expStr);
			logger.info("RegistrarSipServlet: expires = " + expires);		
		}

		if (expires == 0) {
			// unregister
			logger.info("RegistrarSipServlet: UNREGISTER");		
		} else {
			// register
			logger.info("RegistrarSipServlet: REGISTER");		
			logger.info("RegistrarSipServlet: to isSipURI = " + request.getTo().getURI().isSipURI());		
			logger.info("RegistrarSipServlet: to username = " + ((SipURI)request.getTo().getURI()).getUser());		
			logger.info("RegistrarSipServlet: to password = " + ((SipURI)request.getTo().getURI()).getUserPassword());		

			String contact = request.getHeader("Contact");
	        logger.info("Register contact: " + contact);
		}

		SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
		sipServletResponse.send();
	}

	protected void doInvite(SipServletRequest request) throws ServletException, IOException {
		logger.info(request.toString());
		logger.info("RegistrarSipServlet: Got request:\n" + request.getMethod());
		SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
		sipServletResponse.send();
		sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
		sipServletResponse.send();
	}

	protected void doBye(SipServletRequest request) throws ServletException, IOException {
		logger.info(request.toString());
		logger.info("RegistrarSipServlet: Got BYE request:\n" + request);
		SipServletResponse sipServletResponse = request.createResponse(200);
		sipServletResponse.send();
		SipApplicationSession sipApplicationSession = sipServletResponse.getApplicationSession(false);
		if(sipApplicationSession != null && sipApplicationSession.isValid()) {
			sipApplicationSession.invalidate();
		}		
	}

	protected void doResponse(SipServletResponse response) throws ServletException, IOException {
		logger.info(response.toString());
		logger.info("RegistrarSipServlet: Got response:\n" + response);
		if(SipServletResponse.SC_OK == response.getStatus() && "BYE".equalsIgnoreCase(response.getMethod())) {
			SipApplicationSession sipApplicationSession = response.getApplicationSession(false);
			if(sipApplicationSession != null && sipApplicationSession.isValid()) {
				sipApplicationSession.invalidate();
			}			
		}
	}

	public void timeout(ServletTimer servletTimer) {
		SipSession sipSession = servletTimer.getApplicationSession().getSipSession((String)servletTimer.getInfo());
		if(!State.TERMINATED.equals(sipSession.getState())) {
			try {
				sipSession.createRequest("BYE").send();
			} catch (IOException e) {
				logger.severe("RegistrarSipServlet: An unexpected exception occured while sending the BYE");
			}				
		}
	}

}