package com.mwt.common.system.tasks.dbsync;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.mwt.common.framework.dao.io.ImportDAO;
import com.mwt.common.framework.dao.metadata.TableMetaData;
import com.mwt.common.system.tasks.GenericTask;

public class CsvImport {
	public static final Logger log = LoggerFactory.getLogger(CsvImport.class.getName());

	boolean failed = false;
	boolean error = false;
	
	private String date_format = "yyyy-MM-dd";
	private String time_format = "hh:mm:ss";
	private String timestamp_format = "yyyy-MM-dd hh:mm:ss.S";

	private GenericTask task = null;
	private Connection conn = null;
	private TableMetaData metatdata = null;
	private String csvAbsoluteFileName = null;	
	private String csvFileName = null;	
	private String tableName = null;
	
	public CsvImport(Connection conn, TableMetaData metatdata, String csvAbsoluteFileName, String csvFileName, String tableName) {
		this.conn = conn;
		this.metatdata = metatdata;
		this.csvAbsoluteFileName = csvAbsoluteFileName;
		this.csvFileName = csvFileName;
		this.tableName= tableName;
	}
	
	public CsvImport(GenericTask task, TableMetaData metatdata, String csvAbsoluteFileName, String csvFileName, String tableName) {
		this.task = task;
		this.conn = task.getConnection();
		this.metatdata = metatdata;
		this.csvAbsoluteFileName = csvAbsoluteFileName;
		this.csvFileName = csvFileName;
		this.tableName= tableName;
	}
	
	public boolean process() {
		HashMap<String, TableMetaData> tableMetaData = new HashMap<String, TableMetaData>();
		tableMetaData.put(tableName, metatdata);
		SimpleDateFormat sdf_date = new SimpleDateFormat(date_format);
		SimpleDateFormat sdf_time = new SimpleDateFormat(time_format);
		SimpleDateFormat sdf_timestamp = new SimpleDateFormat(timestamp_format);
		ImportDAO dao = new ImportDAO(conn, tableMetaData, sdf_date, sdf_time, sdf_timestamp);
		CsvReader reader = null;
		try {
			dao.openTransaction();
			reader = new CsvReader(csvAbsoluteFileName, ',', Charset.availableCharsets().get("UTF-8"));
			reader.setTextQualifier('"');
			reader.setUseTextQualifier(true);
			reader.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);
			reader.readHeaders();
			String[] columnsHeader = reader.getHeaders();
			String[] lowerColumnsHeader = new String[columnsHeader.length]; 
			
			for (int i=0; i < columnsHeader.length; i++) {
				lowerColumnsHeader[i] = columnsHeader[i].toLowerCase();
			}

			dao.addPreparedStatement(tableName, lowerColumnsHeader);

			String[] line = null;
			int lineNumber = 0;
			String fullLine = "";
			int skipLine = 0;
			boolean goodLine = true;
			while (reader.readRecord()) {
				line = reader.getValues();
				lineNumber++;
				fullLine = line[0];
				for (int i=1; i < line.length; i++) {
					fullLine = fullLine + "," + line[i];
				}
				if (line.length > 0) {
					HashMap<String, String> values = new HashMap<String, String>();
					goodLine = true;
					for (int i=0; i < lowerColumnsHeader.length; i++) {
						if (i < line.length) {
							values.put(lowerColumnsHeader[i], line[i]);
						} else {
							goodLine = false;
							if (skipLine != lineNumber) {
								logImport("ERROR - CSV file " + csvFileName + " : line " + lineNumber + " -> " + fullLine);
								logImport("ERROR - CSV file " + csvFileName + " : error : parsing error.");
								skipLine = lineNumber;
							}							
						}
					}
					if (goodLine) {
						try {
							dao.addValues(values);
						} catch (NumberFormatException e) {
							failed = true;
							e.printStackTrace();
							logImport("ERROR - CSV file " + csvFileName + " : line " + lineNumber + " -> " + fullLine);
							logImport("ERROR - CSV file " + csvFileName + " : error : " + e.getMessage());
						} catch (SQLException e) {
							error = true;
							if (e.getNextException() != null) {
								e.getNextException().printStackTrace();
								logImport("ERROR - CSV file " + csvFileName + " : line " + lineNumber + " -> " + fullLine);
								logImport("ERROR - CSV file " + csvFileName + " : error : " + e.getNextException().getMessage());
							} else {
								e.printStackTrace();
								logImport("ERROR - CSV file " + csvFileName + " : line " + lineNumber + " -> " + fullLine);
								logImport("ERROR - CSV file " + csvFileName + " : error : " + e.getMessage());
							}
						}
					} else {
						error = true;
					}
				}
			}			
		} catch (FileNotFoundException e1) {
			failed = true;
			e1.printStackTrace();
			logImport("ERROR - CSV file " + csvFileName + " : not found.");
		} catch(IOException e2) {
			failed = true;
			e2.printStackTrace();
			logImport("ERROR - CSV file " + csvFileName + " : " + e2.getMessage());
		} catch (SQLException e3) {
			failed = true;
			e3.printStackTrace();
			if (e3.getNextException() != null) {
				logImport("ERROR - CSV file " + csvFileName + " : " + e3.getNextException().getMessage());
			} else {
				logImport("ERROR - CSV file " + csvFileName + " : " + e3.getMessage());
			}
		} catch(NumberFormatException e5) {
			failed = true;
			e5.printStackTrace();
			logImport("ERROR - CSV file " + csvFileName + " : " + e5.getMessage());
		} catch (Exception e4) {
			failed = true;
			e4.printStackTrace();
			logImport("ERROR - CSV file " + csvFileName + " : " + e4.getMessage());
		} finally {
			reader.close();
			dao.closeTransaction(failed);
		}
		return failed;
	}
	
	private void logImport(String message) {
		if (task != null) {
			task.log(message);
		} else {
			System.out.println(message);
		}
	}
	
}
