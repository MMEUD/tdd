package com.iolma.studio.network.sip;

import java.io.IOException;
import java.net.BindException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sdp.Attribute;
import javax.sdp.Connection;
import javax.sdp.MediaDescription;
import javax.sdp.Origin;
import javax.sdp.SdpConstants;
import javax.sdp.SdpException;
import javax.sdp.SdpFactory;
import javax.sdp.SessionDescription;
import javax.sdp.TimeDescription;
import javax.sdp.Version;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransportAlreadySupportedException;
import javax.sip.TransportNotSupportedException;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.Candidate;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import test.SdpUtils;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class UserAgent extends Thread implements IServer, IUserAgent, SipListener {

	private Configuration configuration = null;
	private ILogger logger = null;

	private ConcurrentHashMap<String, IUserAgentListener> listeners = new ConcurrentHashMap<String, IUserAgentListener>();
	
	private HashSet<InetAddress> interfaces = null;
	private SipStack sipStack = null;
	private ListeningPoint listeningPoint = null;
	
	private SipContext sipContext = new SipContext();
	private RequestDispatcher requestDispatcher = null;
	private ResponseDispatcher responseDispatcher = null;
	
	private RegisterActions registerActions = null;
	private CallActions callActions = null;
	
	private SdpFactory sdpFactory = SdpFactory.getInstance();
	private Agent iceAgent = null;
	private SessionDescription sessionDescription = null;
	private String localSDP = null;
	private InetAddress localAddress = null;
	private String publicHost = null;
	private int publicPort = 0;

	private ArrayList<AVProfile> audioProfiles = new ArrayList<AVProfile>();
	private ArrayList<AVProfile> videoProfiles = new ArrayList<AVProfile>();
	private LinkedBlockingQueue<String> sdpQueue = new LinkedBlockingQueue<String>();

	
	public UserAgent(Configuration configuration, ILogger logger) {
		this.configuration = configuration;
		this.logger = logger;
		requestDispatcher = new RequestDispatcher(configuration, sipContext, logger);
		responseDispatcher = new ResponseDispatcher(configuration, sipContext, logger);
		registerActions = new RegisterActions(configuration, sipContext, logger);
		callActions = new CallActions(configuration, sipContext, logger);
		sipContext.setAgent(this);
	}
		
	public String getPublicHost() {
		return publicHost;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public void setPublicAddress(String publicHost, int publicPort) {
		this.publicHost = publicHost;
		this.publicPort = publicPort;
		logger.info("Change all requests to public settings : " + publicHost + ":" + publicPort);
		try {
			listeningPoint.setSentBy(publicHost + ":" + publicPort);
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		}
	}

	public void addAudioProfile(AVProfile profile) {
		audioProfiles.add(profile);
	}
	
	public void addVideoProfile(AVProfile profile) {
		videoProfiles.add(profile);
	}

	public LinkedBlockingQueue<String> getSdpQueue() {
		return sdpQueue;
	}

	public String getLocalSDP() throws InterruptedException {
		while (localSDP == null) {
			Thread.sleep(100);
		}					
		return localSDP;
	}

	public boolean isRegistered() {
		return sipContext.isRegistered();
	}
	
	public boolean isRinging() {
		return sipContext.isRinging();
	}
	
	public boolean isConnected() {
		return sipContext.isConnected();
	}
	
	public void setControlling(boolean controlling) {
		iceAgent.setControlling(controlling);
	}
	
	public void startConnectivityEstablishment(String remoteSDP) throws InterruptedException {
		sdpQueue.put(remoteSDP);
	}

	public void startup() {
		NetworkDetector network = new NetworkDetector(logger);
		interfaces = network.getInterfaces();
    	Iterator<InetAddress> it = interfaces.iterator();
    	if (it.hasNext()) {
    		localAddress = it.next();
    	}
		
		SipFactory sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		
		Properties properties = new Properties();

		// set stack name
		properties.setProperty("javax.sip.STACK_NAME", configuration.getFromUser());
		// set proxy
		properties.setProperty("javax.sip.OUTBOUND_PROXY", configuration.getSipProxy() + ":" + configuration.getSipProxyPort() + "/"+ configuration.getSipTransport());
		// guard against denial of service attack if transport = tcp.
		properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
		// Drop the client connection after we are done with the transaction.
		properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
		// 0 = production, 16 = logging traces, 32 = debug + traces.
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");

		try {
			sipStack = sipFactory.createSipStack(properties);
            listeningPoint = sipStack.createListeningPoint(localAddress.getHostAddress(), configuration.getSipPort(), configuration.getSipTransport());
            logger.info("Create ListeningPoint " + localAddress.getHostAddress() + " [" + configuration.getSipTransport() + "]");
        	sipContext.setSipProvider(sipStack.createSipProvider(listeningPoint));	 
        	sipContext.getSipProvider().addSipListener(this);
	        
	        sipContext.setHeaderFactory(sipFactory.createHeaderFactory());
	        sipContext.setAddressFactory(sipFactory.createAddressFactory());
	        sipContext.setMessageFactory(sipFactory.createMessageFactory());
	        
			sipStack.start();
			start();
			logger.info("UserAgent started...");
			
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

	public void shutdown() {
		sipStack.stop();
		if (isAlive()) {
			interrupt();
		}		
	}

	public void run() {
		while (true) {
			try {				
				configureIceAgent();
		        createSessionDescription();
				String sdp = sdpQueue.take();
				logger.info("--------------------------------------------------------------");
				logger.info(sdp);
				logger.info("--------------------------------------------------------------");
				
	            localSDP = null;
				try {
					SdpUtils.parseSDP(iceAgent, sdp); // de inlocuit sa nu mai dea exception
				} catch (Exception e) {
					e.printStackTrace();
				}
				while (iceAgent.getState() != IceProcessingState.WAITING) {
					Thread.sleep(100);
				}					
				iceAgent.startConnectivityEstablishment();
				while (iceAgent.getState() != IceProcessingState.TERMINATED && iceAgent.getState() != IceProcessingState.FAILED) {
					Thread.sleep(100);
				}					
                logger.info("Pairs found : ");
				List<IceMediaStream> streams = iceAgent.getStreams();
	            for(IceMediaStream stream : streams) {
	            	RTPConfig config = new RTPConfig();
	            	config.setStreamType(stream.getName());
	                logger.info("Stream " + stream.getName() + " : ");
	                List<Component> components = stream.getComponents();
	                for(Component cmp : components) {
	                    String cmpName = cmp.getName();
	                    logger.info(cmpName + ": " + cmp.getSelectedPair());
	                }
	            }
		        // START RTP
		        sipContext.setConnected(true);
		        sipContext.setRinging(false);
		        fireCallAnswered();
				iceAgent.free();
			} catch (BindException e) {
				logger.error("BindException : " + e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error("IllegalArgumentException : " + e.getMessage());
			} catch (IOException e) {
				logger.error("IOException : " + e.getMessage());
			} catch (SdpException e) {
				logger.error("SdpException : " + e.getMessage());
			} catch (InterruptedException e) {
				break;
			}
			logger.info("LOOPING...");
		}
		logger.info("SDPServer killed.");
	}
	
	
	private void configureIceAgent() throws BindException, IllegalArgumentException, IOException {
		iceAgent = new Agent();
		iceAgent.setControlling(false);

        StunCandidateHarvester stunHarv = new StunCandidateHarvester(new TransportAddress("sip-communicator.net", 3478, Transport.UDP));
        iceAgent.addCandidateHarvester(stunHarv);

        IceMediaStream audioStream = iceAgent.createMediaStream(AVProfile.AUDIO);        	
        iceAgent.createComponent(audioStream, Transport.UDP, configuration.getRtpPort(), configuration.getRtpPort(), configuration.getRtpPort() + 100);
		iceAgent.createComponent(audioStream, Transport.UDP, configuration.getRtpPort() + 1, configuration.getRtpPort() + 1, configuration.getRtpPort() + 101);       

        IceMediaStream videoStream = iceAgent.createMediaStream(AVProfile.VIDEO);
        iceAgent.createComponent(videoStream, Transport.UDP, configuration.getRtpPort() + 2, configuration.getRtpPort() + 2, configuration.getRtpPort() + 102);
        iceAgent.createComponent(videoStream, Transport.UDP, configuration.getRtpPort() + 3, configuration.getRtpPort() + 3, configuration.getRtpPort() + 103);
	}

	@SuppressWarnings("rawtypes")
	private void createSessionDescription() throws SdpException {
		
		sessionDescription = sdpFactory.createSessionDescription();

		// "v=0"
		Version v = sdpFactory.createVersion(0);
		sessionDescription.setVersion(v);

		// "s=-"
		sessionDescription.setSessionName(sdpFactory.createSessionName(configuration.getFromUser()));

		// "t=0 0"
		TimeDescription t = sdpFactory.createTimeDescription();
		Vector<TimeDescription> timeDescs = new Vector<TimeDescription>();
		timeDescs.add(t);
		sessionDescription.setTimeDescriptions(timeDescs);

		// o
		String addrType = localAddress instanceof Inet6Address ? Connection.IP6 : Connection.IP4;
		Origin o = sdpFactory.createOrigin(configuration.getFromUser(), 0, 0, "IN", addrType, localAddress.getHostAddress());
		sessionDescription.setOrigin(o);

		// c=
		Connection c = sdpFactory.createConnection("IN", addrType, localAddress.getHostAddress());
		sessionDescription.setConnection(c);

        Vector<Attribute> sessionAttributes = new Vector<Attribute>();
        sessionAttributes.add(sdpFactory.createAttribute("ice-pwd", iceAgent.getLocalPassword()));
        sessionAttributes.add(sdpFactory.createAttribute("ice-ufrag", iceAgent.getLocalUfrag()));        
		
        List<IceMediaStream> streams = iceAgent.getStreams();
        Vector<MediaDescription> mediaDescriptions = new Vector<MediaDescription>(iceAgent.getStreamCount());
        for(IceMediaStream stream : streams) {
           TransportAddress streamDefaultAddr = stream.getComponent(Component.RTP).getDefaultCandidate().getTransportAddress();

           Vector<Attribute> mediaDescriptionAttributes = new Vector<Attribute>();
           int[] formats = new int[]{0};
           if (AVProfile.AUDIO.equals(stream.getName())) {
               formats = new int[audioProfiles.size()];
        	   for (int i=0; i < audioProfiles.size(); i++) {
	        	   AVProfile profile = audioProfiles.get(i);
	        	   formats[i] = profile.getPayloadType();
        		   mediaDescriptionAttributes.add(sdpFactory.createAttribute("rtpmap", profile.getPayloadType() + " " + profile.getEncodingName() + "/" + profile.getClockRate()));
        	   }
        	   mediaDescriptionAttributes.add(sdpFactory.createAttribute("fmtp", "101 0-16"));
        	   mediaDescriptionAttributes.add(sdpFactory.createAttribute("ptime", "20"));
        	   mediaDescriptionAttributes.add(sdpFactory.createAttribute("sendrecv", null));
           }
           if (AVProfile.VIDEO.equals(stream.getName())) {
               formats = new int[videoProfiles.size()];
        	   for (int i=0; i < videoProfiles.size(); i++) {
	        	   AVProfile profile = videoProfiles.get(i);
        		   mediaDescriptionAttributes.add(sdpFactory.createAttribute("rtpmap", profile.getPayloadType() + " " + profile.getEncodingName() + "/" + profile.getClockRate()));
        	   }
        	   mediaDescriptionAttributes.add(sdpFactory.createAttribute("sendrecv", null));
           }
           
           MediaDescription mediaDescription = sdpFactory.createMediaDescription(stream.getName(), streamDefaultAddr.getPort(), 1, SdpConstants.RTP_AVP, formats);
           for(Component component : stream.getComponents()) {
               for(Candidate cand : component.getLocalCandidates()) {
            	   if (cand.getHostAddress().getHostAddress().equals(localAddress.getHostAddress())) {
            		   mediaDescriptionAttributes.add(new CandidateAttribute(cand));
            	   }
               }
           }

           mediaDescription.setAttributes(mediaDescriptionAttributes);
           mediaDescriptions.add(mediaDescription);
        }

        sessionDescription.setAttributes(sessionAttributes);
        sessionDescription.setMediaDescriptions(mediaDescriptions);
        localSDP = sessionDescription.toString();
	}

	public void addListener(String key, IUserAgentListener listener) {
		listeners.put(key, listener);
	}

	public void removeListener(String key) {
		listeners.remove(key);
	}

	public void register() {
		registerActions.register();
	}

	public void unregister() {
		registerActions.unregister();
	}

	public void call(String toUser) {
		callActions.call(toUser);
	}

	public void answer() {
		callActions.answer();
	}

	public void terminate() {
		callActions.terminate();
	}
	
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		logger.debug("SipListener -> processDialogTerminated event notification : [dialog ID = " + dialogTerminatedEvent.getDialog().getDialogId() + "]");
	}

	public void processIOException(IOExceptionEvent ioExceptionEvent) {
		logger.debug("SipListener -> processIOException event notification");
	}

	public void processRequest(RequestEvent requestEvent) {
		requestDispatcher.process(requestEvent);
	}

	public void processResponse(ResponseEvent responseEvent) {
		responseDispatcher.process(responseEvent);
	}

	public void processTimeout(TimeoutEvent timeoutEvent) {
		logger.debug("SipListener -> processTimeout event notification" );
	}

	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
		logger.debug("SipListener -> processTransactionTerminated event notification");
	}

	public synchronized void firePhoneRegistered() {
		logger.debug("UserAgent -> firePhoneRegistered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onPhoneRegistered();
		}
	}

	public synchronized void firePhoneUnRegistered() {
		logger.debug("UserAgent -> firePhoneUnRegistered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onPhoneUnRegistered();
		}
	}

	public synchronized void fireRinging(String userId, String userName) {
		logger.debug("UserAgent -> fireRinging : " + userId + "[" + userName + "]");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onRinging(userId, userName);
		}
	}

	public synchronized void fireCalling() {
		logger.debug("UserAgent -> fireCalling");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCalling();
		}
	}
	
	public synchronized void fireCallAnswered() {
		logger.debug("UserAgent -> fireCallAnswered");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCallAnswered();
		}
	}
	
	public synchronized void fireCallTerminated() {
		logger.debug("UserAgent -> fireCallTerminated");
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onCallTerminated();
		}
	}
	
	public synchronized void fireError(String message) {
		logger.debug("UserAgent -> fireError : " + message);
		Iterator<String> it = listeners.keySet().iterator();
		while (it.hasNext()) {
			listeners.get(it.next()).onError(message);
		}
	}
	
	
	public static void main(String[] args) {
		ILogger logger = new ConsoleLogger();
		Configuration config = new Configuration();
		if (args.length == 0 || "radu".equals(args[0])) {
			config.setFromUser("radu");
			config.setFromName("Radu Obreja");
			config.setFromPassword("radu");
			config.setSipPort(5060);
		}
		if (args.length > 0 && "andrei".equals(args[0])) {
			config.setFromUser("andrei");
			config.setFromName("Andrei Filip");
			config.setFromPassword("andrei");
			config.setSipPort(5070);
		}
		UserAgent userAgent = new UserAgent(config, logger);
		userAgent.startup();
		userAgent.register();
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
		}
		userAgent.unregister();
	}

}
