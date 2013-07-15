package com.iolma.studio.call.agent;

import javax.sip.address.Address;

import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.call.ICallListener;
import com.iolma.studio.call.agent.networkdiscovery.InterfaceNetworkDiscovery;
import com.iolma.studio.call.agent.portgenerator.RandomPortGenerator;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class CallAgent extends Thread implements IServer, ICallAgent {

	private ILogger logger = null;
	private CallActions callActions = null;
	private CallContext callContext = null;
	
	public CallAgent(ICallAgentConfig config, ILogger logger) {
		this.logger = logger;
		
		INetworkDescovery networkDiscovery = new InterfaceNetworkDiscovery(logger);
		IPortGenerator portGenerator = new RandomPortGenerator(10000, 20000);
		
		this.callContext = new CallContext(config, networkDiscovery, portGenerator, logger);
		this.callActions = new CallActions(config, callContext, logger);
	}
	
	public void startup() {
		callContext.startSipStack();
		start();
		logger.info("CallAgent started...");
	}

	public void shutdown() {
		if (isAlive()) {
			interrupt();
		}				
	}

	public void run() {
		while (true) {
			try {

				callActions.register();

				if (callContext.getCallState() == ICallAgent.CALLSTATE_TALK) {
					callActions.updateCall();
				}
				
				Thread.sleep(10000); // 10 sec.
			} catch (InterruptedException e) {
				break;
			}
		}
		callActions.unregister();
		callContext.stopSipStack();
		logger.info("CallAgent killed.");
	}
		
	public int getCallState() {
		return callContext.getCallState();
	}

	public int getUserState() {
		return callContext.getUserState();
	}
	
	public void addAudioProfile(AVProfile profile) {
		callContext.addAudioProfile(profile);
	}
	
	public void addVideoProfile(AVProfile profile) {
		callContext.addVideoProfile(profile);
	}

	public void addListener(String key, ICallListener listener) {
		callContext.addListener(key, listener);
	}

	public void removeListener(String key) {
		callContext.removeListener(key);
	}

	public void initiateCall(Address target) {
		if (callContext.getUserState() == CallAgent.USERSTATE_REGISTERED) {
			callActions.initiateCall(target);
		}
	}

	public void answerCall() {
		if (callContext.getUserState() == CallAgent.USERSTATE_REGISTERED) {
			callActions.answerCall();
		}
	}

	public void terminateCall() {
		if (callContext.getUserState() == CallAgent.USERSTATE_REGISTERED) {
			callActions.terminateCall();
		}
	}

}
