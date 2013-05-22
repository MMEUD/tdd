package com.iolma.tests;

import com.iolma.studio.gui.VideoPanel;
import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.StatisticsGenerator;
import com.iolma.studio.process.capture.AudioCapture;
import com.iolma.studio.process.capture.VideoCapture;

public class H264Recorder {

	public static void main(String[] args) {
		
		// Create logger
		ILogger logger = new ConsoleLogger();
		
		// Create statistics
		StatisticsGenerator statistics = new StatisticsGenerator(1, logger);		
		statistics.startup();
		
		// Create video generator
		FrameGenerator videoGenerator = new FrameGenerator(25, logger);

		// Create audio generator
		FrameGenerator audioGenerator = new FrameGenerator(100, logger);

		// Create video capture
		VideoCapture videoCapture = new VideoCapture();		
		videoCapture.addInput(videoGenerator);
		videoCapture.startup();
		statistics.addInput(videoCapture);
		
		// Create audio capture
		AudioCapture audioCapture = new AudioCapture();		
		audioCapture.addInput(audioGenerator);
		audioCapture.startup();
		statistics.addInput(audioCapture);

		// Create gui
		VideoPanel videoPanel = new VideoPanel();
		videoPanel.addInput(videoCapture);
		videoPanel.startup();
		statistics.addInput(videoPanel);
		
		// Start frame generator
		videoGenerator.startup();
		audioGenerator.startup();
		
	}

}
