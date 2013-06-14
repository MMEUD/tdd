package com.zimbra.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zimbra.Connection;
import com.zimbra.Content;

/**
 * @author Ancuta Gheorghe
 *
 */
public class GetInboxServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
		try {
			HttpURLConnection connection = Connection.connectToUrl(Connection.getConstructedUrl(), 
				Connection.getEncodedCredentials());
			BufferedReader contentFromUrl = 
                new BufferedReader(new InputStreamReader(Content.getContentFromUrl(connection)));
            Content.printContentToResponse(response, contentFromUrl);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
	}

	
}
