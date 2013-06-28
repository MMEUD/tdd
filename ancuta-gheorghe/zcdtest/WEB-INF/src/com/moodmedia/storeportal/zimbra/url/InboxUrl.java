/**
 * 
 */
package com.moodmedia.storeportal.zimbra.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;
import com.moodmedia.storeportal.zimbra.content.InboxContent;

/**
 * @author Ancuta Gheorghe
 *
 */
public class InboxUrl extends AUrl{

	
	public InboxUrl(CustomRequest connectionData) {
		super(connectionData);
	}

	public URL getConstructedUrl() throws MalformedURLException {
		URL url = new URL (customRequest.getHost() + "" + customRequest.getEmail() + "" + customRequest.getInboxLink());
		return url;
	}

	public void processRequest(HttpServletResponse response,
			HttpURLConnection connection) {
		InboxContent inboxContent = new InboxContent(customRequest);
		BufferedReader contentFromUrl;
		try {
			contentFromUrl = new BufferedReader(new InputStreamReader(inboxContent.getContentFromUrl(connection)));
			inboxContent.printContentToResponse(response, contentFromUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
