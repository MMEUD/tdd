/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.archive.ZipArchive;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EmailContent implements IContent {

	public InputStream getContentFromUrl(HttpURLConnection connection)
			throws IOException {
		String saveTo = "D:\\zcs\\";
		InputStream inputStream = null;
		try {
	    	inputStream = (InputStream)connection.getInputStream();
	    	ZipArchive.saveZipToDisk(saveTo, inputStream); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return inputStream;
	}

	public void printContentToResponse(HttpServletResponse response,
			BufferedReader contentFromUrl) throws IOException {
		//unzip and process
	}

}
