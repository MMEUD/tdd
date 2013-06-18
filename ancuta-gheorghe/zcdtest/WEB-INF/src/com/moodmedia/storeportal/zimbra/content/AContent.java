/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class AContent {
	
	CustomRequest customRequest;
	
	public AContent(CustomRequest customRequest) {
		this.customRequest = customRequest;
	}
	
	public abstract InputStream getContentFromUrl(HttpURLConnection connection)
		throws IOException ;
	
	public abstract void printContentToResponse(HttpServletResponse response, BufferedReader contentFromUrl) 
		throws IOException;

}
