/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ancuta Gheorghe
 *
 */
public interface IParser {

	public ArrayList<HashMap<String, Object>> processData(String data);
	
}
