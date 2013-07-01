package com.moodmedia.adcom.dao;

import org.apache.log4j.Logger;

import java.sql.*;

/**
 * DAOUtils class contains just helper routines used in dao package
 *
 * @author <a href="mailto:c.dragoi@moodmedia.ro">Ciprian Dragoi</a>
 */
public class DAOUtils {
    private static final Logger log = Logger.getLogger(DAOUtils.class);

    /**
     * Safely closes a PreparedStatement instance.
     * Previous to close() method call, this routine does a
     * null chekcing and if the argument is not null, it calls
     * close() on it.
     *
     * @param stm is the PreparedStatement to be closed
     */
    public static void close(PreparedStatement stm) {
        if (stm == null) {
            return;
        }

        try {
            stm.close();
        } catch (SQLException e) {
            log.error("error closing prepared statement", e);
        }
    }

    /**
     * Safely closes a Statement instance.
     * Previous to close() method call, this routine does a
     * null chekcing and if the argument is not null, it calls
     * close() on it.
     *
     * @param stm is the Statement to be closed
     */
    public static void close(Statement stm) {
        if (stm == null) {
            return;
        }

        try {
            stm.close();
        } catch (SQLException e) {
            log.error("error closing statement", e);
        }
    }

    /**
     * Safely closes a ResultSet instance.
     * Previous to close() method call, this routine does a
     * null chekcing and if the argument is not null, it calls
     * close() on it.
     *
     * @param rs is the ResultSet to be closed
     */
    public static void close(ResultSet rs) {
        if (rs == null) {
            return;
        }

        try {
            rs.close();
        } catch (SQLException e) {
            log.error("error closing result set", e);
        }
    }

    /**
     * Safely closes a Connection instance.
     * Previous to close() method call, this routine does a
     * null chekcing and if the argument is not null, it calls
     * close() on it.
     *
     * @param conn is the Connection to be closed
     */
    public static void close(Connection conn) {
        if (conn == null) {
            return;
        }

        try {
            conn.close();
        } catch (SQLException e) {
            log.error("error closing connection", e);
        }
    }

    /**
     * This function return an 'Incremented' Id for a Given Table'. It handles the
     * lock (and minimize it) for the operation.
     * ATTENTION: It does not check if the new Id for the Table is coherent with the id
     * existing in the table.
     * PROBLEMS can occur if someone add a record with an Id without
     * taking care of this rule.
     *
     * @param conn      the connection; if is null it is created in the method
     *                  but only for backward compatibility (unfortunatelly); in future implementations it
     *                  must not accept null
     * @param tableName name of table;is not case sensitive
     * @return 'Incremented' Id
     * @throws SQLException if something is wrong
     */
    public static synchronized long getId(Connection conn, String tableName, boolean nextCode) throws SQLException {
        PreparedStatement pStmt = null;

        //si le nom de la table(tableName) existe dans A_Identity le flag=1 sinon flag=0
        int iFlg = 1;
        log.debug("entering getId for the table " + tableName);
        long newId = 0;

        log.debug("before locking the table a_identity (SELECT ... FOR UPDATE)");
        PreparedStatement stmt = null;
        tableName = tableName.toUpperCase();
        stmt = conn.prepareStatement("SELECT id FROM a_identity WHERE UPPER(tablename) = ? ");
        stmt.setString(1, tableName);

        try {
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                newId = rs.getLong(1) + 1;
            else {
                newId = 1000;
                //the table does not exist in A_Identity
                iFlg = 0;
            }

            log.debug(" the table a_identity is locked now, after i've executed SELECT ... FOR UPDATE the newId is: " + newId);

            if (nextCode) {
                if (iFlg == 1)
                    pStmt = conn.prepareStatement("UPDATE a_identity SET id = ? WHERE UPPER(tablename)= ? ");
                else
                    pStmt = conn.prepareStatement("INSERT INTO a_identity ( id, tablename) VALUES(?, ? )");
                pStmt.setLong(1, newId);
                pStmt.setString(2, tableName);

                int updateResult = pStmt.executeUpdate();
                log.debug(" the lock upon a_identity is released now (after i executed UPDATE)");
                if (updateResult != 1)
                    return 0;
            } else {
                return newId;
            }


        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                log.error("Statement Exception!", e);
            }
        }

        log.debug(" exiting getId for the table " + tableName + " returning the id: " + newId);
        return newId;
    }


}
