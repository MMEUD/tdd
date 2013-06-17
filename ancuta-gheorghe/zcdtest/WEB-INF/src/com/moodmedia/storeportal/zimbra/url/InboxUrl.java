/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.moodmedia.storeportal.zimbra.connection.Request;

/**
 * @author Ancuta Gheorghe
 *
 */
public class InboxUrl extends AUrl{

	public InboxUrl(Request connectionData) {
		super(connectionData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HttpURLConnection connectToUrl(URL url, String encoding)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
