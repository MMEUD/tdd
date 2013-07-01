package com.iolma.tests;

import java.io.IOException;
import java.net.BindException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.iolma.studio.log.ILogger;
import com.iolma.studio.network.sip.AVProfile;
import com.iolma.studio.network.sip.CandidateAttribute;
import com.iolma.studio.network.sip.NetworkDetector;
import com.iolma.studio.network.sip.RTPConfig;
import com.iolma.studio.process.IServer;

public class SDPServer extends Thread implements IServer {

	private Configuration configuration = null;
	private InetAddress inetAddress = null;
	private ILogger logger = null;

	private SdpFactory sdpFactory = SdpFactory.getInstance();
	private Agent agent = null;
	private SessionDescription sessionDescription = null;
	private String localSDP = null;

	private ArrayList<AVProfile> audioProfiles = new ArrayList<AVProfile>();
	private ArrayList<AVProfile> videoProfiles = new ArrayList<AVProfile>();
	private ConcurrentHashMap<String, RTPConfig> rtpConfigs = new ConcurrentHashMap<String, RTPConfig>();
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	
	public void startup() {
		start();
		logger.info("SDPServer started.");		
	}

	public void shutdown() {
		if (isAlive()) {
			interrupt();
		}		
	}

	public void run() {
		while (true) {
			try {
				logger.info("RUN configureInetAddr...");
				configureInetAddr();
				logger.info("RUN configureIceAgent...");
				configureIceAgent();
				logger.info("RUN createSessionDescriptionS...");
		        createSessionDescription();
				logger.info("RUN take...");
				String sdp = queue.take();
	            localSDP = null;
				try {
					logger.info("RUN parseSDP...");
					SdpUtils.parseSDP(agent, sdp); // de inlocuit sa nu mai dea exception
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info("RUN startConnectivityEstablishment...");
				while (agent.getState() != IceProcessingState.WAITING) {
					Thread.sleep(100);
				}					
				agent.startConnectivityEstablishment();
				while (agent.getState() != IceProcessingState.TERMINATED) {
					Thread.sleep(100);
				}					
				logger.info("READING STREAMS...");
				List<IceMediaStream> streams = agent.getStreams();
	            for(IceMediaStream stream : streams) {
	            	RTPConfig config = new RTPConfig();
	            	//config.setStreamType(stream.getName());
	                List<Component> components = stream.getComponents();
	                for(Component cmp : components) {
	                	//config.setLocalRtpPort(cmp.getDefaultCandidate().getSocket().getLocalPort());
	                    String cmpName = cmp.getName();
	                    System.out.println(cmpName + ": " + cmp.getSelectedPair());
	                }
	                rtpConfigs.put(stream.getName(), config);
	            }
				logger.info("FREE AGENT...");
				agent.free();
			} catch (BindException e) {
				e.printStackTrace();
				logger.error("BindException : " + e.getMessage());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				logger.error("IllegalArgumentException : " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("IOException : " + e.getMessage());
			} catch (SdpException e) {
				e.printStackTrace();
				logger.error("SdpException : " + e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			logger.info("LOOPING...");
		}
		logger.info("SDPServer killed.");
	}

	public SDPServer(Configuration configuration, ILogger logger) throws BindException, IllegalArgumentException, IOException {
		this.configuration = configuration;
		this.logger = logger;		
	}
	
	public void addAudioProfile(AVProfile profile) {
		audioProfiles.add(profile);
	}
	
	public void addVideoProfile(AVProfile profile) {
		videoProfiles.add(profile);
	}

	private void configureInetAddr() {
		NetworkDetector network = new NetworkDetector(logger);
		HashSet<InetAddress> interfaces = network.getInterfaces();
    	Iterator<InetAddress> it = interfaces.iterator();
    	if (it.hasNext()) {
    		inetAddress = it.next();
    	}		
	}
	
	private void configureIceAgent() throws BindException, IllegalArgumentException, IOException {
		agent = new Agent();
        agent.setControlling(false);

        StunCandidateHarvester stunHarv = new StunCandidateHarvester(new TransportAddress("sip-communicator.net", 3478, Transport.UDP));
        agent.addCandidateHarvester(stunHarv);

        IceMediaStream audioStream = agent.createMediaStream(AVProfile.AUDIO);        	
		agent.createComponent(audioStream, Transport.UDP, configuration.getRtpPort(), configuration.getRtpPort(), configuration.getRtpPort() + 100);
        agent.createComponent(audioStream, Transport.UDP, configuration.getRtpPort() + 1, configuration.getRtpPort() + 1, configuration.getRtpPort() + 101);       

        IceMediaStream videoStream = agent.createMediaStream(AVProfile.VIDEO);
		agent.createComponent(videoStream, Transport.UDP, configuration.getRtpPort() + 2, configuration.getRtpPort() + 2, configuration.getRtpPort() + 102);
        agent.createComponent(videoStream, Transport.UDP, configuration.getRtpPort() + 3, configuration.getRtpPort() + 3, configuration.getRtpPort() + 103);
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
		String addrType = inetAddress instanceof Inet6Address ? Connection.IP6 : Connection.IP4;
		Origin o = sdpFactory.createOrigin(configuration.getFromUser(), 0, 0, "IN", addrType, inetAddress.getHostAddress());
		sessionDescription.setOrigin(o);

		// c=
		Connection c = sdpFactory.createConnection("IN", addrType, inetAddress.getHostAddress());
		sessionDescription.setConnection(c);

        Vector<Attribute> sessionAttributes = new Vector<Attribute>();
        sessionAttributes.add(sdpFactory.createAttribute("ice-pwd", agent.getLocalPassword()));
        sessionAttributes.add(sdpFactory.createAttribute("ice-ufrag", agent.getLocalUfrag()));        
		
        List<IceMediaStream> streams = agent.getStreams();
        Vector<MediaDescription> mediaDescriptions = new Vector<MediaDescription>(agent.getStreamCount());
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
        	   mediaDescriptionAttributes.add(sdpFactory.createAttribute("fmtp", "101 0-11"));
           }
           if (AVProfile.VIDEO.equals(stream.getName())) {
               formats = new int[videoProfiles.size()];
        	   for (int i=0; i < videoProfiles.size(); i++) {
	        	   AVProfile profile = videoProfiles.get(i);
        		   mediaDescriptionAttributes.add(sdpFactory.createAttribute("rtpmap", profile.getPayloadType() + " " + profile.getEncodingName() + "/" + profile.getClockRate()));
        	   }
           }
           
           MediaDescription mediaDescription = sdpFactory.createMediaDescription(stream.getName(), streamDefaultAddr.getPort(), 1, SdpConstants.RTP_AVP, formats);
           for(Component component : stream.getComponents()) {
               for(Candidate cand : component.getLocalCandidates()) {
            	   if (cand.getHostAddress().getHostAddress().equals(inetAddress.getHostAddress())) {
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
	
	public String getLocalSDP() throws InterruptedException {
		while (localSDP == null) {
			Thread.sleep(100);
		}					
		return localSDP;
	}

	public void setControlling(boolean controlling) {
		agent.setControlling(controlling);
	}
	
	public void startConnectivityEstablishment(String remoteSDP) throws InterruptedException {
		rtpConfigs.clear();
		queue.put(remoteSDP);
	}
	
	public RTPConfig getAudioRTPConfig() throws InterruptedException {
		while(!rtpConfigs.containsKey(AVProfile.AUDIO)) {
			Thread.sleep(100);
		}
		return rtpConfigs.get(AVProfile.AUDIO); 
	}
	
	public RTPConfig getVideoRTPConfig() throws InterruptedException {
		while(!rtpConfigs.containsKey(AVProfile.AUDIO)) {
			Thread.sleep(100);
		}
		return rtpConfigs.get(AVProfile.VIDEO); 
	}
	
}
