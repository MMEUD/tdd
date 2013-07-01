package com.iolma.tests.call;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.iolma.studio.application.Configuration;

public class CallTestRadu extends Application {
	
	private CallTest callTest = null;
	
	public static void main(String[] args) {
		CallTestRadu.launch(args);
	}

	public void start(final Stage stage) throws Exception {
		
		setup();
		
		Parent p = FXMLLoader.load(getFXML() , getResourceBundle());  		
		Scene scene = new Scene(p);
		stage.setScene(scene);
		stage.show();

		Configuration configuration = new Configuration();
		configuration.setDesign("testcall");
		configuration.setFromUser("radu");
		configuration.setFromPassword("radu");
		configuration.setFromName("Radu Obreja");
		configuration.setToUser("andrei");
		configuration.setToName("Andrei Filip");		
		
		configuration.setSipPort(5060);
		configuration.setRtpPort(6060);
		
		callTest = new CallTest(scene, configuration);
		callTest.startup();
	}

	public void stop() throws Exception {
		callTest.shutdown();
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
		return CallTestRadu.class.getResource("/testcall.fxml");
	}
	
	private ResourceBundle getResourceBundle() {
		String language = "english";
		if (getParameters().getNamed().get("language") != null) {
			language = getParameters().getNamed().get("language");
		}
		return ResourceBundle.getBundle(language);
	}

}
