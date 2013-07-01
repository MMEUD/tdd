package com.iolma.studio.gui;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoPanel extends BasicProcess {

	private ImageView imageView = null;	
	
	public VideoPanel(ImageView imageView) {		
		this.imageView = imageView;
	}
	
	public IFrame execute(IFrame frame) {
		IVideoPicture picture = frame.getVideoPicture();
		if (picture != null) {
			BufferedImage bufferedImage = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_3BYTE_BGR); 
		    IConverter converter = ConverterFactory.createConverter(bufferedImage, picture.getPixelType()); 
		    bufferedImage = converter.toImage(picture);
		    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
		    imageView.setImage(image);
		}
	    return frame;
	}

}
