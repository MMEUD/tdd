package com.iolma.studio.call.agent;

import gov.nist.javax.sdp.parser.SDPAnnounceParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

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
import javax.sip.address.SipURI;

import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;

public class RTPConfigManager {

	private ILogger logger = null; 
	private CallContext callContext = null;
	private SdpFactory sdpFactory = SdpFactory.getInstance();
	private String localUser = null;


	public RTPConfigManager(ICallAgentConfig config, ILogger logger, CallContext callContext) {
		this.logger = logger;
		this.callContext = callContext;
		this.localUser = ((SipURI)config.getLocalAddress().getURI()).getUser();
	}

	public void generateLocalRTPConfig() {
		callContext.getRtpConfig().setLocalPrivateHost(callContext.getLocalHost());
		callContext.getRtpConfig().setLocalPublicHost(callContext.getPublicHost());
		callContext.getRtpConfig().setLocalAudioPort(callContext.getPortGenerator().getPort());
		callContext.getRtpConfig().setLocalVideoPort(callContext.getPortGenerator().getPort());
	}
	
	public String getLocalSDP() {
		SessionDescription sessionDescription;
		try {
			sessionDescription = sdpFactory.createSessionDescription();
			Version v = sdpFactory.createVersion(0);
			sessionDescription.setVersion(v);

			sessionDescription.setSessionName(sdpFactory.createSessionName(localUser));

			TimeDescription t = sdpFactory.createTimeDescription();
			Vector<TimeDescription> timeDescs = new Vector<TimeDescription>();
			timeDescs.add(t);
			sessionDescription.setTimeDescriptions(timeDescs);

			Origin o = sdpFactory.createOrigin(localUser, 0, 0, "IN", Connection.IP4, callContext.getLocalHost());
			sessionDescription.setOrigin(o);

			Connection c = sdpFactory.createConnection("IN", Connection.IP4, callContext.getLocalHost());
			sessionDescription.setConnection(c);

			Vector<Attribute> sessionAttributes = new Vector<Attribute>();
			Vector<MediaDescription> mediaDescriptions = new Vector<MediaDescription>();

			ArrayList<AVProfile> audioProfiles = callContext.getAudioProfiles();
			Vector<Attribute> audioDescriptionAttributes = new Vector<Attribute>();
			int[] formats = new int[audioProfiles.size()];
			for (int i = 0; i < audioProfiles.size(); i++) {
				AVProfile profile = audioProfiles.get(i);
				formats[i] = profile.getPayloadType();
				audioDescriptionAttributes.add(sdpFactory.createAttribute("rtpmap", profile.getPayloadType() + " " + profile.getEncodingName() + "/" + profile.getClockRate()));
			}
			audioDescriptionAttributes.add(sdpFactory.createAttribute("fmtp", "101 0-16"));
			audioDescriptionAttributes.add(sdpFactory.createAttribute("ptime", "20"));
			audioDescriptionAttributes.add(sdpFactory.createAttribute("sendrecv", null));
			MediaDescription audioDescription = sdpFactory.createMediaDescription("audio", callContext.getRtpConfig().getLocalAudioPort(), 1, SdpConstants.RTP_AVP, formats);
			audioDescription.setAttributes(audioDescriptionAttributes);
			mediaDescriptions.add(audioDescription);
			
			ArrayList<AVProfile> videoProfiles = callContext.getVideoProfiles();
			Vector<Attribute> videoDescriptionAttributes = new Vector<Attribute>();
			formats = new int[videoProfiles.size()];
			for (int i = 0; i < videoProfiles.size(); i++) {
				AVProfile profile = videoProfiles.get(i);
				videoDescriptionAttributes.add(sdpFactory.createAttribute("rtpmap", profile.getPayloadType() + " " + profile.getEncodingName() + "/" + profile.getClockRate()));
			}
			videoDescriptionAttributes.add(sdpFactory.createAttribute("sendrecv", null));
			MediaDescription videoDescription = sdpFactory.createMediaDescription("video", callContext.getRtpConfig().getLocalVideoPort(), 1, SdpConstants.RTP_AVP, formats);
			videoDescription.setAttributes(videoDescriptionAttributes);
			mediaDescriptions.add(videoDescription);
			
			sessionDescription.setAttributes(sessionAttributes);
			sessionDescription.setMediaDescriptions(mediaDescriptions);

			return sessionDescription.toString();
		} catch (SdpException e) {
			e.printStackTrace();
			logger.error("SdpException : " + e.getMessage());
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void parseRemoteSDP(String remoteSDP) {

        SDPAnnounceParser parser = new SDPAnnounceParser(remoteSDP);
        try {
			SessionDescription sessionDescription = parser.parse();
			callContext.getRtpConfig().setRemotePrivateHost(sessionDescription.getOrigin().getAddress());
			Vector<MediaDescription> mediaDescriptions = sessionDescription.getMediaDescriptions(true);
			for (MediaDescription mediaDescription : mediaDescriptions) {
				if ("audio".equals(mediaDescription.getMedia().getMediaType())) {
					callContext.getRtpConfig().setRemoteAudioPort(mediaDescription.getMedia().getMediaPort());
				}
				if ("video".equals(mediaDescription.getMedia().getMediaType())) {
					callContext.getRtpConfig().setRemoteVideoPort(mediaDescription.getMedia().getMediaPort());
				}
			}
		} catch (ParseException e) {
			logger.error("ParseException : " + e.getMessage());
		} catch (SdpException e) {
			logger.error("SdpException : " + e.getMessage());
		}
		
		callContext.getRtpConfig().setRemotePublicHost(((SipURI)callContext.getRemotePublicAddress().getURI()).getHost());
	}

}
