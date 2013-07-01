package com.iolma.registrar;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;

import com.iolma.registrar.config.Configuration;
import com.iolma.registrar.pool.DatabaseQuery;
import com.iolma.registrar.pool.DatabaseService;

public class RegistrationServlet extends SipServlet implements TimerListener  {

	private static final long serialVersionUID = 4260962358503280827L;
	private static Logger logger = Logger.getLogger(RegistrationServlet.class.getSimpleName());

	private ConcurrentHashMap<URI, ConcurrentHashMap<URI, Binding>> registrations = new ConcurrentHashMap<URI, ConcurrentHashMap<URI, Binding>>();
	//private ConcurrentHashMap<URI, ServletTimer> timers = new ConcurrentHashMap<URI, ServletTimer>();
	
	private DatabaseQuery query = null;
	private SipFactory sipFactory = null;
	//private TimerService timerService = null;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		sipFactory = (SipFactory) servletConfig.getServletContext().getAttribute(SipServlet.SIP_FACTORY);
		//timerService = (TimerService)servletConfig.getServletContext().getAttribute(SipServlet.TIMER_SERVICE);

		Configuration config = new Configuration();
		try {
			
			// load config
			config.configureLogger(getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "logger.properties");
			config.configurePool(getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "conf" + File.separator + "connection.properties");
			
			// start connection pool
			DatabaseService databaseService = new DatabaseService(config);
			databaseService.startup();
			
			// set query
			query = new DatabaseQuery(databaseService);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("RegistrationServlet: servlet has been started");
		
	}

	protected void doRegister(SipServletRequest request) throws ServletException, IOException {
		String id_user = ((SipURI)request.getFrom().getURI()).getUser();
		
		String expStr = request.getHeader("Expires");
		int expires = 0;
		if (expStr != null) {
			expires = Integer.parseInt(expStr);
		}
		
		HashMap<String, Object> user = query.selectOne("select * from `user` where id_user='" + id_user + "'");		
		if (user.size() > 0) {
			
			URI aor = request.getTo().getURI();
			Address contact = request.getAddressHeader("Contact");
			SipURI publicContactURI = sipFactory.createSipURI(((SipURI)contact.getURI()).getUser(), request.getRemoteHost());
			publicContactURI.setPort(request.getRemotePort());			
			Address publicContact = sipFactory.createAddress(publicContactURI);					

			String callID = request.getCallId();
			int seq = Integer.parseInt(new StringTokenizer(request.getHeader("CSeq").trim()).nextToken());
			URI key = publicContact.getURI();
			
			ConcurrentHashMap<URI, Binding> bindings = registrations.get(aor);
			if (bindings == null) {
				bindings = new ConcurrentHashMap<URI, Binding>();
				registrations.put(aor, bindings);
			}

			if (expires == 0) {
				// unregister
				logger.info("RegistrationServlet: doRegister -> UNREGISTER");
				removeBindings(aor);
			} else {
				// register
				logger.info("RegistrationServlet: doRegister -> REGISTER");
				
				if (!publicContact.isWildcard()) {
					Binding currentBinding = bindings.get(key);
					if (currentBinding != null) {
						if (!currentBinding.getCallID().equals(callID) || currentBinding.getSeq() < seq) {
							if (expires != 0) {
								currentBinding.update(callID, seq, expires);
							} else {
								removeBindings(aor);
							}
						}
					} else {
						if (expires > 0) {
							bindings.put(key, new Binding(publicContact, callID, seq, expires));
							//timers.put(aor, timerService.createTimer(request.getApplicationSession(), 10000, 10000, false, false, request.getSession().getId()));
						}
					}
				}
				
			}
			SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
			for (Binding b : bindings.values()) {
				Address address = sipFactory.createAddress(b.getContact());
				address.setExpires(b.getExpires());
				sipServletResponse.addAddressHeader("Contact", address, false);
			}
			sipServletResponse.send();
		} else {
			logger.fine("RegistrationServlet: doRegister -> TEMPORARILY UNAVAILABLE");
			SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_TEMPORARILY_UNAVAILABLE);
			sipServletResponse.send();
		}
	}

	protected void doInvite(SipServletRequest request) throws ServletException, IOException {
		forwardRequest(request);
	}

	protected void doUpdate(SipServletRequest request) throws ServletException, IOException {
		forwardRequest(request);
	}

	protected void doBye(SipServletRequest request) throws ServletException, IOException {
		forwardRequest(request);
	}
/*
	protected void doResponse(SipServletResponse response) throws ServletException, IOException {
		URI aor = response.getFrom().getURI();
		if (response.getStatus() == Response.REQUEST_TIMEOUT && response.getMethod().equals(Request.OPTIONS)) {
			removeBindings(aor);
		}
	}
*/	
	public void timeout(ServletTimer servletTimer) {
/*
		SipSession sipSession = servletTimer.getApplicationSession().getSipSession((String)servletTimer.getInfo());
		URI aor = sipSession.getRemoteParty().getURI();
		if(!State.TERMINATED.equals(sipSession.getState())) {
			try {
				List<URI> targets = getBindings(aor);
				for (int i=0; i < targets.size(); i++) {
					SipServletRequest request = sipFactory.createRequest(sipSession.getApplicationSession(), Request.OPTIONS, aor, targets.get(i));
					request.send();
				}
			} catch (IOException e) {
				logger.severe("RegistrationServlet: An unexpected exception occured while sending the BYE");
			}				
		}
*/
	}

	private void forwardRequest(SipServletRequest request) throws TooManyHopsException {
		if (request.isInitial()) {
			URI aor = request.getTo().getURI();
			Proxy proxy = request.getProxy();
			List<URI> targets = getBindings(aor);
			if (targets.size() > 0) {
				proxy.setRecordRoute(true);
				proxy.setRecurse(true);
				proxy.setParallel(false);
				proxy.proxyTo(targets);
			} else {
				proxy.proxyTo(aor);
			}
		}
	}

	private synchronized void removeBindings(URI aor) {
/*
		ServletTimer timer = timers.get(aor);
		if (timer != null) {
			timer.cancel();
			timers.remove(aor);
		}
*/
		ConcurrentHashMap<URI, Binding> bindings = registrations.get(aor);
		if (bindings != null) {
			bindings.clear();
		}
	}
	
	private synchronized List<URI> getBindings(URI aor) {
		ConcurrentHashMap<URI, Binding> map = registrations.get(aor);
		if (map != null) {
			List<URI> endpoints = new LinkedList<URI>();
			List<URI> expires = new LinkedList<URI>();
			for (Binding binding : map.values()) {
				URI contact = binding.getContact();
				if (binding.isExpired()) {
					expires.add(contact);
				} else {
					endpoints.add(contact);
				}
			}
			for (URI uri : expires) {
				map.remove(uri);
			}
			return endpoints;
		}
		return Collections.emptyList();
	}

}