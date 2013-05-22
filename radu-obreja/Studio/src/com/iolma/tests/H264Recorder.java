package com.iolma.tests;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iolma.studio.gui.VideoPanel;
import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.StatisticsGenerator;
import com.iolma.studio.process.capture.AudioCapture;
import com.iolma.studio.process.capture.VideoCapture;

public class H264Recorder<IProcess> extends JFrame {

	private static final long serialVersionUID = -7743106683549579396L;

	public H264Recorder() {
		
		// Create IProcess container
		final HashMap<String, StatisticsGenerator> statisticsGenerators = new HashMap<String, StatisticsGenerator>();		
		
		// Create logger
		final ILogger logger = new ConsoleLogger();
				
		// Create video generator
		final FrameGenerator videoGenerator = new FrameGenerator(25, logger);
		videoGenerator.setProcessName("videoGenerator");
		videoGenerator.startup();

		// Create audio generator
		final FrameGenerator audioGenerator = new FrameGenerator(100, logger);
		audioGenerator.setProcessName("audioGenerator");
		audioGenerator.startup();

		// Create video capture
		VideoCapture videoCapture = new VideoCapture(640, 480, 30);
		videoCapture.setDevice("0");
		videoCapture.addInput(videoGenerator);
		videoCapture.startup();
		
		// Create audio capture
		AudioCapture audioCapture = new AudioCapture();		
		audioCapture.addInput(audioGenerator);
		audioCapture.startup();

		// Create gui
		VideoPanel videoPanel = new VideoPanel(640, 480);
		videoPanel.addInput(videoCapture);
		videoPanel.startup();
				
		JButton btnRecord = new JButton("Record");
		JButton btnStop = new JButton("Stop");
		
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (statisticsGenerators.size() == 0) {
					StatisticsGenerator statistics = new StatisticsGenerator(1, logger);
					statistics.addInput(videoGenerator);
					statistics.addInput(audioGenerator);
					statistics.startup();
					statisticsGenerators.put("StatisticsGenerator", statistics);
				}
			}
			
		});

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (statisticsGenerators.size() == 1) {
					statisticsGenerators.get("StatisticsGenerator").shutdown();
					statisticsGenerators.remove("StatisticsGenerator");
				}
			}
			
		});

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnPanel.add(btnRecord);
		btnPanel.add(btnStop);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(btnPanel, BorderLayout.NORTH);
		getContentPane().add(videoPanel.getPanel(), BorderLayout.CENTER);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);		
	}	
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		new H264Recorder();
		
	}

}
