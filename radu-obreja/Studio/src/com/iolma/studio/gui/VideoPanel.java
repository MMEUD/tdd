package com.iolma.studio.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;

public class VideoPanel extends BasicProcess {

	private JPanel panel = new JPanel();
	
	public VideoPanel(int width, int height) {		
		panel.setPreferredSize(new Dimension(width, height));
	}
	
	public JPanel getPanel() {
		return panel;
	}

	public IFrame execute(IFrame frame) {
		
		return frame;
	}

}
