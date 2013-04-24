package com.laura.testemood.sorter;

import java.util.Map;

public class SorterContext{

	private SorterStrategy strategy;   

	public void setSortingStrategy(SorterStrategy strategy) 
	{
		this.strategy = strategy;  
	}

	public Map<String, String> sortMap(Map<String, String> map) 
	{
		return strategy.sort(map);
	}
}
