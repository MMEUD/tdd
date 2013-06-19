/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

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
		String eml = "C:\\Users\\Ancuta Gheorghe\\Documents\\GitHub\\tdd\\ancuta-gheorghe\\EmlParser\\src\\267.eml";
		Meme4JParser parser = new Meme4JParser();
        ArrayList<HashMap<String, Object>> mails = parser.parseMessage(eml);
		return mails;
	}

}
