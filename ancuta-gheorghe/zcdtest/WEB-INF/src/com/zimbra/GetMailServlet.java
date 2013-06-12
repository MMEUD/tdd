package com.zimbra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;


 
public class GetMailServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
		
		try {
            URL url = new URL ("http://lost.moodmedia.ro:6080/home/frfsamb@lost.moodmedia.ro/inbox.rss");///frfsamb@lost.moodmedia.ro");
            String encoding = Base64.encodeBase64String("frfsamb:admin".getBytes());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept","*/*");
            //connection.setDoInput(true);
            //connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   = 
                new BufferedReader (new InputStreamReader (content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
		/*
		String userpass = "frfsamb:admin";
	    //String basicAuth = new String(Base64.encode(userpass.getBytes()));
		URL url = new URL("http://lost.moodmedia.ro:6080/home/frfsamb@lost.moodmedia.ro?auth=ba");//,qp&zauthtoken=" + basicAuth);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		String basicAuth = "Basic " + new String(Base64.encodeBase64(userpass.getBytes()));
		//connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
	    connection.setRequestProperty("Authorization", basicAuth);
		System.out.println(basicAuth);
		connection.connect();

		int code = connection.getResponseCode();
		
		response.setContentType("text/html;charset=UTF-8");
		createResponse(response, code);
		*/
	}

	public void createResponse(HttpServletResponse response, int code) throws IOException {
		PrintWriter out = response.getWriter();
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<body>" + code + "</body></html>");
		} finally {
		   out.close();
		}
	}
}
