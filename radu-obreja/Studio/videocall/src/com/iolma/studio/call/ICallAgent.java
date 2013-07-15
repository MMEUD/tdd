package com.iolma.studio.call;

import javax.sip.address.Address;

public interface ICallAgent {

	public final static int USERSTATE_UNREGISTERED = 0;
	public final static int USERSTATE_REGISTERED = 1;

	public final static int CALLSTATE_IDLE = 0;
	public final static int CALLSTATE_CALLING = 1;
	public final static int CALLSTATE_RINGING = 2;
	public final static int CALLSTATE_TALK = 3;

	public void addListener(String key, ICallListener listener);
	
	public void removeListener(String key);
	
	public void initiateCall(Address target);
	
	public void answerCall();
	
	public void terminateCall();

}
