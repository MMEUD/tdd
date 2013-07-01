package com.iolma.tests.call;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

import com.iolma.studio.application.Configuration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CallTestAndrei extends Application {
	
	private CallTest callTest = null;
	
	public static void main(String[] args) {
		CallTestAndrei.launch(args);
	}

	public void start(final Stage stage) throws Exception {
		
		setup();
		
		Parent p = FXMLLoader.load(getFXML() , getResourceBundle());  		
		Scene scene = new Scene(p);
		stage.setScene(scene);
		stage.show();

		Configuration configuration = new Configuration();
		configuration.setDesign("testcall");
		configuration.setFromUser("andrei");
		configuration.setFromPassword("andrei");
		configuration.setFromName("Andrei Filip");
		configuration.setToUser("radu");
		configuration.setToName("Radu Obreja");
		configuration.setSipPort(5070);
		configuration.setRtpPort(7060);
		
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
		return CallTestAndrei.class.getResource("/testcall.fxml");
	}
	
	private ResourceBundle getResourceBundle() {
		String language = "english";
		if (getParameters().getNamed().get("language") != null) {
			language = getParameters().getNamed().get("language");
		}
		return ResourceBundle.getBundle(language);
	}

}
