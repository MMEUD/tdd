/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.content.IContent;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ChainEmailParser implements IContent {

	/* (non-Javadoc)
	 * @see com.moodmedia.storeportal.zimbra.content.IContent#getContentFromUrl(java.net.HttpURLConnection)
	 */
	@Override
	public InputStream getContentFromUrl(HttpURLConnection connection)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.moodmedia.storeportal.zimbra.content.IContent#printContentToResponse(javax.servlet.http.HttpServletResponse, java.io.BufferedReader)
	 */
	@Override
	public void printContentToResponse(HttpServletResponse response,
			BufferedReader contentFromUrl) throws IOException {
		// TODO Auto-generated method stub

	}

}
