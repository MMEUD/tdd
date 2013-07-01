package com.iolma.studio.network.sip;

public interface IUserAgentListener {
	
	public void onPhoneRegistered();

	public void onPhoneUnRegistered();

	public void onRinging(String userId, String userName);

	public void onCalling();

	public void onCallAnswered();
	
	public void onCallTerminated();

	public void onError(String message);
	
}
