/**
 * 
 */
package com.zimbra;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Ancuta Gheorghe
 *
 */
public class Connection {

	private static final String USERNAME = "frfsamb";
	private static final String PASSWORD = "admin";
	private static final String EMAIL = "frfsamb@lost.moodmedia.ro";
	private static final String CREDENTIALS_SEPARATOR = ":";
	private static final String HOST = "http://lost.moodmedia.ro:6080/home/";
	private static final String INBOX = "/inbox.xml";
	
	public static String getEncodedCredentials() {
		String encoding = Base64.encodeBase64String((USERNAME + "" + CREDENTIALS_SEPARATOR + "" + PASSWORD).getBytes());
		return encoding;
	}

	public static URL getConstructedUrl() throws MalformedURLException {
		URL url = new URL (HOST + "" + EMAIL + "" + INBOX);
		return url;
	}
	
	public static HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Accept","*/*");
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		return connection;
	}

}
