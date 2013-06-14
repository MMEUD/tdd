package com.iolma.studio.application;

import javafx.stage.Stage;

import com.iolma.studio.gui.VideoPanel;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.IServer;
import com.iolma.studio.process.StatisticsGenerator;
import com.iolma.studio.process.capture.AudioCapture;
import com.iolma.studio.process.capture.VideoCapture;
import com.iolma.studio.process.play.AudioPlay;

public class DefaultGraph implements IServer {

	private Configuration configuration = null;
	private Stage stage = null;
	private ILogger logger = null;
	
	private FrameGenerator videoGenerator = null;
	private FrameGenerator audioGenerator = null;
	private VideoCapture videoCapture = null;
	private VideoPanel videoPanel = null;
	private AudioCapture audioCapture = null;
	private AudioPlay audioPlay = null;
	private StatisticsGenerator statistics = null;
	
	
	public DefaultGraph(Configuration configuration, Stage stage, ILogger logger) {
		this.configuration = configuration;
		this.stage = stage;
		this.logger = logger;
	}
	
	public void startup() {
		
		// Create video generator
		videoGenerator = new FrameGenerator(30, logger);
		videoGenerator.setProcessName("videoGenerator");
		videoGenerator.startup();

		// Create audio generator
		audioGenerator = new FrameGenerator(25, logger);
		audioGenerator.setProcessName("audioGenerator");
		audioGenerator.startup();

		// Create video capture
		videoCapture = new VideoCapture(640, 480, 25);
		videoCapture.setDevice("0");
		videoCapture.addInput(videoGenerator);
		videoCapture.startup();
		
		// Create gui
		videoPanel = new VideoPanel(640, 480);
		videoPanel.addInput(videoCapture);
		videoPanel.startup();
				
		// Create audio capture
		audioCapture = new AudioCapture();		
		audioCapture.addInput(audioGenerator);
		audioCapture.startup();

		// Create audio play
		audioPlay = new AudioPlay();		
		audioPlay.addInput(audioCapture);
		audioPlay.startup();

		// Create statistics
		statistics = new StatisticsGenerator(1, logger);
		statistics.addInput(audioCapture);
		statistics.addInput(audioPlay);
		statistics.startup();
		
	}

	public void shutdown() {
		
		if (videoGenerator != null) {
			videoGenerator.shutdown();
		}
		if (audioGenerator != null) {
			audioGenerator.shutdown();
		}
		if (videoCapture != null) {
			videoCapture.shutdown();
		}
		if (videoPanel != null) {
			videoPanel.shutdown();
		}
		if (audioCapture != null) {
			audioCapture.shutdown();
		}
		if (audioPlay != null) {
			audioPlay.shutdown();
		}
		if (statistics != null) {
			statistics.shutdown();
		}
		if (stage != null) {
			stage.close();
		}
		System.exit(0);
	}

}
