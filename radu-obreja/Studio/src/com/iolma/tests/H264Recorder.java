package com.iolma.tests;

import com.iolma.studio.gui.VideoPanel;
import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.capture.VideoCapture;
import com.iolma.studio.process.tick.Ticker;

public class H264Recorder {

	public static void main(String[] args) {
		
		// Create logger
		ILogger logger = new ConsoleLogger();
		
		// Create ticker
		Ticker videoTicker = new Ticker(1);
		videoTicker.setLogger(logger);

		// Create video capture
		VideoCapture videoCapture = new VideoCapture();		
		videoCapture.addInput(videoTicker);
		
		// Create gui
		VideoPanel videoPanel = new VideoPanel();
		videoPanel.addInput(videoCapture);
		
		// Start ticker
		videoTicker.startup();
		
	}

}
