/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.parser.InboxParser;


/**
 * @author Ancuta Gheorghe
 *
 */
public class InboxContent implements IContent {

	public InputStream getContentFromUrl(HttpURLConnection connection)
		throws IOException {
			InputStream content = (InputStream)connection.getInputStream();
			return content;
	}
	
	public void printContentToResponse(HttpServletResponse response, BufferedReader contentFromUrl) 
		throws IOException {
			PrintWriter out = response.getWriter();
			String line = "";
			String contentForResponse = "";
			while ((line = contentFromUrl.readLine()) != null) {
				contentForResponse += line;
			}
			InboxParser inboxParser = new InboxParser();
			ArrayList<HashMap<String, Object>> mails = inboxParser.processData(contentForResponse);
			try {
				out.println("<!DOCTYPE html>");
				out.println("<html><head>");
				out.println("<meta http-equiv='Content-Type' content='application/xml; charset=UTF-8'>");
				out.println("<body>");
				for (HashMap<String, Object> mail: mails){
					out.println("Subject: " + mail.get("su") + "<br>");
					out.println("Content: " + mail.get("fr") + "<br>");
					out.println("Author: " + mail.get("a") + "<br>");
					out.println("Read: " + mail.get("read") + "<br>");
					out.println("Has Attachment: " + mail.get("hasAttachment") + "<br>");
					out.println("Date: " + mail.get("d") + "<br>");
					out.println("Id: " + mail.get("id") + "<br>");
					out.println("Cid: " + mail.get("cid") + "<br>");
					out.println("<br>");
				}
				out.println(contentForResponse);
				out.println("</body></html>");
			} finally {
			   out.close();
			}
	}

}
