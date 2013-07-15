package com.iolma.studio.process.play;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoPlay extends BasicProcess {

	private ImageView imageView = null;
	private boolean working = false;
	private BufferedImage bufferedImage = null;
	private IVideoResampler resampler = null;
	private IConverter converter = null;
	
	private double lastImageViewWidth = 0;
	private boolean enableResampler = false;
	
	public VideoPlay(ImageView imageView) {		
		this.imageView = imageView;
	}
	
	public IFrame execute(IFrame frame) {
		if (working) {
			return frame;
		}
		working = true;
		IVideoPicture picture = frame.getVideoPicture();
		if (picture != null) {
			
			if (lastImageViewWidth != imageView.getFitWidth()) {
				lastImageViewWidth = imageView.getFitWidth();
				enableResampler = (imageView.getFitWidth() < picture.getWidth());
				bufferedImage = null;
				resampler = null;
				converter = null;
			}
			if (bufferedImage == null) {
				bufferedImage = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			}
			if (enableResampler && resampler == null) {
				resampler = IVideoResampler.make((int)imageView.getFitWidth(), (int)imageView.getFitHeight(), IPixelFormat.Type.BGR24, picture.getWidth(), picture.getHeight(), picture.getPixelType());
			}
			try {
				if (enableResampler) {
					IVideoPicture resizedPicture = IVideoPicture.make(IPixelFormat.Type.BGR24, (int)imageView.getFitWidth(), (int)imageView.getFitHeight());
					resampler.resample(resizedPicture, picture);
					if (converter == null) {
						converter = ConverterFactory.createConverter(bufferedImage, resizedPicture.getPixelType());
					}
				    bufferedImage = converter.toImage(resizedPicture);
				} else {
					if (converter == null) {
						converter = ConverterFactory.createConverter(bufferedImage, picture.getPixelType());
					}
					bufferedImage = converter.toImage(picture);
				}
			    final Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				Platform.runLater(new Runnable() {
					public void run() {
					    imageView.setImage(image);
					    working = false;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    return frame;
	}

}
