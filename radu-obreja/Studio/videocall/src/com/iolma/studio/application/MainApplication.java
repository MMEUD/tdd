package com.iolma.studio.application;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IServer;

public class MainApplication extends Application {
	
	private IServer graph = null; 

	public static void main(String[] args) {
		MainApplication.launch(args);
	}

	public void start(Stage stage) throws Exception {
		
		ILogger logger = new ConsoleLogger();
		Configuration configuration = getConfiguration();

		setup();
		
		Parent p = FXMLLoader.load(getFXML() , getResourceBundle());  		
		Scene s = new Scene(p);
		stage.setScene(s);
		stage.setFullScreen(true);
		stage.show();

		graph = GraphFactory.makeGraph(configuration, stage, logger);
		graph.startup();

	}

	public void stop() {
		graph.shutdown();
	}

	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setDesign(getParameters().getNamed().get("design"));
		configuration.setFromUser(getParameters().getNamed().get("fromUser"));
		configuration.setFromMd5Password(getParameters().getNamed().get("fromMd5Password"));
		configuration.setFromName(getParameters().getNamed().get("fromName"));
		configuration.setToUser(getParameters().getNamed().get("toUser"));
		configuration.setToName(getParameters().getNamed().get("toName"));
		return configuration;
	}
	
	private void setup() {
		FXMLLoader l = new FXMLLoader();
		l.setBuilderFactory(new JavaFXBuilderFactory());
		
		if( System.getProperty("javafx.version") != null && 
			System.getProperty("javafx.version").startsWith("2.0") ) {
			try {
				Constructor<JavaFXBuilderFactory> c = JavaFXBuilderFactory.class.getConstructor(boolean.class);
				l.setBuilderFactory(c.newInstance(false));
			} catch (Throwable ex ) {
				ex.printStackTrace();
			}
		}		
	}
	
	private URL getFXML() {
		if (getParameters().getNamed().get("design") != null) {
			return MainApplication.class.getResource("/" + getParameters().getNamed().get("design") + ".fxml");
		} else {
			return MainApplication.class.getResource("/default.fxml");
		}
	}
	
	private ResourceBundle getResourceBundle() {
		String language = "english";
		if (getParameters().getNamed().get("language") != null) {
			language = getParameters().getNamed().get("language");
		}
		return ResourceBundle.getBundle(language);
	}
}
