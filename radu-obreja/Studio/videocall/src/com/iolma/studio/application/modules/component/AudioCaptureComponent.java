package com.iolma.studio.application.modules.component;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import com.iolma.studio.application.module.IAudioComponent;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IProcess;
import com.iolma.studio.process.IServer;
import com.iolma.studio.process.capture.VideoCapture;

public class AudioCaptureComponent implements IServer, IAudioComponent {

	private static final String ENABLE = "_enable";
	
	private Stage stage = null;
	private ILogger logger = null;
	private Mixer mixer = null;
	private Port line = null;
	private String lineId = null;
	private String fxPrefix = null;
	
	private boolean enable = false;
	
	private IProcess statistics = null;
	private IProcess audioInput = null;
	private VideoCapture audioCapture = null;
	
	private HashMap<String, Node> nodes = new HashMap<String, Node>();
	 
	
	public AudioCaptureComponent(Stage stage, IProcess statistics, ILogger logger, Mixer mixer, Port line, String lineId, String fxPrefix) {
		this.stage= stage;
		this.statistics = statistics;
		this.logger = logger;
		this.mixer = mixer;
		this.line = line;
		this.lineId = lineId;
		this.fxPrefix = fxPrefix + lineId;
	}

	public AudioCaptureComponent(Stage stage, ILogger logger, String lineId, String fxPrefix) {
		this.stage= stage; 
		this.lineId = lineId;
		this.fxPrefix = fxPrefix + lineId;
	}

	public void startup() {
		if (line != null) {
			logger.info("AudioCapture : " + mixer.getMixerInfo().getName());
		}
		detectEnable();
	}

	public void shutdown() {
		
	}

	public void setAudioEnabled(boolean enabled) {
		
	}

	public boolean isAudioEnabled() {
		return false;
	}

	public void addAudioInput(IProcess input) {
		
	}

	public void removeAudioInput(IProcess input) {
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void detectControl(final String controlName) {
		Node control = stage.getScene().lookup(fxPrefix + controlName);
		if (control != null) {
			if (line == null) {
				control.setDisable(true);
				control.setVisible(false);
			} else {
				if (control instanceof Button) {
					Button button = (Button)control;
					button.setOnAction(new EventHandler() {
						public void handle(final Event arg0) {
							Platform.runLater(new Runnable() {
								public void run() {
									//onChange(controlName);
								}
							});
						}
					});
					nodes.put(controlName, button);
				}
			}
		}				
	}
	
	private void detectEnable() {
		Node control = stage.getScene().lookup(fxPrefix + ENABLE);
		if (control != null) {
			if (line == null) {
				control.setDisable(true);
				control.setVisible(false);
			} else {
				if (control instanceof CheckBox) {
					String label = mixer.getMixerInfo().getName();
					label = label.replaceAll("\\(", "");
					label = label.replaceAll("\\)", "");
					label = label.replaceAll("Port Microphone", "");
					((CheckBox)control).setText(label);
				}
			}
		}				
	}

}
