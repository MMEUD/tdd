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
import com.moodmedia.storeportal.zimbra.content.EmailContent;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EmailUrl extends AUrl{
	
	public EmailUrl(CustomRequest customRequest) {
		super(customRequest);
	}

	public URL getConstructedUrl() throws MalformedURLException {
		URL url = new URL (customRequest.getHost() + "" + customRequest.getEmail() + "/?id=" + customRequest.getIdMail() + "&fmt=zip");
		return url;
	}

	public void processRequest(HttpServletResponse response,
			HttpURLConnection connection) {
		EmailContent emailContent = new EmailContent(customRequest);
		BufferedReader contentFromUrl;
		try {
			contentFromUrl = new BufferedReader(new InputStreamReader(emailContent.getContentFromUrl(connection)));
			emailContent.printContentToResponse(response, contentFromUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
