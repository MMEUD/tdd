package com.moodmedia.adcom.dao.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.moodmedia.adcom.cassiopsynch.XMLSynchronizerScheduler;



public class TableMetaData  {
	private static final Logger log = Logger.getLogger(XMLSynchronizerScheduler.class);	
		
	private HashMap fieldsMap;
    private ArrayList fields;
    private ArrayList pkfields;
    private ArrayList pkfieldsnames;
    private ArrayList valuefields;
    private Connection con;
    private String vendor;
    
    /**
     * 		Retrives table meta data from the database.
     */
    public TableMetaData(Connection connection, String schema, String table) throws SQLException
    {
        this.con = connection;
        DatabaseMetaData meta = con.getMetaData();
        String userName = meta.getUserName();
        vendor = detectDatabaseVendor(meta);
        
        fields = new ArrayList();
        pkfields = new ArrayList();
        pkfieldsnames = new ArrayList();
        valuefields = new ArrayList();
        fieldsMap = new HashMap();
        
        ResultSet rs = null;
        rs = meta.getColumns(con.getCatalog(), schema, getTableName(table), "%");
        if(rs.next())
        {
            do
            {
                log.debug("getting field...");
                FieldMetaData field = new FieldMetaData();
                field.type = rs.getShort("DATA_TYPE");
                field.name = rs.getString("COLUMN_NAME").toLowerCase();
                field.size = rs.getInt("COLUMN_SIZE");
                field.typeName = rs.getString("TYPE_NAME");
                field.decimalDigits = rs.getInt("DECIMAL_DIGITS");
                if(field.decimalDigits > 100) field.decimalDigits = 0; //if precision is not specified, default 
                //precision can be larger than allowed
                
                //fix for Postgres Numeric between 1 and 1000
                if(field.typeName!=null 
                   && field.typeName.toLowerCase().equals("numeric")
                   && field.size>1000)
                {
                   field.size=20;
                }
                log.debug("name:" + field.name);
                fields.add(field);
                fieldsMap.put(field.name, field);
            }
            while(rs.next());
        }
        else
        {
            log.debug("Cannot retrieve table meta data for table:" +
                    table.toUpperCase() +
                    ", user:" +
                    userName +
                    ", catalog:" +
                    con.getCatalog());
            throw new SQLException(
                    "Cannot retrieve table meta data for table:" +
                    table.toUpperCase() +
                    ", user:" +
                    userName +
                    ", catalog:" +
                    con.getCatalog());
        }
        rs.close();
        
        rs = meta.getPrimaryKeys(con.getCatalog(),
                schema,
                getTableName(table));
        
        while(rs.next()) {
            FieldMetaData field = (FieldMetaData) fieldsMap.get(rs.getString("COLUMN_NAME").toLowerCase());
            field.primaryKey = true;
            pkfields.add(field);
            pkfieldsnames.add(rs.getString("COLUMN_NAME").toLowerCase());
        }
        
        
        for (Iterator it = fields.iterator(); it.hasNext();) {
            Object field = it.next();
            if (!pkfields.contains(field)) {
                valuefields.add(field);
            }
        }
        rs.close();
    }
    
    /** Detects the database vendor*/
    private String detectDatabaseVendor(DatabaseMetaData meta)
    {
        try {
            String product = meta.getDatabaseProductName().toLowerCase();
            if (product.indexOf("oracle") != -1) {
                return "oracle";
            } else if (product.indexOf("microsoft sql server") != -1) {
                return "mssql";
            } else if (product.indexOf("postgresql") != -1) {
                return "postgresql";
            } else if (product.indexOf("mysql") != -1) {
                return "mysql";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
    public String getTableName(String table) {
        if ("postgresql".equalsIgnoreCase(vendor)) {
            return table.toLowerCase().trim();
        } else if ("mysql".equalsIgnoreCase(vendor)) {
            return table;
        } else {
            return table.toUpperCase().trim();
        }
    }
    
    public ArrayList getPkFieldsNames() {
        return pkfieldsnames;
    }
    
    public ArrayList getColumns()
    {
        return fields;
    }
    
    public ArrayList getColumnNames()
    {
		ArrayList columnNames = new ArrayList();
		for (int i=0; i < fields.size(); i++) {
			columnNames.add(((FieldMetaData)fields.get(i)).name);
		}
        return columnNames;
    }
    
    public HashMap getFieldsMap()
    {
        return fieldsMap;
    }
    
    public ArrayList getPKColumns()
    {
        return pkfields;
    }
    
    public ArrayList getNonPKColumns()
    {
        return valuefields;
    }
    
    public String toString()
    {
        return "columns: " +
        fields.size() +
        "; pkcolumns :" +
        pkfields.size() +
        "; valuefields : " +
        valuefields.size();
    }
}

