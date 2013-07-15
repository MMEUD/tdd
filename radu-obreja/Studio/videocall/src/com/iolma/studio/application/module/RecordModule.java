package com.iolma.studio.application.module;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.iolma.studio.application.IApplicationConfig;
import com.iolma.studio.call.ICallAgentConfig;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class RecordModule implements IServer {

	private LocalModule localModule = null;
	private VideocallModule videocallModule = null;
	private ICallAgentConfig config = null;
	private ILogger logger = null;
	private Stage stage = null;
	
	public RecordModule(LocalModule localModule, VideocallModule videocallModule, IApplicationConfig config, Stage stage) {
		this.localModule = localModule;
		this.videocallModule = videocallModule;
		this.config = config;
		this.logger  = new TextAreaLogger((TextArea)stage.getScene().lookup("#log"));
		this.stage = stage;
	}

	public void startup() {
	}

	public void shutdown() {
	}
	
}
