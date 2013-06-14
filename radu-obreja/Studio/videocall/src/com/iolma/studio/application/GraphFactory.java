package com.iolma.studio.application;

import javafx.stage.Stage;

import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class GraphFactory {

	public static IServer makeGraph(Configuration configuration, Stage stage, ILogger logger) {
		if ("studio".equals(configuration.getDesign())) {
			return (IServer) new StudioGraph(configuration, stage, logger);
		} else {
			return (IServer) new DefaultGraph(configuration, stage, logger);
		}
	}
}
