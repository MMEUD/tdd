package com.iolma.studio.network.sip;

import java.net.InetAddress;

public class RTPConfig {
	
	private String streamType = null;
	private int localRtpPort = 0;
	private int localRtpcPort = 0;
	private InetAddress remoteIp = null;
	private int remoteRtpPort = 0;
	private int remoteRtpcPort = 0;
	
	public String getStreamType() {
		return streamType;
	}

	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}

	public int getLocalRtpPort() {
		return localRtpPort;
	}
	
	public void setLocalRtpPort(int localRtpPort) {
		this.localRtpPort = localRtpPort;
	}

	public int getLocalRtpcPort() {
		return localRtpcPort;
	}
	
	public void setLocalRtpcPort(int localRtpcPort) {
		this.localRtpcPort = localRtpcPort;
	}

	public InetAddress getRemoteIp() {
		return remoteIp;
	}
	
	public void setRemoteIp(InetAddress remoteIp) {
		this.remoteIp = remoteIp;
	}

	public int getRemoteRtpPort() {
		return remoteRtpPort;
	}
	
	public void setRemoteRtpPort(int remoteRtpPort) {
		this.remoteRtpPort = remoteRtpPort;
	}

	public int getRemoteRtpcPort() {
		return remoteRtpcPort;
	}
	
	public void setRemoteRtpcPort(int remoteRtpcPort) {
		this.remoteRtpcPort = remoteRtpcPort;
	}

	public String toString() {
//		return "RTP [" + streamType + "] : localRtpPort = " +  localRtpPort + ", localRtpcPort = " + localRtpcPort +
//				", remoteIp = " + remoteIp.getHostAddress() + ", remoteRtpPort = " + remoteRtpPort + ", remoteRtpcPort" + remoteRtpcPort;
		return "RTP [" + streamType + "]";
	}
}
