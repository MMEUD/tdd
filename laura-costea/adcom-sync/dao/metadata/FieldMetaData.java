package com.moodmedia.adcom.dao.metadata;

public class FieldMetaData
{
	public String name;
	public int type;
	public int size;
	public int decimalDigits;
	public String typeName;
    public boolean primaryKey = false;
	
	public String toString()
	{
		return 
		"name		: " + name + "\n"+
		"type		: " + type + "\n" +
		"size		: " + size + "\n" +
		"decimalDigits	: " + decimalDigits + "\n" +
		"typeName	: " + typeName + "\n";
	}
}