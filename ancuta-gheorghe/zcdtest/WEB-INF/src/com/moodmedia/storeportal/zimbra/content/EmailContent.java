/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.archive.ZipArchive;
import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EmailContent extends AContent {

	public EmailContent(CustomRequest customRequest) {
		super(customRequest);
	}

	public InputStream getContentFromUrl(HttpURLConnection connection)
			throws IOException {
		String saveTo = "D:\\zcs\\";
		InputStream inputStream = null;
		try {
	    	inputStream = (InputStream)connection.getInputStream();
	    	//ZipArchive.unzipToDisk(new File(ZipArchive.saveZipToDisk(saveTo, inputStream, customRequest)), customRequest);
	    	ZipArchive.saveZipToDisk(saveTo, inputStream, customRequest);
	    	ZipArchive.unzip();		
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
