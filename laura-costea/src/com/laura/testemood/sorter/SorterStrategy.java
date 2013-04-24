package com.laura.testemood.sorter;

import java.util.Map;

public interface SorterStrategy {
	public Map<String, String> sort(Map<String, String> map);
}
