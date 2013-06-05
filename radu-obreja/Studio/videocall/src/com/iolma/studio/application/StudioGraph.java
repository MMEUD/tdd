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

public class StudioGraph implements IServer {

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
	
	
	public StudioGraph(Configuration configuration, Stage stage, ILogger logger) {
		this.configuration = configuration;
		this.stage = stage;
		this.logger = logger;
	}
	
	public void startup() {
		
	}

	public void shutdown() {
		
		if (stage != null) {
			stage.close();
		}
		System.exit(0);
	}

}
