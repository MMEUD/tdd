package com.laura.testemood.sorter;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SortMapBySpecificValue {
	public static  Set<String> getKeysByValue(Map<String, String> map, String value) {
	     Set<String> keys = new HashSet<String>();
	     for (Entry<String, String> entry : map.entrySet()) {
	         if (value.equals(entry.getValue())) {
	             keys.add(entry.getKey());
	         }
	     }
	     return keys;
	} 
	
}
