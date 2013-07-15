package com.iolma.studio.call.agent.networkdiscovery;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.iolma.studio.call.agent.INetworkDescovery;
import com.iolma.studio.log.ILogger;

public class InterfaceNetworkDiscovery implements INetworkDescovery {

	private ILogger logger = null;
	private ArrayList<InetAddress> interfaces = new ArrayList<InetAddress>();
	
	public InterfaceNetworkDiscovery(ILogger logger) {
		this.logger = logger;
	}

	public String getLocalHost() {
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
	                }
                }
            }
        } catch (SocketException e) {
        	logger.error("SocketException : " + e.getMessage());
		}
        if (interfaces.size() > 0) {
        	return interfaces.get(0).getHostAddress();
        } else {
        	return null;
        }
	}

}
