package com.iolma.registrar;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.TooManyHopsException;

public interface IProxy {
	
	public void doProxy(SipServletRequest request) throws TooManyHopsException;

}
