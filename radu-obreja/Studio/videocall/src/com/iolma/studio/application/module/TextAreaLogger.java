package com.iolma.studio.application.module;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import com.iolma.studio.log.ILogger;

public class TextAreaLogger implements ILogger {

	private TextArea textArea = null;
	
	public TextAreaLogger(TextArea textArea) {
		this.textArea = textArea;
	}
	
	
	public synchronized void clear() {
		Platform.runLater(new Runnable() {
			public void run() {
				textArea.clear();
			}
		});
	}

	public synchronized void info(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				textArea.appendText(message + "\n");
			}
		});
	}

	public synchronized void debug(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				textArea.appendText(message + "\n");
			}
		});
	}

	public synchronized void error(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				textArea.appendText(message + "\n");
			}
		});
	}

}
