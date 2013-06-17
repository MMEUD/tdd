/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class AUrl {

	CustomRequest customRequest;
	
	public AUrl(CustomRequest customRequest) {
		this.customRequest = customRequest;
	}
	
	public HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException {
		System.out.println("!!!! connectToUrl");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Accept","*/*");
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		return connection;
	}
	
	public String getEncodedCredentials() {
		String encoding = Base64.encodeBase64String((customRequest.getUsername() + ":" + customRequest.getPassword()).getBytes());
		return encoding;
	}
	
	public abstract URL getConstructedUrl() 
			throws MalformedURLException;

	public abstract void processRequest(HttpServletResponse response, HttpURLConnection connection);
	
}
