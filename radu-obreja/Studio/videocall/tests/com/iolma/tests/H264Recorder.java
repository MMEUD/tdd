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
import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.StatisticsGenerator;
import com.iolma.studio.process.capture.AudioCapture;
import com.iolma.studio.process.capture.VideoCapture;
import com.iolma.studio.process.play.AudioPlay;
import com.iolma.studio.process.record.MP4Recorder;

public class H264Recorder<IProcess> extends JFrame {

	private static final long serialVersionUID = -7743106683549579396L;

	public H264Recorder() {
		
		// Create Container
		final HashMap<String, BasicProcess> processContainer = new HashMap<String, BasicProcess>(); 
		
		// Create logger
		ILogger logger = new ConsoleLogger();
				
		// Create video generator
		FrameGenerator videoGenerator = new FrameGenerator(30, logger);
		videoGenerator.setProcessName("videoGenerator");
		videoGenerator.startup();

		// Create audio generator
		FrameGenerator audioGenerator = new FrameGenerator(25, logger);
		audioGenerator.setProcessName("audioGenerator");
		audioGenerator.startup();

		// Create video capture
		final VideoCapture videoCapture = new VideoCapture(640, 480, 25);
		videoCapture.setDevice("0");
		videoCapture.addInput(videoGenerator);
		videoCapture.startup();
		
		// Create gui
		VideoPanel videoPanel = new VideoPanel(640, 480);
		videoPanel.addInput(videoCapture);
		videoPanel.startup();
				
		// Create audio capture
		final AudioCapture audioCapture = new AudioCapture();		
		audioCapture.addInput(audioGenerator);
		audioCapture.startup();

		// Create audio play
		final AudioPlay audioPlay = new AudioPlay();		
		audioPlay.addInput(audioCapture);
		audioPlay.startup();

		StatisticsGenerator statistics = new StatisticsGenerator(1, logger);
		statistics.addInput(audioCapture);
		statistics.addInput(audioPlay);
		statistics.startup();
		
		JButton btnRecord = new JButton("Record");
		JButton btnStop = new JButton("Stop");
		
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!processContainer.containsKey("recorder")) {
					MP4Recorder recorder = new MP4Recorder("output.mp4", 640, 480);
					recorder.addInput(audioCapture);
					recorder.addInput(videoCapture);
					recorder.startup();
					processContainer.put("recorder", (BasicProcess)recorder);
				}
			}
			
		});

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (processContainer.containsKey("recorder")) {
					BasicProcess recorder = processContainer.get("recorder");
					recorder.shutdown();
					processContainer.remove("recorder");
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
