package com.iolma.registrar.proxy;

import java.util.List;

import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;

import com.iolma.registrar.IProxy;
import com.iolma.registrar.IRegistration;

public class ProxyService implements IProxy {
	
	private IRegistration registrationService = null;
	
	public ProxyService(IRegistration registrationService) {
		this.registrationService = registrationService;
	}
	
	public void doProxy(SipServletRequest request) throws TooManyHopsException {
		if (request.isInitial()) {
			URI aor = request.getTo().getURI();
			Proxy proxy = request.getProxy();
			List<URI> bindings = registrationService.getBindings(aor);
			if (bindings.size() > 0) {
				proxy.setRecordRoute(true);
				proxy.setRecurse(true);
				proxy.setParallel(false);
				proxy.proxyTo(bindings);
			} else {
				proxy.proxyTo(aor);
			}
		}
	}

}
