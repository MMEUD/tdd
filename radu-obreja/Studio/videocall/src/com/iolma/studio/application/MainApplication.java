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

import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;

import com.iolma.studio.application.module.LocalModule;
import com.iolma.studio.application.module.RecordModule;
import com.iolma.studio.application.module.VideocallModule;
import com.iolma.studio.process.IServer;

public class MainApplication extends Application {
	
	private LocalModule localModule = null; 
	private VideocallModule videocallModule = null; 
	private IServer recordModule = null; 
	

	public static void main(String[] args) {
		MainApplication.launch(args);
	}

	public void start(final Stage stage) throws Exception {
		
		setup();
		
		Parent p = FXMLLoader.load(getFXML() , getResourceBundle());  		
		Scene scene = new Scene(p);
		stage.setScene(scene);
		stage.show();

		SipFactory sipFactory = SipFactory.getInstance();
		AddressFactory addressFactory = sipFactory.createAddressFactory();

		IApplicationConfig config = new Configuration();
		config.setSipProxyHost("leon.telecast.ro");
		config.setSipProxyPort(5080);
		config.setSipLocalPort(5060);

		Address localAddress = addressFactory.createAddress(addressFactory.createSipURI(getParameters().getNamed().get("fromUser"), "leon.telecast.ro"));
		localAddress.setDisplayName(getParameters().getNamed().get("fromName"));		
		config.setLocalAddress(localAddress);		
		config.setLocalPassword(getParameters().getNamed().get("fromMd5Password"));
		
		config.setRemoteUser(getParameters().getNamed().get("toUser"));		
		
		stage.setTitle(config.getLocalAddress().getDisplayName());

		localModule = new LocalModule(config, stage);
		localModule.startup();
		
		videocallModule = new VideocallModule(localModule, config, stage);
		videocallModule.startup();

		recordModule = new RecordModule(localModule, videocallModule, config, stage); 
		recordModule.startup();
	}

	public void stop() throws Exception {
		recordModule.shutdown();
		videocallModule.shutdown();
		localModule.shutdown();
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
		return RaduApplication.class.getResource("/demo.fxml");
	}
	
	private ResourceBundle getResourceBundle() {
		String language = "english";
		if (getParameters().getNamed().get("language") != null) {
			language = getParameters().getNamed().get("language");
		}
		return ResourceBundle.getBundle(language);
	}

}
