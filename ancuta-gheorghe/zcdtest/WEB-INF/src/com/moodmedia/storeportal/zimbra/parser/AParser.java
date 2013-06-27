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
public abstract class AParser {
	
	CustomRequest customRequest;
	
	public AParser(CustomRequest customRequest) {
		this.customRequest = customRequest;
	}

	public abstract ArrayList<HashMap<String, Object>> processData(String data);
	
}
