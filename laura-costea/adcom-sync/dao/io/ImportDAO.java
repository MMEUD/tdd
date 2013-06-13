package com.moodmedia.adcom.dao.io;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.moodmedia.adcom.dao.metadata.FieldMetaData;
import com.moodmedia.adcom.dao.metadata.TableMetaData;

public class ImportDAO {

	private int batchSize = 1;
	private Connection conn = null;
	private PreparedStatement pst = null;
	private long counter = 0;
	private HashMap tableMetaData = null;
	private SimpleDateFormat sdf_date = null;
	private SimpleDateFormat sdf_time = null;
	private SimpleDateFormat sdf_timestamp = null;
	private String sql = null;
	private ArrayList sql_columns = null;
	private String sql_table = null;
	
	public ImportDAO(Connection conn, HashMap tableMetaData, SimpleDateFormat sdf_date, SimpleDateFormat sdf_time, SimpleDateFormat sdf_timestamp) {
		this.conn = conn;
		this.tableMetaData = tableMetaData;
		this.sdf_date = sdf_date;
		this.sdf_time = sdf_time;
		this.sdf_timestamp = sdf_timestamp;
	}
	
	public void openTransaction() throws SQLException {
		conn.setAutoCommit(false);
	}

	public void addPreparedStatement(String tableName, String[] columns) throws SQLException{
		sql_table = tableName;
		sql_columns = ImportHelper.getInsertRecordsInTempColumns((TableMetaData)tableMetaData.get(tableName), columns);
		sql = ImportHelper.getInsertRecordsInTempSql("x_" + tableName, sql_columns).toString();
		pst = conn.prepareStatement(sql);
	}
	
	public void addValues(HashMap values) throws SQLException, NumberFormatException  {
		//System.out.println(values);
		TableMetaData metaData = (TableMetaData)tableMetaData.get(sql_table);
		for (int i=0; i < sql_columns.size(); i++) {
			String column = (String)sql_columns.get(i);
			FieldMetaData fmetadata = (FieldMetaData)(metaData.getFieldsMap()).get(column);
			int type = fmetadata.type;
			String value = (String) values.get(column);
			if (value == null || "".equals(value) || "null".equals(value.toLowerCase())) {
				value = null;
			}
			
			if (value == null && fmetadata.primaryKey) {
				switch (type) {
				case Types.CHAR:
				case Types.LONGVARCHAR:
				case Types.VARCHAR:
					value = "";
					break;
				case Types.BIGINT:
				case Types.BOOLEAN:
				case Types.DECIMAL:
				case Types.DOUBLE:
				case Types.FLOAT:
				case Types.INTEGER:
				case Types.NUMERIC:
				case Types.SMALLINT:
					value = "0";
					break;
				case Types.DATE:
					value = sdf_date.format(new Date());
					break;
				case Types.TIME:
					value = sdf_time.format(new Date());
					break;						
				case Types.TIMESTAMP:
					value = sdf_timestamp.format(new Date());
					break;						
				case Types.OTHER: 
					value = "";
					break;			
				}
			}

			if (value == null) {
				if(type == Types.OTHER) {
					pst.setNull(i+1, Types.VARCHAR);
				} else {
					pst.setNull(i+1, type);
				}
			} else {
				switch (type) {
					case Types.CHAR:
					case Types.LONGVARCHAR:
					case Types.VARCHAR:
						pst.setString(i+1, value);
						break;
					case Types.BIGINT:
					case Types.BOOLEAN:
					case Types.DECIMAL:
					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.INTEGER:
					case Types.NUMERIC:
					case Types.SMALLINT:
						pst.setObject(i+1, new java.math.BigDecimal(value));
						break;
					case Types.DATE:
						if (sdf_date == null) {  
							pst.setNull(i+1, type);
							break;
						}
						Date d = null;
						try {
							d = sdf_date.parse(value);
							if (d != null) {
								pst.setDate(i+1, new java.sql.Date(d.getTime()));
							}
						} catch (ParseException e) {
						}
						if (d == null) {
							pst.setNull(i+1, type);
						}
						break;
					case Types.TIME:
						if (sdf_time == null) {  
							pst.setNull(i+1, type);
							break;
						}
						d = null;
						try {
							d = sdf_time.parse(value);
							if (d != null) {
								pst.setTime(i+1, new java.sql.Time(d.getTime()));
							}
						} catch (ParseException e) {
						}
						if (d == null) {
							pst.setNull(i+1, type);
						}
						break;						
					case Types.TIMESTAMP:
						if (sdf_timestamp == null) {  
							pst.setNull(i+1, type);
							break;
						}
						d = null;
						try {
							d = sdf_timestamp.parse(value);
							if (d != null) {
								pst.setTimestamp(i+1, new java.sql.Timestamp(d.getTime()));
							}
						} catch (ParseException e) {
						}
						if (d == null) {
							pst.setNull(i+1, type);
						}
						break;						
					case Types.OTHER: 
						pst.setNull(i+1, Types.VARCHAR);
						break;			
						
				}
			}
		}
		pst.addBatch();
		if (counter++ % batchSize == 0) {
			pst.executeBatch();
		}
	}
	
	public void closeTransaction(boolean failed) {
		if (failed) {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
					if (conn != null) {
						conn.setAutoCommit(true);
					}
				} catch (Exception e2) {
				}
			}			
		} else {
			try {
				if (counter % batchSize > 0) {
					pst.executeBatch();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.commit();
				}
			} catch (BatchUpdateException e) {
				e.printStackTrace();
				try {
					if (pst != null) {
						pst.close();
					}
					if (conn != null) {
						conn.rollback();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					if (conn != null) {
						conn.rollback();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
					if (conn != null) {
						conn.setAutoCommit(true);
					}
				} catch (Exception e2) {
				}
			}
		}
	}

	public boolean executeUpdate(String sql) {
		boolean failed = false; 
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate(sql);
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			failed = true;
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
		}
		return failed;
	}

	public HashMap selectOne(String sql) {

		HashMap row = new HashMap();
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs != null) {
				if (rs.next()) {
					for (int i=1; i <= rs.getMetaData().getColumnCount(); i++) {
						String columnName = rs.getMetaData().getColumnName(i);
						row.put(columnName.toLowerCase(), rs.getObject(i));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e2) {
			}			
		}
		return row;
	}
	
}
