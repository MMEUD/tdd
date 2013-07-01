package com.iolma.registrar;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;

import com.iolma.registrar.authorization.AuthorizationService;
import com.iolma.registrar.configuration.Configuration;
import com.iolma.registrar.proxy.ProxyService;
import com.iolma.registrar.registration.RegistrationService;


public class RegistrarServlet extends SipServlet implements TimerListener  {

	private static final long serialVersionUID = 4260962358503280827L;
	private static Logger logger = Logger.getLogger(RegistrarServlet.class.getSimpleName());

	private IAuthorization authorizationService = null;
	private IProxy proxyService = null;
	private IRegistration registrationService = null;
	
	private SipFactory sipFactory = null;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		sipFactory = (SipFactory) servletConfig.getServletContext().getAttribute(SipServlet.SIP_FACTORY);

		try {
			Configuration config = new Configuration(getServletContext());
			authorizationService = new AuthorizationService(config);
			registrationService = new RegistrationService(sipFactory);
			proxyService = new ProxyService(registrationService);
		} catch (RegistrarException e1) {
			e1.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("RegistrationServlet: servlet has been started");		
	}

	protected void doRegister(SipServletRequest request) throws ServletException, IOException {
		String user = ((SipURI)request.getFrom().getURI()).getUser();
		String password = ((SipURI)request.getFrom().getURI()).getUserPassword();
		
		if (authorizationService.isUserAuthorized(user, password)) {
			try {
				registrationService.setBindings(request);
				List<URI> bindings = registrationService.getBindings(request.getFrom().getURI());
				SipServletResponse response = request.createResponse(SipServletResponse.SC_OK);
				for (URI b : bindings) {
					Address address = sipFactory.createAddress(b);
					response.addAddressHeader("Contact", address, false);
				}
				logger.fine("RegistrationServlet: doRegister -> OK");
				response.send();			
			} catch (RegistrarException e) {
				SipServletResponse response = request.createResponse(SipServletResponse.SC_AMBIGUOUS);
				logger.fine("RegistrationServlet: doRegister -> AMBIGUOUS");
				response.send();
			}
		} else {
			SipServletResponse response = request.createResponse(SipServletResponse.SC_TEMPORARILY_UNAVAILABLE);
			logger.fine("RegistrationServlet: doRegister -> TEMPORARILY UNAVAILABLE");
			response.send();
		}
	}

	protected void doInvite(SipServletRequest request) throws ServletException, IOException {
		try {
			proxyService.doProxy(request);
		} catch (TooManyHopsException e) {
			SipServletResponse response = request.createResponse(SipServletResponse.SC_TOO_MANY_HOPS);
			logger.fine("RegistrationServlet: doInvite -> SC_TOO_MANY_HOPS");
			response.send();
		}
	}

	protected void doUpdate(SipServletRequest request) throws ServletException, IOException {
		try {
			proxyService.doProxy(request);
		} catch (TooManyHopsException e) {
			SipServletResponse response = request.createResponse(SipServletResponse.SC_TOO_MANY_HOPS);
			logger.fine("RegistrationServlet: doUpdate -> SC_TOO_MANY_HOPS");
			response.send();
		}
	}

	protected void doBye(SipServletRequest request) throws ServletException, IOException {
		try {
			proxyService.doProxy(request);
		} catch (TooManyHopsException e) {
			SipServletResponse response = request.createResponse(SipServletResponse.SC_TOO_MANY_HOPS);
			logger.fine("RegistrationServlet: doBye -> SC_TOO_MANY_HOPS");
			response.send();
		}
	}

	public void timeout(ServletTimer arg0) {
	}

}