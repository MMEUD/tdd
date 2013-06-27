/**
 * 
 */
package com.moodmedia.storeportal.zimbra.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.archive.ZipArchive;
import com.moodmedia.storeportal.zimbra.connection.CustomRequest;
import com.moodmedia.storeportal.zimbra.parser.EmailParser;
import com.moodmedia.storeportal.zimbra.parser.InboxParser;

/**
 * @author Ancuta Gheorghe
 *
 */
@SuppressWarnings("unused")
public class EmailContent extends AContent {

	public EmailContent(CustomRequest customRequest) {
		super(customRequest);
	}

	public InputStream getContentFromUrl(HttpURLConnection connection)
			throws IOException {
		InputStream inputStream = null;
		try {
	    	inputStream = (InputStream)connection.getInputStream();
	    	ZipArchive.unZipToDisk(new File(ZipArchive.saveZipToDisk(inputStream, customRequest)), customRequest);	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return inputStream;
	}

	public void printContentToResponse(HttpServletResponse response,
			BufferedReader contentFromUrl) throws IOException {
		PrintWriter out = response.getWriter();
		String line = "";
		String contentForResponse = "";
		while ((line = contentFromUrl.readLine()) != null) {
			contentForResponse += line;
		}
		EmailParser emailParser = new EmailParser(customRequest);
		ArrayList<HashMap<String, Object>> mails = emailParser.processData(contentForResponse);
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='application/xml; charset=UTF-8'>");
			out.println("<body>");
			for (HashMap<String, Object> mail: mails){
				out.println("To: " + mail.get("to") + "<br>");
				out.println("From: " + mail.get("from") + "<br>");
				out.println("Subject: " + mail.get("subject") + "<br>");
				out.println("Body: " + mail.get("body").toString().replace("\\\n", "<br>") + "<br>");
				int i = 1;
				while (mail.get("attach" + i) != null){
					out.println("Attachment" + i + ": " + mail.get("attach" + i) + "<br>");
					i++;
				}
				out.println("<a href='http://localhost:8080/zcd/mail'>Back</a><br>");
				out.println("<br>");
			}
			out.println(contentForResponse);
			out.println("</body></html>");
		} finally {
		   out.close();
		}
	}

}
