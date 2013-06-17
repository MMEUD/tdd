package com.moodmedia.storeportal.zimbra.connection;

/**
 * @author ancuta
 *
 */
//to be updated to populate fields from properties file!

public class Request {

	int urlType = 2;

	String username = "frfsamb";
	String password = "admin";
	String email = "frfsamb@lost.moodmedia.ro";
	String host = "http://lost.moodmedia.ro:6080/home/";
	String inboxLink = "/inbox.xml";
	String emailLink = "/inbox.xml";
	
	public int getUrlType() {
		return urlType;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getInboxLink() {
		return inboxLink;
	}
	
	public String getEmailLink() {
		return emailLink;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public void setInboxLink(String inboxLink) {
		this.inboxLink = inboxLink;
	}
	
	public void setEmailLink(String emailLink) {
		this.emailLink = emailLink;
	}

}
