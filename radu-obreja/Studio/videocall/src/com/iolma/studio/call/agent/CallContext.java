package com.iolma.studio.call.agent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransportAlreadySupportedException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.call.ICallListener;
import com.iolma.studio.call.RTPConfig;
import com.iolma.studio.log.ILogger;

public class CallContext implements SipListener {
		
	protected final static String SIP_TRANSPORT = "udp";
	
	private ICallAgentConfig config = null;
	private INetworkDescovery networkDiscovery = null;
	private IPortGenerator portGenerator = null;
	private ILogger logger = null;

	private SipStack sipStack = null;
	private ListeningPoint listeningPoint = null;
	private SipProvider sipProvider = null;
	private AddressFactory addressFactory = null;
	private MessageFactory messageFactory = null;
	private HeaderFactory headerFactory = null;

	private CallListenersNotificator listenersNotificator = null;
	private RequestDispatcher requestDispatcher = null;
	private ResponseDispatcher responseDispatcher = null;

	private ArrayList<AVProfile> audioProfiles = new ArrayList<AVProfile>();
	private ArrayList<AVProfile> videoProfiles = new ArrayList<AVProfile>();
	private RTPConfig rtpConfig = new RTPConfig();	

	
	
	private ClientTransaction userTransaction = null;
	private AtomicInteger userState = new AtomicInteger(ICallAgent.USERSTATE_UNREGISTERED);

	private Request callInviteRequest = null;
	private ClientTransaction callClientTransaction = null;
	private ServerTransaction callServerTransaction = null;
	private AtomicInteger callState = new AtomicInteger(ICallAgent.CALLSTATE_IDLE);

	private Address remoteAddress = null;
	private Address remotePublicAddress = null;
	private AtomicLong numSeq = new AtomicLong(1);
	
	private String localHost = null; 
	private String publicHost = null; 
	private int publicPort = 0;

	
	public CallContext(ICallAgentConfig config, INetworkDescovery networkDiscovery, IPortGenerator portGenerator, ILogger logger) {
		this.config = config;
		this.networkDiscovery = networkDiscovery;
		this.portGenerator = portGenerator;
		this.logger = logger;
		this.listenersNotificator = new CallListenersNotificator(logger);
		this.requestDispatcher = new RequestDispatcher(config, this, logger);
		this.responseDispatcher = new ResponseDispatcher(config, this, logger);
	}
	
	protected void startSipStack() {
		localHost = networkDiscovery.getLocalHost();
		
		SipFactory sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		
		Properties properties = new Properties();
		// set stack name
		properties.setProperty("javax.sip.STACK_NAME", "iolma");
		// set proxy
		properties.setProperty("javax.sip.OUTBOUND_PROXY", config.getSipProxyHost() + ":" + config.getSipProxyPort() + "/"+ SIP_TRANSPORT);
		// guard against denial of service attack if transport = tcp.
		properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
		// Drop the client connection after we are done with the transaction.
		properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
		// 0 = production, 16 = logging traces, 32 = debug + traces.
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");

		try {
			
			sipStack = sipFactory.createSipStack(properties);
            
			listeningPoint = sipStack.createListeningPoint(localHost, config.getSipLocalPort(), SIP_TRANSPORT);
            logger.info("Create ListeningPoint " + localHost + " [" + SIP_TRANSPORT + "]");
            
        	sipProvider = sipStack.createSipProvider(listeningPoint);	 
        	sipProvider.addSipListener(this);
	        
	        headerFactory = sipFactory.createHeaderFactory();
	        addressFactory = sipFactory.createAddressFactory();
	        messageFactory = sipFactory.createMessageFactory();
	        
			sipStack.start();
			
		} catch (PeerUnavailableException e) {
			logger.error("PeerUnavailableException : " + e.getMessage());
		} catch (TransportNotSupportedException e) {
			logger.error("TransportNotSupportedException : " + e.getMessage());
		} catch (InvalidArgumentException e) {
			logger.error("InvalidArgumentException : " + e.getMessage());
		} catch (ObjectInUseException e) {
			logger.error("ObjectInUseException : " + e.getMessage());
		} catch (TooManyListenersException e) {
			logger.error("TooManyListenersException : " + e.getMessage());
		} catch (TransportAlreadySupportedException e) {
			logger.error("TransportAlreadySupportedException : " + e.getMessage());
		} catch (SipException e) {
			logger.error("SipException : " + e.getMessage());
		}
		
	}

	protected void stopSipStack() {
		if (sipStack != null) {
			sipStack.stop();
		}
	}

	protected void setPublicAddress(String publicHost, int publicPort) {
		try {
			this.publicHost = publicHost;
			this.publicPort = publicPort;
			listeningPoint.setSentBy(publicHost + ":" + publicPort);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	protected ArrayList<AVProfile> getAudioProfiles() {
		return audioProfiles;
	}

	protected ArrayList<AVProfile> getVideoProfiles() {
		return videoProfiles;
	}

	protected void addAudioProfile(AVProfile profile) {
		audioProfiles.add(profile);
	}
	
	protected void addVideoProfile(AVProfile profile) {
		videoProfiles.add(profile);
	}

	protected void addListener(String key, ICallListener listener) {
		listenersNotificator.addListener(key, listener);
	}

	protected void removeListener(String key) {
		listenersNotificator.removeListener(key);
	}

	protected CallListenersNotificator getListenersNotificator() {
		return listenersNotificator;
	}

	protected SipProvider getSipProvider() {
		return sipProvider;
	}
	
	protected AddressFactory getAddressFactory() {
		return addressFactory;
	}
	
	protected MessageFactory getMessageFactory() {
		return messageFactory;
	}
	
	protected HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	protected IPortGenerator getPortGenerator() {
		return portGenerator;
	}

	protected RTPConfig getRtpConfig() {
		return rtpConfig;
	}

	protected String getLocalHost() {
		return localHost;
	}

	protected String getPublicHost() {
		if (publicHost != null) {
			return publicHost;
		} else {
			return listeningPoint.getIPAddress();
		}
	}
	
	protected int getPublicPort() {
		if (publicPort != 0) {
			return publicPort;
		} else {
			return listeningPoint.getPort();
		}
	}
	
	protected ClientTransaction getUserTransaction() {
		return userTransaction;
	}

	protected void setUserTransaction(ClientTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	protected int getUserState() {
		return userState.get();
	}

	protected void setUserState(int userState) {
		this.userState.set(userState);
	}

	protected Request getCallInviteRequest() {
		return callInviteRequest;
	}

	protected void setCallInviteRequest(Request callInviteRequest) {
		this.callInviteRequest = callInviteRequest;
	}

	protected ClientTransaction getCallClientTransaction() {
		return callClientTransaction;
	}

	protected void setCallClientTransaction(ClientTransaction callClientTransaction) {
		this.callClientTransaction = callClientTransaction;
	}

	protected ServerTransaction getCallServerTransaction() {
		return callServerTransaction;
	}

	protected void setCallServerTransaction(ServerTransaction callServerTransaction) {
		this.callServerTransaction = callServerTransaction;
	}

	protected Dialog getCallDialog() {
		if(callClientTransaction != null) {
			return callClientTransaction.getDialog();
		}
		if(callServerTransaction != null) {
			return callServerTransaction.getDialog();
		}
		return null;
	}
	
	protected int getCallState() {
		return callState.get();
	}
	
	protected void setCallState(int callState) {
		this.callState.set(callState);
		if (callState == ICallAgent.CALLSTATE_IDLE) {
			callInviteRequest = null;
			callClientTransaction = null;
			callServerTransaction = null;
			remoteAddress = null;
			remotePublicAddress = null;
		}
	}
	
	protected Address getRemotePublicAddress() {
		return remotePublicAddress;
	}

	protected void setRemotePublicAddress(Address remotePublicAddress) {
		this.remotePublicAddress = remotePublicAddress;
	}

	protected Address getRemoteAddress() {
		return remoteAddress;
	}

	protected void setRemoteAddress(Address remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	protected long getNextNumSeq() {
		return numSeq.incrementAndGet();
	}

	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		if (dialogTerminatedEvent.getDialog().getDialogId() == getCallDialog().getDialogId()) {
			setCallState(ICallAgent.CALLSTATE_IDLE);
			getListenersNotificator().fireCallTerminated();
		}
	}

	public void processIOException(IOExceptionEvent ioExceptionEvent) {
	}

	public void processRequest(RequestEvent requestEvent) {
		requestDispatcher.process(requestEvent);
	}

	public void processResponse(ResponseEvent responseEvent) {
		responseDispatcher.process(responseEvent);
	}

	public void processTimeout(TimeoutEvent timeoutEvent) {
	}

	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
	}

}
