package com.iolma.studio.application;

import javax.sip.address.Address;

public class Configuration implements IApplicationConfig {

	private int sipLocalPort = 0;
	private String sipProxyHost = null;
	private int sipProxyPort = 0;
	private Address localAddress = null;
	private String localPassword = null;
	private String remoteUser = null;
	
	public int getSipLocalPort() {
		return sipLocalPort;
	}

	public void setSipLocalPort(int sipLocalPort) {
		this.sipLocalPort = sipLocalPort;
	}

	public String getSipProxyHost() {
		return sipProxyHost;
	}

	public void setSipProxyHost(String sipProxyHost) {
		this.sipProxyHost = sipProxyHost;
	}

	public int getSipProxyPort() {
		return sipProxyPort;
	}

	public void setSipProxyPort(int sipProxyPort) {
		this.sipProxyPort = sipProxyPort;
	}

	public Address getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(Address localAddress) {
		this.localAddress = localAddress;
	}

	public String getLocalPassword() {
		return localPassword;
	}

	public void setLocalPassword(String localPassword) {
		this.localPassword = localPassword;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

}
