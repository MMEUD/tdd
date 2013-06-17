/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class AUrl {

	CustomRequest connectionData;
	
	public AUrl(CustomRequest connectionData) {
		this.connectionData = connectionData;
	}
	
	public abstract HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException;
	
	public abstract String getEncodedCredentials();
	
	public abstract URL getConstructedUrl() 
			throws MalformedURLException;
	
}
