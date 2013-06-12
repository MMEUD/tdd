/**
 * 
 */
package com.zimbra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Ancuta Gheorghe
 *
 */
public class Content {

	public static InputStream getContentFromUrl(HttpURLConnection connection)
			throws IOException {
		InputStream content = (InputStream)connection.getInputStream();
		return content;
	}
		
	public static void printContentToResponse(HttpServletResponse response, BufferedReader contentFromUrl) throws IOException {
		PrintWriter out = response.getWriter();
		String line = "";
		String contentForResponse = "";
		while ((line = contentFromUrl.readLine()) != null) {
			contentForResponse += line;
		}
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<body>" + contentForResponse + "</body></html>");
		} finally {
		   out.close();
		}
	}
}
