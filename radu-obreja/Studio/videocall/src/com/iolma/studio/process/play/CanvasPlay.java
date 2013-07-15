package com.iolma.studio.process.play;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class CanvasPlay extends BasicProcess {

	private GraphicsContext graphics = null;
	private long start = 0;
	
	public CanvasPlay(Pane pane) {		
		Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
		pane.getChildren().add(canvas);
		graphics = canvas.getGraphicsContext2D();
	}
	
	public IFrame execute(IFrame frame) {
		start = System.currentTimeMillis();
		IVideoPicture picture = frame.getVideoPicture();
		if (picture != null) {
			BufferedImage bufferedImage = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_3BYTE_BGR); 
		    IConverter converter = ConverterFactory.createConverter(bufferedImage, picture.getPixelType()); 
		    bufferedImage = converter.toImage(picture);
		    System.out.println("A = " + (System.currentTimeMillis() - start));
		    final Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			Platform.runLater(new Runnable() {
				public void run() {
				    graphics.drawImage(image, 0, 0);
				    System.out.println("B = " + (System.currentTimeMillis() - start));
				}
			});
		}
	    return frame;
	}

}
