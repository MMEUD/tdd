package com.moodmedia.storeportal.actions.messagerie.zimbra;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moodmedia.storeportal.zimbra.connection.CustomConnection;
import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class MailServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
		try {
			CustomRequest customRequest = new CustomRequest();
			customRequest.setType(1);
			CustomConnection customConnection = new CustomConnection(customRequest);
			HttpURLConnection connection = customConnection.getUrl().connectToUrl(customConnection.getUrl().getConstructedUrl(), 
					customConnection.getUrl().getEncodedCredentials());
			customConnection.getUrl().processRequest(response, connection);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
	}

	
}
