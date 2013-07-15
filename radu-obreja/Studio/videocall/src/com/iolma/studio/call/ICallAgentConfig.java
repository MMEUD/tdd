package com.iolma.studio.call;

import javax.sip.address.Address;

public interface ICallAgentConfig {

	public int getSipLocalPort();

	public void setSipLocalPort(int sipLocalPort);
	
	public String getSipProxyHost();
	
	public void setSipProxyHost(String sipProxyHost);
	
	public int getSipProxyPort();
	
	public void setSipProxyPort(int sipProxyPort);
	
	public Address getLocalAddress();
	
	public void setLocalAddress(Address localAddress);
	
	public String getLocalPassword();
	
	public void setLocalPassword(String localPassword);
	
}
