package com.moodmedia.storeportal.zimbra.connection;

/**
 * @author Ancuta Gheorghe
 *
 */
//to be updated to populate fields from properties file!

public class CustomRequest {

	int type = 1;

	String username = "ancuta";
	String password = "k4hvdrq2d3";
	String email = "ancuta@lost.moodmedia.ro";
	String host = "http://lost.moodmedia.ro:6080/home/";
	String inboxLink = "/inbox.xml";
	String emailLink = "/inbox.xml";
	String emailDownload = "..\\..\\..\\..\\datamail\\tempzip\\";
	String idMail = "";
	
	public int getType() {
		return type;
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
	
	public String getIdMail() {
		return idMail;
	}

	public void setType(int type) {
		this.type = type;
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

	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}
}
