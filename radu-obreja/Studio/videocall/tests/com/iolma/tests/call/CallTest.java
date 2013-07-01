package com.iolma.tests.call;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import com.iolma.studio.application.Configuration;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.network.sip.AVProfile;
import com.iolma.studio.network.sip.IUserAgentListener;
import com.iolma.studio.network.sip.UserAgent;
import com.iolma.studio.process.IServer;

public class CallTest implements IServer, IUserAgentListener {

	private Scene scene = null;
	private Configuration configuration = null;
	
	private UserAgent userAgent = null;

	public CallTest(Scene scene, Configuration configuration) {
		this.scene = scene;
		this.configuration = configuration;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startup() {
		
		TextField user = (TextField)scene.lookup("#user");
		user.setText(configuration.getToUser());
		
		Button registerButton = (Button)scene.lookup("#register");
		registerButton.setOnAction(new EventHandler() {
			public void handle(Event arg0) {
				if (userAgent.isRegistered()) {
					userAgent.unregister();
				} else {
					userAgent.register();
				}
			}
        });
		
		Button callButton = (Button)scene.lookup("#call");
		callButton.setOnAction(new EventHandler() {
			public void handle(Event arg0) {
				if (userAgent.isConnected()) {
					userAgent.terminate();
				} else {
					TextField user = (TextField)scene.lookup("#user");
					if (userAgent.isRinging()) {
						userAgent.answer();
					} else {
						userAgent.call(user.getText());
					}
				}
			}
        });
		
		ILogger logger = new TextAreaLogger(scene, "#log");

		userAgent = new UserAgent(configuration, logger);
		userAgent.addAudioProfile(new AVProfile(9,"G722", 8000));
		userAgent.addAudioProfile(new AVProfile(101,"telephone-event", 8000));
		userAgent.addVideoProfile(new AVProfile(99,"MP4V-ES", 90000));		
		userAgent.addListener("main",this);
		userAgent.startup();	

	}

	public void shutdown() {
		userAgent.shutdown();
	}
	
	public synchronized void onPhoneRegistered() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button registerButton = (Button) scene.lookup("#register");
				registerButton.setText("Unregister");
			}
		});
	}

	public synchronized void onPhoneUnRegistered() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button registerButton = (Button) scene.lookup("#register");
				registerButton.setText("Register");
			}
		});
	}

	public synchronized void onRinging(final String userId, String userName) {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)scene.lookup("#call");
				callButton.setText("Answer to " + userId);
			}
		});
	}

	public synchronized void onCalling() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)scene.lookup("#call");
				callButton.setText("Calling");
			}
		});
	}

	public synchronized void onCallAnswered() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)scene.lookup("#call");
				callButton.setText("Hangdown");
			}
		});
	}

	public synchronized void onCallTerminated() {
		Platform.runLater(new Runnable() {
			public void run() {
				Button callButton = (Button)scene.lookup("#call");
				callButton.setText("Call");
			}
		});
	}

	public void onError(String message) {
		System.out.println(message);
	}
	
}
