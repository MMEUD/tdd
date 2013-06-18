/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EmailParser extends AParser {

	public EmailParser(CustomRequest customRequest) {
		super(customRequest);
	}

	public ArrayList<HashMap<String, Object>> processData(String data) {
		return new ArrayList<HashMap<String, Object>>();
	}

}
