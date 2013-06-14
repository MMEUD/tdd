package com.iolma.studio.application;

public class Configuration {

	private String design = "";
	private String fromUser = "";
	private String fromMd5Password = "";
	private String fromName = "";
	private String toUser = "";
	private String toName = "";

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
	
	public String getFromMd5Password() {
		return fromMd5Password;
	}
	
	public void setFromMd5Password(String fromMd5Password) {
		this.fromMd5Password = fromMd5Password;
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

}
