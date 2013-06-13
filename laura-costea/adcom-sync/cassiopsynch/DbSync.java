package com.mwt.common.system.tasks.dbsync;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mwt.common.framework.dao.io.ImportHelper;
import com.mwt.common.framework.dao.metadata.FieldMetaData;
import com.mwt.common.framework.dao.metadata.TableMetaData;
import com.mwt.common.system.servers.config.PropertiesLoader;
import com.mwt.common.system.tasks.GenericTask;
import com.mwt.common.utils.CRCUtils;
import com.mwt.common.utils.DownloadUtils;
import com.mwt.common.utils.FileUtils;
import com.mwt.common.utils.TaskUtils;

public class DbSync extends GenericTask {
    
	private String build = null;
	private String message = "";
	
	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	@SuppressWarnings("unchecked")
	public void execute() throws BuildException {
    	setupProperties();
    	PropertiesLoader pl = PropertiesLoader.getInstance();

    	if (openConnection()) {
			SAXReader reader = null;
			Document document = null;
			String syncPath = pl.getServerPath() + pl.get("project") + File.separator + "system" + File.separator + "temp" + File.separator + "sync";
			File syncXmlFile = new File(syncPath + File.separator + "sync.xml"); 
			File writerFile = new File(syncPath + File.separator + "writer.txt"); 
			if (syncXmlFile.exists()) {
				try {
		        	reader = new SAXReader();  
		        	document = reader.read(syncXmlFile);
		
		        	int buildInt = Integer.parseInt(build);
		        	List<Element> theList = document.selectNodes("//sync/*");
		        	for(int i = 0; i < theList.size(); i++) {
		        		Element elem = (Element)theList.get(i);
		        		if (elem.matches("//table")){
		        			String tableName = elem.attributeValue("name");					
							String[] operations = elem.attributeValue("operation").split("\\,");
							
							String ddlPath = syncPath + File.separator + tableName + ".ddl";
							String existentDDL = null;
							String expectedDDL = null;
							try {
					    		TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), tableName);
								existentDDL = ImportHelper.getTempTableDDL(tableName, tmd.getColumns(), tmd.getPkFieldsNames(), true).toString();
							} catch (Exception e) {						
							}
							try {
								expectedDDL = FileUtils.getContents(new File(ddlPath));
							} catch (Exception e) {						
							}

							System.out.println("existentDDL = " + existentDDL);
							System.out.println("expectedDDL = " + expectedDDL);
							
							if (existentDDL == null) {
								TaskUtils.executeUpdate(conn, expectedDDL);
								try {
						    		TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), tableName);
									existentDDL = ImportHelper.getTempTableDDL(tableName, tmd.getColumns(), tmd.getPkFieldsNames(), true).toString();
								} catch (Exception e) {						
								}
							}
		
		
							import_table(conn, syncPath, tableName, expectedDDL, operations);
							log("table " + tableName + " " + elem.attributeValue("operation"));
		        		}
		        		if (elem.matches("//alter")){
		        			
		        			String alterBuild = elem.attributeValue("build").toLowerCase();					
							String operation = elem.attributeValue("operation").toLowerCase();
							int alterBuildInt = Integer.parseInt(alterBuild);


							if (!"".equals(operation)) {
								String alterTable = elem.attributeValue("column").toLowerCase().split("\\.")[0];					
								String alterColumn = elem.attributeValue("column").toLowerCase().split("\\.")[1];					
								String alterType = elem.attributeValue("type").toLowerCase().replaceAll("\\ ", "");
								log("alter " + alterTable + "." + alterColumn + " " + operation);

								String expectedAlterType = "";
								try {
									TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), alterTable);
						    		FieldMetaData field = tmd.getFieldsMap().get(alterColumn);
						    		expectedAlterType = expectedAlterType + field.typeName;
									String decimals = field.decimalDigits != 0 ? "," + field.decimalDigits : "";
									if (!(field.type == Types.DATE || field.type == Types.TIME || field.type == Types.TIMESTAMP)) {
										expectedAlterType = expectedAlterType + "(" + field.size + decimals + ")";
									}
								} catch (Exception e) {		
									e.printStackTrace();
									message = e.getMessage();
								}
								expectedAlterType = expectedAlterType.replaceAll("\\ ", "");
								if (alterBuildInt <= buildInt) {
									if ("add".equals(operation) && !alterType.equals(expectedAlterType)) {
										String sql_test = "select count(" + alterColumn + ") from " + alterTable;
										String[] sql_execute = new String[2];
										sql_execute[0] = "alter table " + alterTable + " add " + alterColumn + " " + alterType;
										sql_execute[1] = "drop table x_" + alterTable;
										alterIfError(conn, sql_test, sql_execute);
									}
									if ("drop".equals(operation) && !"".equals(expectedAlterType)) {
										String sql_test = "select count(" + alterColumn + ") from " + alterTable;
										String[] sql_execute = new String[2];
										sql_execute[0] = "alter table " + alterTable + " drop column " + alterColumn;
										sql_execute[1] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
									if ("modify".equals(operation) && !expectedAlterType.equals(alterType)) {
										String sql_test = "select count(" + alterColumn + ") from " + alterTable;
										String[] sql_execute = new String[2];
										sql_execute[0] = "alter table " + alterTable + " modify column " + alterColumn + " " + alterType;
										sql_execute[1] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
									if ("default".equals(operation) && !expectedAlterType.equals(alterType)) {
										String alterDefault = elem.attributeValue("value").toLowerCase().replaceAll("\\ ", "");
										String sql_test = "select count(" + alterColumn + ") from " + alterTable;
										String[] sql_execute = new String[3];
										sql_execute[0] = "alter table " + alterTable + " alter " + alterColumn + " set default " + alterDefault;
										sql_execute[1] = "update " + alterTable + " set " + alterColumn + " = " + alterDefault + " where " + alterColumn + " is null";
										sql_execute[2] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
								}
							}

							if (alterBuildInt <= buildInt) {
								String sql = elem.getTextTrim();
								if (sql != null && !"".equals(sql)) {
									sqlExecute(conn, sql);
								}
							}
		        		}
		        		if (elem.matches("//alterpk")){
		        			
		        			String alterBuild = elem.attributeValue("build").toLowerCase();					
							String operation = elem.attributeValue("operation").toLowerCase();
							int alterBuildInt = Integer.parseInt(alterBuild);


							if (!"".equals(operation)) {
								String alterTable = elem.attributeValue("table").toLowerCase().replaceAll("\\ ", "");					
								String alterPk = elem.attributeValue("pk");
								log("alterpk " + alterTable + " : " + alterPk + " " + operation);

								boolean identicPk = false;
								int pkSize = 0;
								try {
									TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), alterTable);
									String[] pks_arr = alterPk.split("\\,");
									ArrayList<String> newPks = new ArrayList<String>();
									for (int j=0; j < pks_arr.length; j++) {
										newPks.add(pks_arr[j].trim().toLowerCase());
									}
									pkSize = tmd.getPkFieldsNames().size();
									if (tmd.getPkFieldsNames().size() == newPks.size() && tmd.getPkFieldsNames().containsAll(newPks)) {
										identicPk = true;
									}
								} catch (Exception e) {		
									e.printStackTrace();
									message = e.getMessage();
								}
								if (alterBuildInt <= buildInt) {
									if ("add".equals(operation) && !identicPk) {
										String sql_test = "select count(*) from " + alterTable;
										String[] sql_execute = new String[2];
										sql_execute[0] = "alter table " + alterTable + " add primary key (" + alterPk + " ) ";
										sql_execute[1] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
									if ("drop".equals(operation) && pkSize > 0) {
										String sql_test = "select count(*) from " + alterTable;
										String[] sql_execute = new String[3];
										sql_execute[0] = "alter table " + alterTable + " drop primary key ";
										sql_execute[1] = "alter table " + alterTable + " add primary key (" + alterPk + " ) ";
										sql_execute[2] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
									if ("modify".equals(operation) && !identicPk) {
										String sql_test = "select count(*) from " + alterTable;
										String[] sql_execute = new String[3];
										sql_execute[0] = "alter table " + alterTable + " drop primary key ";
										sql_execute[1] = "alter table " + alterTable + " add primary key (" + alterPk + " ) ";
										sql_execute[2] = "drop table x_" + alterTable;
										alterIfSuccess(conn, sql_test, sql_execute);
									}
								}
							}

							if (alterBuildInt <= buildInt) {
								String sql = elem.getTextTrim();
								if (sql != null && !"".equals(sql)) {
									sqlExecute(conn, sql);
								}
							}
		        		}
		        		if (elem.matches("//sql")){		        			
		        			String rs_sql = elem.attributeValue("rs");
		        			String sql = elem.getTextTrim();
		        			if (rs_sql == null) {
								if (sql != null && !"".equals(sql)) {
									sqlExecute(conn, sql);
								}
		        			} else {
			        			int rs_size = Integer.parseInt(elem.attributeValue("size"));
			        			if (sqlRSsize(conn, rs_sql) == rs_size) {
									if (sql != null && !"".equals(sql)) {
										sqlExecute(conn, sql);
									}			        				
			        			}
		        			}
		        		}
		        		if (elem.matches("//file")){
		        			
		        			String url = elem.attributeValue("from");
							String to = elem.attributeValue("to");
							long new_crc = Long.parseLong(elem.attributeValue("crc"));
							long old_crc = CRCUtils.getCrc32Checksum(new File(this.getProject().getBaseDir() + "/../../" + to));
							
							if (new_crc != old_crc) {
								log("download " + to);
								DownloadUtils.fileDownload(url, this.getProject().getBaseDir() + "/../../" + to);
							}
						}
		        			
		        	}
		        	
				} catch (DocumentException e) {
					e.printStackTrace();
					message = e.getMessage();
				} finally {
					onShutdown(conn, "1", message);
					closeConnection();
				}
				log("DbSync OK");  

			}
			if (writerFile.exists()) {
				message = FileUtils.getContents(writerFile).trim();
				log(message);
				onShutdown(conn, "1", message);
			}
		}
		closeConnection();
    }
    
    private void import_table(Connection conn, String syncPath, String tableName, String ddl, String[] operations) {
    	PropertiesLoader pl = PropertiesLoader.getInstance();
    	HashMap<String, String> sql = new HashMap<String, String>();
    	
    	try {
    		sql.put(tableName + "_drop", "drop table x_" + tableName);
    		sql.put(tableName + "_clean", "delete from x_" + tableName);
    		sql.put(tableName + "_test", "select count(*) as cnt from x_" + tableName);
    		
    		TableMetaData metadata = new TableMetaData(conn, pl.get("connection.schema"), tableName);
    		sql.put(tableName + "_create", ddl.replaceAll("CREATE TABLE ", "CREATE TABLE x_").replaceAll("CONSTRAINT PK_", "CONSTRAINT PK_x_"));
    		sql.put(tableName + "_insert", ImportHelper.getInsertQueryDB2(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());
    		sql.put(tableName + "_update", ImportHelper.getUpdateQueryMysql(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());
    		sql.put(tableName + "_delete", ImportHelper.getDeleteQueryDB2(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());

    		//System.out.println(sql.get(tableName + "_insert"));
    		//System.out.println(sql.get(tableName + "_update"));
    		
    		// create table temp or clean it
    		boolean failed = false;
    		Statement stm = null;
    		try {
    			stm = conn.createStatement();
    			stm.execute(sql.get(tableName + "_test"));
    			stm.close();
    		} catch (SQLException e) { 
    			failed = true;
    		} finally {
    			try {
    				if (stm != null) {
    					stm.close();
    				}
    			} catch (SQLException e2) {
    			}			
    		}
    		if (failed) {
        		TaskUtils.executeUpdate(conn, sql.get(tableName + "_create"));
        	} else {
        		TaskUtils.executeUpdate(conn, sql.get(tableName + "_clean"));
        	}
    	
        	// import csv
        	CsvImport csvImport = new CsvImport(this, metadata, syncPath + File.separator + tableName + ".csv", tableName + ".csv", tableName);
        	csvImport.process();
        	
        	// operations
        	for (int i=0; i < operations.length; i++) {
        		String operation = operations[i];
        		if ("clean".equals(operation)) {
        			TaskUtils.executeUpdate(conn, "delete from " + tableName);
        		}
        		if ("insert".equals(operation)) {
        			TaskUtils.executeUpdate(conn, sql.get(tableName + "_insert"));
        		}
        		if ("update".equals(operation)) {
        			TaskUtils.executeUpdate(conn, sql.get(tableName + "_insert"));
        			if (metadata.getNonPKColumns().size() > 0) {
        				TaskUtils.executeUpdate(conn, sql.get(tableName + "_update"));
        			}
        		}
        	}
        	
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
 
	public void onShutdown(Connection conn, String status_type, String error) {		
    }

	private void alterIfError(Connection conn, String sql_test, String[] sql_execute) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			System.out.println("sql : " + sql_test);
			stm.execute(sql_test);
			stm.close();
		} catch (Exception ex1) {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					ex1.printStackTrace();
				}
			}
			try {
				for(int i=0; i < sql_execute.length; i++) {
					stm = conn.createStatement();
					System.out.println("sql : " + sql_execute[i]);
					log("sql " + sql_execute[i]);
					stm.execute(sql_execute[i]);
					stm.close();
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			} finally {
				if (stm != null) {
					try {
						stm.close();
					} catch (SQLException e) {
					}
				}
			}
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
		}		
	}

	private void alterIfSuccess(Connection conn, String sql_test, String[] sql_execute) {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			System.out.println("sql : " + sql_test);
			stm.execute(sql_test);
			stm.close();

			for(int i=0; i < sql_execute.length; i++) {
				stm = conn.createStatement();
				System.out.println("sql : " + sql_execute[i]);
				log("sql " + sql_execute[i]);
				stm.execute(sql_execute[i]);
				stm.close();
			}
			
		} catch (Exception ex1) {
			ex1.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
		}		
	}

	private void sqlExecute(Connection conn, String sql) {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			System.out.println("sql : " + sql);
			log("sql " + sql);
			stm.execute(sql);
			stm.close();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
		}		
	}

	private int sqlRSsize(Connection conn, String sql) {
		Statement stm = null;
		ResultSet rs = null;
		int cnt = 0;

		try {
			stm = conn.createStatement();
			System.out.println("sql rs : " + sql);
			log("sql rs " + sql);
			rs = stm.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					cnt++;
				}
			}
			rs.close();
			stm.close();
		} catch (SQLException ex1) {
			ex1.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e1) {
			}			
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e2) {
				}
			}
		}		
		System.out.println("sql rs size : " + cnt);
		log("sql rs size " + cnt);
		return cnt;
	}

}

