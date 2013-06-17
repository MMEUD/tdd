/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class InboxUrl extends AUrl{

	
	public InboxUrl(CustomRequest connectionData) {
		super(connectionData);
	}

	public HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Accept","*/*");
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		return connection;
	}

	public String getEncodedCredentials() {
		String encoding = Base64.encodeBase64String((connectionData.getUsername() + ":" + connectionData.getPassword()).getBytes());
		return encoding;
	}

	public URL getConstructedUrl() throws MalformedURLException {
		URL url = new URL (connectionData.getHost() + "" + connectionData.getEmail() + "" + connectionData.getInboxLink());
		return url;
	}
	
}
