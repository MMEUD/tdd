/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class AUrl {

	public abstract HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException;
	
	public abstract String getEncodedCredentials();
	
	public abstract URL getConstructedUrl() 
			throws MalformedURLException;
	
}
