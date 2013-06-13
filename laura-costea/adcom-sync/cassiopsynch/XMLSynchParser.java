package com.moodmedia.adcom.cassiopsynch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class XMLSynchParser implements ISynchParser {

	private static final String TABLES_ELEMENT = "tables";
	private static final String LINE_ELEMENT = "line";
	private static final String NAME_ELEMENT = "name";
	private final Document doc;

	public XMLSynchParser(InputStream is) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		doc = reader.read(is);
	}


	public String[] getColumns(String tableName) {
		String[] columnNames = null;
		List children = getTable(tableName).elements(LINE_ELEMENT);

		for(Iterator it = children.iterator(); it.hasNext();){
			Element child = (Element) it.next();
			if(child.getName().equals(LINE_ELEMENT)){
				List columns = child.elements();
				columnNames = new String[columns.size()];
				int i=0;
				for(Iterator it2 = columns.iterator(); it2.hasNext();){
					columnNames[i] = ((Element)it2.next()).getName();
					i++;
				}break;
			}
		}
		return columnNames;
	}

	public String getColumnValue(Object line, String columnName) {
		return  ((Element)line).elementText(columnName);
	}


	public List getTableNames() {
		List tableNames = new ArrayList();
		for(Iterator it = getTables().iterator(); it.hasNext();){
			Element table = (Element) it.next();
			tableNames.add(table.elementText(NAME_ELEMENT));
		}
		return tableNames;
	}

	public List getLines(String tableName) {
		Element table = getTable(tableName);
		if(table!=null){
			List lines = table.elements();
			if(lines.size()>0) lines.remove(0);
			return lines;
		}else{
			return Collections.EMPTY_LIST;
		}
	}

	private Element getTable(String tableName){
		assert tableName!=null && !"".equals(tableName);
		Element table = null;
		for(Iterator it = getTables().iterator(); it.hasNext();){
			table = (Element) it.next();
			if(tableName.equalsIgnoreCase(table.elementText(NAME_ELEMENT)))
				return table;
		}
		return table;
	}

	private List getTables() {
		List tables = doc.selectNodes("//"+TABLES_ELEMENT +"/*");
		return tables;
	}

}
