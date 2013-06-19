/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;
import com.moodmedia.storeportal.zimbra.meme4j.Meme4JParser;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EmailParser extends AParser {

	public EmailParser(CustomRequest customRequest) {
		super(customRequest);
	}

	public ArrayList<HashMap<String, Object>> processData(String fileName) {
		String emlLocation = customRequest.getDownloadPath() + "" + customRequest.getUsername() + "_" + customRequest.getIdMail() + "\\inbox";
		ArrayList<HashMap<String, Object>> mails = new ArrayList<HashMap<String, Object>>();
		for (File fileEntry : new File(emlLocation).listFiles()) {
	        Meme4JParser parser = new Meme4JParser();
	        mails = parser.parseMessage(emlLocation + "\\" + fileEntry.getName());
	    }
		return mails;
	}

}
