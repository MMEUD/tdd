package com.iolma.studio.network.sip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;

import com.iolma.studio.log.ILogger;

public class NetworkDetector {

	private ILogger logger = null;
	private HashSet<InetAddress> interfaces = new HashSet<InetAddress>();
	private long lastDetection = 0;
	
	public NetworkDetector(ILogger logger) {
		this.logger = logger;
	}
	
	public HashSet<InetAddress> getInterfaces() {
		if (System.currentTimeMillis() - lastDetection > 5000) {
			
			logger.info("Detecting network interfaces...");
	        try {
	            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	            while(nets.hasMoreElements()) {
	                NetworkInterface netInt = nets.nextElement();
	                if (netInt.isUp() && !netInt.isVirtual()) {
		                Enumeration<InetAddress> inetAdresses = netInt.getInetAddresses();
		                while (inetAdresses.hasMoreElements()) {                	
		                    InetAddress inetAddress = inetAdresses.nextElement();
		                    if (inetAddress.isSiteLocalAddress() && inetAddress instanceof Inet4Address) {
		                    	logger.info("Found interface : " + inetAddress.getHostAddress());
		                    	interfaces.add(inetAddress);
		                    }
/*
	                    	if (inetAddress.isSiteLocalAddress()) {
		                    	Socket socket = null;
								try {
									socket = new Socket();
									socket.setSoTimeout(2000);
									socket.bind(new InetSocketAddress(inetAddress, 8080));
									socket.connect(new InetSocketAddress("google.com", 80));
								} catch (IOException ex) {
									ex.printStackTrace();
									continue;
								} finally {
									try {
										if (socket != null) {
											socket.close();
										}
									} catch (IOException e) {
									}
								}
		                    }
*/		                    
		                }
	                }
	            }
	        } catch (SocketException e) {
	        	logger.error("SocketException : " + e.getMessage());
			}
	        lastDetection = System.currentTimeMillis();
		}
		return interfaces;
	}
}
