package com.moodmedia.adcom.cassiopsynch;

import java.util.List;


public interface ISynchParser {
	List getTableNames();
	String[] getColumns(String tableName);
	List getLines(String tableName);
	String getColumnValue(Object line, String string);
	
}
