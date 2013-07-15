package com.iolma.studio.call.agent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.address.SipURI;

import com.iolma.studio.call.ICallListener;
import com.iolma.studio.call.RTPConfig;
import com.iolma.studio.log.ILogger;

public class CallListenersNotificator {

	private ILogger logger;
	private ConcurrentHashMap<String, ICallListener> listeners = new ConcurrentHashMap<String, ICallListener>();

	public CallListenersNotificator(ILogger logger) {
		this.logger = logger;		
	}
	
	public void addListener(String key, ICallListener listener) {
		logger.info("UserAgent addListener " + key);
		listeners.put(key, listener);
	}

	public void removeListener(String key) {
		listeners.remove(key);
	}

	public synchronized void firePhoneRegistered() {
		//logger.debug("UserAgent -> firePhoneRegistered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onPhoneRegistered();
		}
	}

	public synchronized void firePhoneUnRegistered() {
		//logger.debug("UserAgent -> firePhoneUnRegistered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onPhoneUnRegistered();
		}
	}

	public synchronized void fireRinging(SipURI sourceURI) {
		//logger.debug("UserAgent -> fireRinging : " + sourceURI);
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onRinging(sourceURI);
		}
	}

	public synchronized void fireCalling() {
		//logger.debug("UserAgent -> fireCalling");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCalling();
		}
	}
	
	public synchronized void fireCallAnswered(RTPConfig rtpConfig) {
		//logger.debug("UserAgent -> fireCallAnswered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCallAnswered(rtpConfig);
		}
	}
	
	public synchronized void fireCallTerminated() {
		//logger.debug("UserAgent -> fireCallTerminated");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCallTerminated();
		}
	}
	
	public synchronized void fireError(String message) {
		//logger.debug("UserAgent -> fireError : " + message);
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onError(message);
		}
	}
}
