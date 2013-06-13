package com.moodmedia.adcom.dao.helpers;


import com.moodmedia.adcom.dao.DAOException;
import snaq.db.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class executes DAO Commands.
 * Its responsability is to create database connections if necessary, and to
 * call the execute method on DAOCommand.<br>
 * For a usage example, see DAOCommand
 *
 * @author Catalin Grigoroscuta
 * @version $Revision: 1.1 $ $Date: 2007/01/31 12:02:52 $
 * @see DAOCommand
 */
public class DAOCommandRunner {
    private static ConnectionPoolManager poolManager =
            ConnectionPoolManager.getInstance("connection.properties");
    private static ConnectionPoolManager mssqlPoolManager =
            ConnectionPoolManager.getInstance("mssql-connection.properties");

    /**
     * Executes a DAO command.
     * The execute method createa a new connection, sets autocommit on false
     * (if transactions are supported), and calls the command's execute method
     * with the connection.
     * After the command finishes its execution, if the execution is finished ok,
     * it will commit the transaction and close the connection; if the command
     * execcution finishes with exception, the transactions will not be commited.
     * So, the command will execute in a single transaction.
     *
     * @param command the command to be executed
     * @return the Object returned by <code>DAOCommand.execute()</code>
     * @throws DAOException
     */
    public static Object executeDAOCommand(DAOCommand command)
            throws DAOException {
        Connection conn = null;
        try {
            conn = poolManager.getConnection("local");
        } catch (SQLException e) {
            throw new DAOException(e, "Could not get connection to database");
        }

        if (conn != null) {
            boolean committed = false;
            try {

                /* begin a transaction, if possible */
                if (conn.getMetaData().supportsTransactions()) {
                    conn.setAutoCommit(false);
                }

                /* execute the command */
                Object result = command.execute(conn);

                /* commit the transaction */
                if (conn.getMetaData().supportsTransactions()) {
                    if(!conn.getAutoCommit()) conn.commit();
                    committed = true;
                }
                return result;
            } catch (SQLException e) {
                throw new DAOException(e);
            } finally {
                /* transaction was not committed, try to rollback */
                try {
                    if ((!committed) && (conn.getMetaData().supportsTransactions())) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DAOException(e, "Error in closing connection");
                }
            } //finally
        } else {
            throw new DAOException("Could not get connection to database");
        }
    } //executeDAOCommand

    public static Object executeDAOCommandMsSql(DAOCommand command)
            throws DAOException {
        Connection conn = null;
        try {
            conn = mssqlPoolManager.getConnection("mssql");
        } catch (SQLException e) {
            throw new DAOException(e, "Could not get connection to database");
        }

        if (conn != null) {
            boolean committed = false;
            try {

                /* begin a transaction, if possible */
                if (conn.getMetaData().supportsTransactions()) {
                    conn.setAutoCommit(false);
                }

                /* execute the command */
                Object result = command.execute(conn);

                /* commit the transaction */
                if (conn.getMetaData().supportsTransactions()) {
                    conn.commit();
                    committed = true;
                }
                return result;
            } catch (SQLException e) {
                throw new DAOException(e);
            } finally {
                /* transaction was not committed, try to rollback */
                try {
                    if ((!committed)
                            && (conn.getMetaData().supportsTransactions())) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DAOException(e, "Error in closing connection");
                }
            } //finally
        } else {
            throw new DAOException("Could not get connection to database");
        }
    } //executeMsSqlDAOCommand
}
