package com.iolma.studio.call;

import javax.sip.address.SipURI;


public interface ICallListener {
	
	public void onPhoneRegistered();

	public void onPhoneUnRegistered();

	public void onRinging(SipURI sourceURI);

	public void onCalling();

	public void onCallAnswered(RTPConfig rtpConfig);
	
	public void onCallTerminated();

	public void onError(String message);
	
}
