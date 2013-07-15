package com.iolma.studio.application.module;

import java.util.HashMap;
import java.util.List;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import com.iolma.studio.application.IApplicationConfig;
import com.iolma.studio.application.modules.component.AudioCaptureComponent;
import com.iolma.studio.application.modules.component.VideoCaptureComponent;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.IServer;
import com.iolma.studio.process.StatisticsGenerator;
import com.iolma.studio.process.capture.video.Device;
import com.iolma.studio.process.capture.video.OpenIMAJGrabber;

public class LocalModule implements IServer {

	private IApplicationConfig config = null;
	private ILogger logger = null;
	private Stage stage = null;

	private FrameGenerator videoGenerator = null;
	private FrameGenerator audioGenerator = null;
	private StatisticsGenerator statisticsGenerator = null;
	
	private HashMap<String, IServer> videos = new HashMap<String, IServer>();
	private HashMap<String, IServer> audios = new HashMap<String, IServer>();

	public LocalModule(IApplicationConfig config, Stage stage) {
		this.config = config;
		this.logger = new TextAreaLogger((TextArea)stage.getScene().lookup("#log"));
		this.stage = stage;
	}
	
	public void startup() {
		statisticsGenerator = new StatisticsGenerator(1, new TextAreaLogger((TextArea)stage.getScene().lookup("#statistics")));
		statisticsGenerator.startup();

		videoGenerator = new FrameGenerator(25, logger);
		videoGenerator.setProcessName("Video");
		videoGenerator.startup();

		audioGenerator = new FrameGenerator(25, logger);
		audioGenerator.setProcessName("Audio");
		audioGenerator.startup();
		
		OpenIMAJGrabber grabber = new OpenIMAJGrabber();
		List<Device> devices = grabber.getVideoDevices().get().asArrayList();
		for (int i=0; i < devices.size(); i++) {
			Device device = devices.get(i);
			VideoCaptureComponent video = new VideoCaptureComponent(stage, statisticsGenerator, logger, device, "#vi", 640, 480, 25);
			video.addVideoInput(videoGenerator);
			video.startup();
			videos.put(device.getIdentifierStr(), video);
		}
		for (int i=devices.size(); i < 2; i++) {
			VideoCaptureComponent video = new VideoCaptureComponent(stage, logger, i, "#vi", 640, 480, 25);
			video.startup();
		}
		int audioInputId = 0;
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
				try {
					Line line = mixer.getLine(lineInfo);
					if (line instanceof Port) {
						AudioCaptureComponent audio = new AudioCaptureComponent(stage, statisticsGenerator, logger, mixer, (Port)line, audioInputId+"", "#ai");
						audio.addAudioInput(audioGenerator);
						audio.startup();
						audios.put(audioInputId+"", audio);
						audioInputId++;
					}
				} catch (LineUnavailableException e) {
				}
			}
		}	
		for (int i=audioInputId; i < 5; i++) {
			AudioCaptureComponent audio = new AudioCaptureComponent(stage, logger, i+"", "#ai");
			audio.startup();
		}
	}

	public void shutdown() {
		for(String key : videos.keySet()) {
			IServer video = videos.get(key);
			video.shutdown();
		}
		for(String key : audios.keySet()) {
			IServer video = videos.get(key);
			video.shutdown();
		}
		videoGenerator.shutdown();
		audioGenerator.shutdown();
		statisticsGenerator.shutdown();
	}

}
