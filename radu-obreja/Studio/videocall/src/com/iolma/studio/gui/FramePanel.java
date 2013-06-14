package com.iolma.studio.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class FramePanel extends JPanel {
	
	private static final long serialVersionUID = 1184825817268014928L;
	private BufferedImage bufferedImage;
	
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(bufferedImage, 0, 0, null);
	}

}
