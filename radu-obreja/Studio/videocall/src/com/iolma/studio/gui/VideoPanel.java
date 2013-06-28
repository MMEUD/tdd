package com.iolma.studio.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoPanel extends BasicProcess {

	private FramePanel panel = new FramePanel();	
	
	public VideoPanel(int width, int height) {		
		panel.setPreferredSize(new Dimension(width, height));
	}
	
	public JPanel getPanel() {
		return panel;
	}

	public IFrame execute(IFrame frame) {
		IVideoPicture picture = frame.getVideoPicture();
		if (picture != null) {
			BufferedImage bufferedImage = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_3BYTE_BGR); 
		    IConverter converter = ConverterFactory.createConverter(bufferedImage, picture.getPixelType()); 
		    bufferedImage = converter.toImage(picture);
		    panel.setBufferedImage(bufferedImage);
		    panel.repaint();
		}
	    return frame;
	}

}
