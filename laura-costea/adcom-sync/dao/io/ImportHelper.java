package com.moodmedia.adcom.dao.io;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.moodmedia.adcom.dao.metadata.FieldMetaData;
import com.moodmedia.adcom.dao.metadata.TableMetaData;




public class ImportHelper {

	public static StringBuffer getInsertQueryDB2(String mainTableName, String secTableName, Collection pks, Collection fileFieldNames) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO ").append(mainTableName).append("(");
		sb.append(generateCommaSeparatedFields(pks, null).toString());
		if (fileFieldNames.size() > pks.size()) {
			sb.append(", ");
			sb.append(generateCommaSeparatedFieldsNonPk(fileFieldNames, pks, null).toString());
		}
		sb.append(") ");
		sb.append(" SELECT ");
		sb.append(generateCommaSeparatedFields(pks, secTableName).toString());
		if (pks.size() < fileFieldNames.size()) {
			sb.append(", ");
			sb.append(generateCommaSeparatedFieldsNonPk(fileFieldNames, pks, secTableName).toString());
		}
		sb.append(" FROM ").append(mainTableName).append(" RIGHT OUTER JOIN ")
				.append(secTableName).append(" ON ");
		int i = 0;
		for (Iterator it = pks.iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			if (i != 0)
				sb.append(" and ");
			sb.append(secTableName).append(".").append(keyName);
			sb.append(" = ");
			sb.append(mainTableName).append(".").append(keyName);
			i++;
		}

		sb.append(" WHERE ");
		i = 0;
		for (Iterator it = pks.iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			if (i != 0)
				sb.append(" and ");
			sb.append(mainTableName).append(".").append(keyName).append(" is null ");
			i++;
		}

		return sb;
	}

	public static StringBuffer getUpdateQueryDB2(String mainTableName, String secTableName, Collection pks, Collection fileFieldNames) {

		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE ").append(mainTableName).append(" SET (");
		sb.append(generateCommaSeparatedFieldsNonPk(fileFieldNames, pks, null)
				.toString());
		sb.append(") = (");
		sb.append(" SELECT ").append(
				generateCommaSeparatedFieldsNonPk(fileFieldNames, pks,
						secTableName).toString());
		sb.append(" FROM ").append(secTableName);
		sb.append(" WHERE ");
		int i = 0;
		for (Iterator it = pks.iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			if (i != 0)
				sb.append(" AND ");
			sb.append(secTableName).append(".").append(keyName);
			sb.append(" = ");
			sb.append(mainTableName).append(".").append(keyName);
			i++;
		}

		sb.append(")");

		sb.append(" WHERE EXISTS (");
		sb.append(" SELECT ").append(
				generateCommaSeparatedFields(pks, secTableName).toString());
		sb.append(" FROM ").append(secTableName);
		sb.append(" WHERE ");
		i = 0;
		for (Iterator it = pks.iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			if (i != 0)
				sb.append(" AND ");
			sb.append(secTableName).append(".").append(keyName);
			sb.append(" = ");
			sb.append(mainTableName).append(".").append(keyName);
			i++;
		}
		sb.append(")");
		return sb;
	}

	
	public static StringBuffer getUpdateQueryMysql(String mainTableName, String secTableName, Collection pks, Collection fileFieldNames) {

		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE ").append(mainTableName + " , " + secTableName).append(" SET ");
		sb.append(generatePairedFieldsNonPk(fileFieldNames, pks, mainTableName, secTableName).toString());
		sb.append(" WHERE ");
		sb.append(generatePairedFieldsPk(pks, mainTableName, secTableName).toString());
		
		return sb;
	}

	
	public static StringBuffer getDeleteQueryDB2(String mainTableName, String secTableName, Collection pks, Collection fileFieldNames) {
		StringBuffer sb = new StringBuffer();
		sb.append(" DELETE FROM ").append(mainTableName);
		sb.append(" WHERE (");
		sb.append(generateCommaSeparatedFields(pks, null).toString());
		sb.append(") IN (");
		sb.append(" SELECT ");
		sb.append(generateCommaSeparatedFields(pks, mainTableName).toString());
		sb.append(" FROM ").append(mainTableName).append(" INNER JOIN ")
				.append(secTableName);
		sb.append(" ON ");
		int i = 0;
		for (Iterator it = pks.iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			if (i != 0)
				sb.append(" and ");
			sb.append(secTableName).append(".").append(keyName);
			sb.append(" = ");
			sb.append(mainTableName).append(".").append(keyName);
			i++;
		}
		sb.append(")");
		return sb;
	}

	public static StringBuffer getTempTableDDL(String tableName, ArrayList fields, ArrayList pkFieldNames, boolean insertPrimaryKey) {
		StringBuffer ddl = new StringBuffer();
		ddl.append("CREATE TABLE ").append(tableName);
		ddl.append(" (");
		int i = 0;
		for (Iterator it = fields.iterator(); it.hasNext();) {
			FieldMetaData field = (FieldMetaData) it.next();
			if (i != 0) {
				ddl.append(",");
			}
			ddl.append(" ").append(field.name).append(" ");
			ddl.append(field.typeName);
			String decimals = field.decimalDigits != 0 ? "," + field.decimalDigits : "";
			if (!(field.type == Types.DATE || field.type == Types.TIME
					|| field.type == Types.TIMESTAMP))
				ddl.append("(").append(field.size).append(decimals).append(")");

			if (field.primaryKey && insertPrimaryKey) {
				ddl.append(" NOT NULL");
			}

			i++;
		}

		// add primary key constraint
		if (insertPrimaryKey && !pkFieldNames.isEmpty()) {
			ddl.append(", CONSTRAINT PK_");
			ddl.append(tableName.substring(0, Math.min(15, tableName.length())));
			ddl.append(" PRIMARY KEY (");

			i = 0;
			for (Iterator it = pkFieldNames.iterator(); it.hasNext();) {
				String field = (String) it.next();
				if (i != 0) {
					ddl.append(",");
				}
				ddl.append(" ").append(field);
				i++;
			}

			ddl.append(")");
		}

		ddl.append(")");

		return ddl;
	}

	
	public static String getTempTableIndex(String tableName, ArrayList fields) {
		StringBuffer ddl = new StringBuffer();
		Date date = new Date();
		if (fields == null || fields.size() == 0)
			return null;

		ddl.append("CREATE INDEX IDX_");
		ddl.append(date.getTime() % 100000000000L);
		ddl.append(" ON ");
		ddl.append(tableName);
		ddl.append(" (");

		int i = 0;
		for (Iterator it = fields.iterator(); it.hasNext();) {
			String field = (String) it.next();
			if (i != 0) {
				ddl.append(",");
			}
			ddl.append(" ").append(field);
			i++;
		}

		ddl.append(")");

		return ddl.toString();
	}
	
	public static StringBuffer generateCommaSeparatedFields(Collection fields,
			String tableName) {
		StringBuffer result = new StringBuffer();
		if (fields == null || fields.size() == 0)
			return result;
		int i = 0;
		for (Iterator it = fields.iterator(); it.hasNext();) {
			String field = (String) it.next();
			if (i != 0) {
				result.append(",");
			}
			if (tableName != null)
				result.append(tableName).append(".");
			result.append(field);
			i++;
		}
		return result;
	}

	public static StringBuffer generateCommaSeparatedFieldsNonPk(Collection fields,
			Collection pk, String tableName) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Iterator it = fields.iterator(); it.hasNext();) {
			String field = (String) it.next();
			//the update will be made only on non-primary key fields
			if (pk.contains(field))
				continue;

			if (i != 0) {
				sb.append(", ");
			}
			if (tableName != null)
				sb.append(tableName).append(".");
			sb.append(field);
			i++;
		}
		return sb;
	}

	public static StringBuffer generatePairedFieldsNonPk(Collection fields,
			Collection pk, String tableName, String secTableName) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Iterator it = fields.iterator(); it.hasNext();) {
			String field = (String) it.next();
			//the update will be made only on non-primary key fields
			if (pk.contains(field))
				continue;

			if (i != 0) {
				sb.append(", ");
			}
			if (tableName != null)
				sb.append(tableName).append(".").append(field).append(" = ").append(secTableName).append(".").append(field);
			i++;
		}
		return sb;
	}

	public static StringBuffer generatePairedFieldsPk(Collection pk, String tableName, String secTableName) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Iterator it = pk.iterator(); it.hasNext();) {
			String field = (String) it.next();

			if (i != 0) {
				sb.append(" and ");
			}
			if (tableName != null)
				sb.append(tableName).append(".").append(field).append(" = ").append(secTableName).append(".").append(field);
			i++;
		}
		return sb;
	}

    public static StringBuffer getInsertRecordsInTempSql(String tableName, ArrayList columns){
		StringBuffer sql = new StringBuffer();
		StringBuffer values = new StringBuffer();
		sql.append("INSERT INTO ").append(tableName).append(" ( ");

		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			values.append("?");
			if (i + 1 < columns.size()) {
				sql.append(",  ");
				values.append(", ");
			}			
		}

		return sql.append(") VALUES (").append(values.toString()).append(")");
	}

    public static ArrayList getInsertRecordsInTempColumns(TableMetaData tableMetaData, String[] csvColumns) throws  SQLException {
		ArrayList columns = new ArrayList();
		HashMap dbFieldsMap = tableMetaData.getFieldsMap();
		ArrayList pks = tableMetaData.getPkFieldsNames();

		for (int i = 0; i < pks.size(); i++) {
			columns.add(pks.get(i));
		}
		
		for (int i = 0; i < csvColumns.length; i++) {
			String fieldName = csvColumns[i];
			if (!dbFieldsMap.containsKey(fieldName)) {
				throw new SQLException(
						"The stream structure does not match the table "
								+ "structure, field '" + fieldName
								+ "' not found");
			}

			if (!pks.contains(fieldName)) {
				columns.add(fieldName);
			}
		}

		return columns;
	}
    
}
