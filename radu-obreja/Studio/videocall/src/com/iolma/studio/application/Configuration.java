package com.iolma.studio.application;

public class Configuration {

	// JNLP
	private String design = "";
	private String fromUser = "";
	private String fromPassword = "";
	private String fromName = "";
	private String toUser = "";
	private String toName = "";

	// SIP
	private String sipTransport = "udp";
	private String sipDomain = "leon.telecast.ro";
	private int sipPort = 5060;
	private String sipProxy = "leon.telecast.ro";
	private int sipProxyPort = 5080;
	
	// RTP
	private int rtpPort = 8000;
	
	
	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getFromUser() {
		return fromUser;
	}
	
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	
	public String getFromPassword() {
		return fromPassword;
	}
	
	public void setFromPassword(String fromPassword) {
		this.fromPassword = fromPassword;
	}
	
	public String getFromName() {
		return fromName;
	}
	
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	public String getToUser() {
		return toUser;
	}
	
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	
	public String getToName() {
		return toName;
	}
	
	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getSipTransport() {
		return sipTransport;
	}

	public void setSipTransport(String sipTransport) {
		this.sipTransport = sipTransport;
	}

	public String getSipDomain() {
		return sipDomain;
	}

	public void setSipDomain(String sipDomain) {
		this.sipDomain = sipDomain;
	}

	public int getSipPort() {
		return sipPort;
	}

	public void setSipPort(int sipPort) {
		this.sipPort = sipPort;
	}

	public String getSipProxy() {
		return sipProxy;
	}

	public void setSipProxy(String sipProxy) {
		this.sipProxy = sipProxy;
	}

	public int getSipProxyPort() {
		return sipProxyPort;
	}

	public void setSipProxyPort(int sipProxyPort) {
		this.sipProxyPort = sipProxyPort;
	}

	public int getRtpPort() {
		return rtpPort;
	}

	public void setRtpPort(int rtpPort) {
		this.rtpPort = rtpPort;
	}

}
