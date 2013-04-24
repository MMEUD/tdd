package com.laura.testemood.sorter;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortByValueAndKeyStrategy implements SorterStrategy{

	@Override
	public Map<String, String> sort(Map<String, String> map) {
		List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> m1, Map.Entry<String, String> m2) {
                int cmp1 = m1.getValue().compareTo(m2.getValue());
                if (cmp1 != 0) {
                    return cmp1;
                } else {
                    return m1.getKey().compareTo(m2.getKey());
                }
            }
        });

        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
	}

}
