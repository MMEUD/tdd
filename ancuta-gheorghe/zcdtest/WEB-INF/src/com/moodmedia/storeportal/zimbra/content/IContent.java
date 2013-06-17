/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Ancuta Gheorghe
 *
 */
public interface IContent {
	
	public InputStream getContentFromUrl(HttpURLConnection connection)
		throws IOException ;
	
	public void printContentToResponse(HttpServletResponse response, BufferedReader contentFromUrl) 
		throws IOException;

}
