package com.iolma.studio.network.sip;

import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

public class SipContext {

	private UserAgent agent = null;
	private SipProvider sipProvider = null;
	private AddressFactory addressFactory = null;
	private MessageFactory messageFactory = null;
	private HeaderFactory headerFactory = null;
	AtomicLong numSeq = new AtomicLong(1);
	private ClientTransaction registerTransaction = null;
	private Dialog inviteDialog = null;
	private ServerTransaction inviteTransaction = null;
	private Request inviteRequest = null;
	private boolean registered = false;
	private boolean ringing = false;
	private boolean connected = false;
	private String userId = null;

	
	public UserAgent getAgent() {
		return agent;
	}

	public void setAgent(UserAgent agent) {
		this.agent = agent;
	}

	public SipProvider getSipProvider() {
		return sipProvider;
	}
	
	public void setSipProvider(SipProvider sipProvider) {
		this.sipProvider = sipProvider;
	}
	
	public AddressFactory getAddressFactory() {
		return addressFactory;
	}
	
	public void setAddressFactory(AddressFactory addressFactory) {
		this.addressFactory = addressFactory;
	}
	
	public MessageFactory getMessageFactory() {
		return messageFactory;
	}
	
	public void setMessageFactory(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}
	
	public HeaderFactory getHeaderFactory() {
		return headerFactory;
	}
	
	public void setHeaderFactory(HeaderFactory headerFactory) {
		this.headerFactory = headerFactory;
	}

	public long getNextNumSeq() {
		return numSeq.incrementAndGet();
	}

	public ClientTransaction getRegisterTransaction() {
		return registerTransaction;
	}

	public void setRegisterTransaction(ClientTransaction registerTransaction) {
		this.registerTransaction = registerTransaction;
	}

	public Dialog getInviteDialog() {
		return inviteDialog;
	}

	public void setInviteDialog(Dialog inviteDialog) {
		this.inviteDialog = inviteDialog;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isRinging() {
		return ringing;
	}

	public void setRinging(boolean ringing) {
		this.ringing = ringing;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public ServerTransaction getInviteTransaction() {
		return inviteTransaction;
	}

	public void setInviteTransaction(ServerTransaction inviteTransaction) {
		this.inviteTransaction = inviteTransaction;
	}

	public Request getInviteRequest() {
		return inviteRequest;
	}

	public void setInviteRequest(Request inviteRequest) {
		this.inviteRequest = inviteRequest;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
