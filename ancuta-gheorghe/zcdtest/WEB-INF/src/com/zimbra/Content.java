/**
 * 
 */
package com.zimbra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

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
		ArrayList<HashMap<String, String>> mails = XmlParser.getFormatedData(contentForResponse);
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='application/xml; charset=UTF-8'>");
			out.println("<body>");
			out.println("Subject: " + contentForResponse + "<br>");
			for (HashMap<String, String> mail: mails){
				out.println("Subject: " + mail.get("title") + "<br>");
				out.println("Content: " + mail.get("description") + "<br>");
				out.println("Author: " + mail.get("author") + "<br>");
				out.println("Date: " + mail.get("pubDate") + "<br>");
				out.println("<br>");
			}
			out.println(contentForResponse);
			out.println("</body></html>");
		} finally {
		   out.close();
		}
	}
}
