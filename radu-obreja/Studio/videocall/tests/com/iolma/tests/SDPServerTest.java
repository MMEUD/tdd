package com.iolma.tests;

import java.io.IOException;
import java.net.BindException;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.network.sip.AVProfile;

public class SDPServerTest {

	public static void main(String[] args) {
		ILogger logger = new ConsoleLogger();
		
		Configuration configuration = new Configuration();
		configuration.setDesign("testcall");
		configuration.setFromUser("radu");
		configuration.setFromPassword("radu");
		configuration.setFromName("Radu Obreja");
		configuration.setToUser("andrei");
		configuration.setToName("Andrei Filip");
		configuration.setSipPort(5060);
		configuration.setRtpPort(6060);

		Configuration configuration2 = new Configuration();
		configuration2.setDesign("testcall");
		configuration2.setFromUser("radu");
		configuration2.setFromPassword("radu");
		configuration2.setFromName("Radu Obreja");
		configuration2.setToUser("andrei");
		configuration2.setToName("Andrei Filip");
		configuration2.setSipPort(5070);
		configuration2.setRtpPort(6070);
		
		try {

			SDPServer sdpServer = new SDPServer(configuration, logger);
			SDPServer sdpServer2 = new SDPServer(configuration2, logger);

			sdpServer.addAudioProfile(new AVProfile(9,"G722", 8000));
			sdpServer.addAudioProfile(new AVProfile(101,"telephone-event", 8000));
			sdpServer.addVideoProfile(new AVProfile(99,"MP4V-ES", 90000));
			
			sdpServer2.addAudioProfile(new AVProfile(9,"G722", 8000));
			sdpServer2.addAudioProfile(new AVProfile(101,"telephone-event", 8000));
			sdpServer2.addVideoProfile(new AVProfile(99,"MP4V-ES", 90000));
			
			sdpServer.startup();
			sdpServer2.startup();
			
			for (int i=0; i < 10; i++) {

				String sdp = sdpServer.getLocalSDP();
				String sdp2 = sdpServer2.getLocalSDP();
				System.out.println(sdp);
				System.out.println("------------------------------------ " + i + " -----------------------------------------");
				System.out.println(sdp2);
				
				sdpServer.setControlling(true);
				sdpServer.startConnectivityEstablishment(sdp2);
				sdpServer2.startConnectivityEstablishment(sdp);

				System.out.println(sdpServer.getAudioRTPConfig());
				System.out.println(sdpServer.getVideoRTPConfig());
	            
			}

		} catch (BindException e) {
			logger.error("BindException : " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException : " + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException : " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("InterruptedException : " + e.getMessage());
		}
		
	}

}
