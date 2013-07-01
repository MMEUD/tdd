package com.iolma.registrar;

import javax.servlet.sip.Address;
import javax.servlet.sip.URI;

public class Binding {

	private URI contact;
	private int expires;
	private long expiresMillis;
	private String callID;
	private int cseq;

	public Binding(Address contact, String callID, int cseq, int expires) {
		setContact(contact.getURI());
		update(callID, cseq, expires);
	}

	public URI getContact() {
		return contact;
	}

	public void setContact(URI contact) {
		this.contact = contact;
	}

	public String getCallID() {
		return callID;
	}

	public void setCallID(String callID) {
		this.callID = callID;
	}

	public int getSeq() {
		return cseq;
	}

	public void setSeq(int cseq) {
		this.cseq = cseq;
	}

	public void setExpires(int expires) {
		this.expires = expires;
		expiresMillis = System.currentTimeMillis() + expires * 1000L;
	}

	public int getExpires() {
		return expires;
	}

	public boolean isExpired() {
		return (System.currentTimeMillis() - expiresMillis) > 0 ? true : false;
	}

	public void update(String callID, int cseq, int expires) {
		setCallID(callID);
		setSeq(cseq);
		setExpires(expires);
	}

	public String toString() {
		return contact.toString();
	}

	public int hashCode() {
		return contact.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Binding) {
			Binding right = (Binding) o;
			if (!contact.equals(right.contact)) {
				return false;
			}
			if (!callID.equals(right.callID)) {
				return false;
			}
			if (cseq != right.cseq) {
				return false;
			}
			return true;
		}
		return false;
	}
}
