package com.iolma.studio.call;


public class RTPConfig {
	
	private String localPrivateHost = null;
	private String localPublicHost = null;
	private int localAudioPort = 0;
	private int localVideoPort = 0;
	
	private String remotePrivateHost = null;
	private String remotePublicHost = null;
	private int remoteAudioPort = 0;
	private int remoteVideoPort = 0;

	public String getLocalPrivateHost() {
		return localPrivateHost;
	}

	public void setLocalPrivateHost(String localPrivateHost) {
		this.localPrivateHost = localPrivateHost;
	}

	public String getLocalPublicHost() {
		return localPublicHost;
	}

	public void setLocalPublicHost(String localPublicHost) {
		this.localPublicHost = localPublicHost;
	}

	public int getLocalAudioPort() {
		return localAudioPort;
	}

	public void setLocalAudioPort(int localAudioPort) {
		this.localAudioPort = localAudioPort;
	}

	public int getLocalVideoPort() {
		return localVideoPort;
	}

	public void setLocalVideoPort(int localVideoPort) {
		this.localVideoPort = localVideoPort;
	}

	public String getRemotePrivateHost() {
		return remotePrivateHost;
	}

	public void setRemotePrivateHost(String remotePrivateHost) {
		this.remotePrivateHost = remotePrivateHost;
	}

	public String getRemotePublicHost() {
		return remotePublicHost;
	}

	public void setRemotePublicHost(String remotePublicHost) {
		this.remotePublicHost = remotePublicHost;
	}

	public int getRemoteAudioPort() {
		return remoteAudioPort;
	}

	public void setRemoteAudioPort(int remoteAudioPort) {
		this.remoteAudioPort = remoteAudioPort;
	}

	public int getRemoteVideoPort() {
		return remoteVideoPort;
	}

	public void setRemoteVideoPort(int remoteVideoPort) {
		this.remoteVideoPort = remoteVideoPort;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RTP configuration :\n");
		sb.append("localPrivateHost = " + localPrivateHost + "\n");
		sb.append("localPublicHost = " + localPublicHost + "\n");
		sb.append("localAudioPort = " + localAudioPort + "\n");
		sb.append("localVideoPort = " + localVideoPort + "\n");
		sb.append("remotePrivateHost = " + remotePrivateHost + "\n");
		sb.append("remotePublicHost = " + remotePublicHost + "\n");
		sb.append("remoteAudioPort = " + remoteAudioPort + "\n");
		sb.append("remoteVideoPort = " + remoteVideoPort + "\n");		
		return sb.toString();
	}
}
