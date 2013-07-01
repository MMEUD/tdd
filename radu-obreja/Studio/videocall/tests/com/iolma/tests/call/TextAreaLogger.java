package com.iolma.tests.call;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import com.iolma.studio.log.ILogger;

public class TextAreaLogger implements ILogger {

	private Scene scene = null;
	private String componentId = null;
	
	public TextAreaLogger(Scene scene, String componentId) {
		this.scene = scene;
		this.componentId = componentId;
	}
	
	
	public synchronized void info(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				TextArea log = (TextArea)scene.lookup(componentId);
				log.appendText(message + "\n");
			}
		});
	}

	public synchronized void debug(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				TextArea log = (TextArea)scene.lookup(componentId);
				log.appendText(message + "\n");
			}
		});
	}

	public synchronized void error(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				TextArea log = (TextArea)scene.lookup(componentId);
				log.appendText(message + "\n");
			}
		});
	}

}
