/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.moodmedia.storeportal.zimbra.connection.Request;

/**
 * @author ancuta
 *
 */
public abstract class AUrl {

	Request connectionData;
	
	public AUrl(Request connectionData) {
		this.connectionData = connectionData;
	}

	public Request getConnectionData() {
		return connectionData;
	}

	public void setConnectionData(Request connectionData) {
		this.connectionData = connectionData;
	}

	public abstract HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException;
	
}
