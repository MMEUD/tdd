package com.moodmedia.adcom.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.moodmedia.adcom.cassiopsynch.ISynchParser;
import com.moodmedia.adcom.dao.ImporterDAO;
import com.moodmedia.adcom.dao.io.ImportDAO;
import com.moodmedia.adcom.dao.io.ImportHelper;
import com.moodmedia.adcom.dao.metadata.TableMetaData;
import com.moodmedia.adcom.utils.TaskUtils;

public class MySQLImporterDAO implements ImporterDAO {

	private static final HashMap tableMetaData = new HashMap();
	private static final HashMap sql = new HashMap();
	private static final SimpleDateFormat sdf = new SimpleDateFormat();

	public boolean importTable(Connection conn, String tableName, ISynchParser parser) throws SQLException{
		prepare_sqls(conn, tableName);
		create_table_temp(conn, tableName);
		boolean status = false;
		if(process(conn, tableName, parser)) {
			update_table(conn, tableName);
			status = true;
		}
		return status;
	}

	private boolean process(Connection conn, String tableName, ISynchParser parser) {
		ImportDAO dao = new ImportDAO(conn, tableMetaData, sdf, null, null);
		boolean success = true;
		try{
			dao.openTransaction();
			String[] columnNames = parser.getColumns(tableName);
			dao.addPreparedStatement(tableName, columnNames);
			List lines = parser.getLines(tableName);
			HashMap values = new HashMap();
			for(Iterator it = lines.iterator(); it.hasNext();){
				values.clear();
				for(int i=0; i<columnNames.length; i++)
					values.put(columnNames[i], parser.getColumnValue(it.next(), columnNames[i]));
				dao.addValues(values);
			}

		} catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}finally{
			dao.closeTransaction(!success);
		}
		return success;

	}

	private void prepare_sqls(Connection conn, String tableName)  throws SQLException{
		sql.put(tableName + "_drop", "drop table x_" + tableName);
		sql.put(tableName + "_clean", "delete from x_" + tableName);
		sql.put(tableName + "_test", "show tables like 'x_" + tableName + "'");

		TableMetaData tmd = new TableMetaData(conn, null, tableName);
		tableMetaData.put(tableName, tmd);
		sql.put(tableName + "_create", ImportHelper.getTempTableDDL("x_" + tableName, tmd.getColumns(), tmd.getPkFieldsNames(), true).toString());
		sql.put(tableName + "_insert", ImportHelper.getInsertQueryDB2(tableName, "x_" + tableName, tmd.getPkFieldsNames(), tmd.getColumnNames()).toString());
		sql.put(tableName + "_update", ImportHelper.getUpdateQueryMysql(tableName, "x_" + tableName, tmd.getPkFieldsNames(), tmd.getColumnNames()).toString());
		sql.put(tableName + "_delete", ImportHelper.getDeleteQueryDB2(tableName, "x_" + tableName, tmd.getPkFieldsNames(), tmd.getColumnNames()).toString());
	}

	private void create_table_temp(Connection conn, String tableName) {
		ArrayList result = TaskUtils.select(conn, (String) sql.get(tableName + "_test"));
		if (result.size() == 0)
			TaskUtils.executeUpdate(conn, (String) sql.get(tableName + "_create"));
		else
			TaskUtils.executeUpdate(conn, (String) sql.get(tableName + "_clean"));

	}

	private void update_table(Connection conn, String tableName)
	{
		
		if(!"customer".equals(tableName.toLowerCase())){
			TaskUtils.executeUpdate(conn, "delete from " + tableName);
			TaskUtils.executeUpdate(conn, (String) sql.get(tableName + "_insert"));
		} else
			TaskUtils.executeUpdate(conn, (String) sql.get(tableName + "_insert"));
		
	}



}
