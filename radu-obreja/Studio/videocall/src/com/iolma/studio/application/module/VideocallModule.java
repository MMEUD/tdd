package com.iolma.studio.application.module;

import java.text.ParseException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;

import com.iolma.studio.application.IApplicationConfig;
import com.iolma.studio.call.ICallAgent;
import com.iolma.studio.call.ICallListener;
import com.iolma.studio.call.RTPConfig;
import com.iolma.studio.call.agent.AVProfile;
import com.iolma.studio.call.agent.CallAgent;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class VideocallModule implements IServer, ICallListener {

	private LocalModule localModule = null;	
	private IApplicationConfig config = null;
	private ILogger logger = null;
	private Stage stage = null;
	private CallAgent callAgent = null;

	public VideocallModule(LocalModule localModule, IApplicationConfig config, Stage stage) {
		this.localModule = localModule;
		this.config = config;
		this.logger = new TextAreaLogger((TextArea)stage.getScene().lookup("#log"));
		this.stage = stage;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startup() {

		TextField user = (TextField)stage.getScene().lookup("#vc_user");
		if (config.getRemoteUser() != null) {
			user.setText(config.getRemoteUser());
		}
		
		Button callButton = (Button)stage.getScene().lookup("#vc_call");
		callButton.setOnAction(new EventHandler() {
			public void handle(Event arg0) {
				if (callAgent.getCallState() == ICallAgent.CALLSTATE_TALK) {
					callAgent.terminateCall();
				} else {
					if (callAgent.getCallState() == ICallAgent.CALLSTATE_IDLE) {
						TextField user = (TextField)stage.getScene().lookup("#vc_user");
						try {
							SipFactory sipFactory = SipFactory.getInstance();
							AddressFactory addressFactory = sipFactory.createAddressFactory();
							Address remoteAddress = addressFactory.createAddress(addressFactory.createSipURI(user.getText(), "leon.telecast.ro"));
							remoteAddress.setDisplayName(user.getText().toUpperCase());
							callAgent.initiateCall(remoteAddress);
						} catch (PeerUnavailableException e) {
							logger.error("PeerUnavailableException : " + e.getMessage());
						} catch (ParseException e) {
							logger.error("ParseException : " + e.getMessage());
						}
					}
					if (callAgent.getCallState() == ICallAgent.CALLSTATE_RINGING) {
						callAgent.answerCall();
					}
				}
			}
        });
		
		Button hangButton = (Button)stage.getScene().lookup("#vc_hang");
		hangButton.setVisible(false);
		hangButton.setOnAction(new EventHandler() {
			public void handle(Event arg0) {
				if (callAgent.getCallState() == ICallAgent.CALLSTATE_CALLING) {
					callAgent.terminateCall();
				}
				if (callAgent.getCallState() == ICallAgent.CALLSTATE_RINGING) {
					callAgent.terminateCall();
				}
			}
        });
		
		callAgent = new CallAgent(config, logger);
		callAgent.addAudioProfile(new AVProfile(9,"G722", 8000));
		callAgent.addAudioProfile(new AVProfile(101,"telephone-event", 8000));
		callAgent.addVideoProfile(new AVProfile(99,"MP4V-ES", 90000));		
		callAgent.addListener("gui", this);
		callAgent.startup();	

	}

	public void shutdown() {
		callAgent.shutdown();
	}
	
	public synchronized void onPhoneRegistered() {
	}

	public synchronized void onPhoneUnRegistered() {
	}

	public synchronized void onRinging(final SipURI sourceURI) {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)stage.getScene().lookup("#vc_call");
				callButton.setText("Answer");
				Button hangButton = (Button)stage.getScene().lookup("#vc_hang");
				hangButton.setVisible(true);
			}
		});
	}

	public synchronized void onCalling() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)stage.getScene().lookup("#vc_call");
				callButton.setText("Calling");
				Button hangButton = (Button)stage.getScene().lookup("#vc_hang");
				hangButton.setVisible(true);
			}
		});
	}

	public void onCallAnswered(final RTPConfig rtpConfig) {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)stage.getScene().lookup("#vc_call");
				callButton.setText("Terminate");
				Button hangButton = (Button)stage.getScene().lookup("#vc_hang");
				hangButton.setVisible(false);
				logger.debug(rtpConfig.toString());
			}
		});
	}

	public synchronized void onCallTerminated() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)stage.getScene().lookup("#vc_call");
				callButton.setText("Call");
				Button hangButton = (Button)stage.getScene().lookup("#vc_hang");
				hangButton.setVisible(false);
			}
		});
	}

	public void onError(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				logger.error(message);
			}
		});
	}

}
