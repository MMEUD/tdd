package com.mood.stomp.server.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class DatabaseQuery {
	
	protected static Logger log = Logger.getLogger(DatabaseQuery.class.getName());
	private DatabaseService pool = null; 
	
	public DatabaseQuery(DatabaseService pool) {
		this.pool = pool;
	}
	
	public ArrayList<HashMap<String, Object>> select(String sql) {
		log.fine(sql);
		
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = pool.aquire();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					HashMap<String, Object> row = new HashMap<String, Object>();
					for (int i=1; i <= rs.getMetaData().getColumnCount(); i++) {
						String columnName = rs.getMetaData().getColumnName(i);
						row.put(columnName.toLowerCase(), rs.getObject(i));
					}
					result.add(row);
				}
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		} catch (DatabaseException e) {
			log.severe(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
			}			
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e2) {
			}			
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
			}			
		}
		return result;
	}

	public HashMap<String, Object> selectOne(String sql) {
		log.fine(sql);

		HashMap<String, Object> row = new HashMap<String, Object>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = pool.aquire();
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
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			log.severe(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
			}			
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e2) {
			}			
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
			}			
		}
		return row;
	}
	
	public boolean executeUpdate(String sql) {
		log.fine(sql);
		
		boolean success = true; 
		Connection conn = null;
		Statement stm = null;
		try {
			conn = pool.aquire();
			stm = conn.createStatement();
			stm.executeUpdate(sql);
			stm.close();
		} catch (SQLException e) {
			log.severe(e.getMessage());
			success = false;
		} catch (DatabaseException e) {
			log.severe(e.getMessage());
			success = false;
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e2) {
			}			
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
			}			
		}
		return success;
	}	
		
}
