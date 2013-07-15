package com.iolma.studio.call.agent;

public class AVProfile {
	
	private int payloadType = 0; 
	private String encodingName = null; 
	private int clockRate = 0;

	public AVProfile(int payloadType, String encodingName, int clockRate) {
		this.payloadType = payloadType;
		this.encodingName = encodingName;
		this.clockRate = clockRate;		
	}

	public int getPayloadType() {
		return payloadType;
	}

	public String getEncodingName() {
		return encodingName;
	}

	public int getClockRate() {
		return clockRate;
	}
		
}
