/* $Id: DAOCommand.java,v 1.1 2007/01/31 12:02:52 sorina Exp $ */

package com.moodmedia.adcom.dao.helpers;

import com.moodmedia.adcom.dao.DAOException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the interface for a DAO command, which is a commads that is executed
 * by <code>DAOCommandRunner</code><br>
 * A DAOCommand is a command that receives a valid sql connection upon execution.
 * The connection is created/colsed by DAOCommandRunner.<br>
 * <p/>
 * A typical useage would be:<br>
 * <code>
 * DAOCommandRunner.execute(new DAOCommand() {
 * public void execute(java.sql.Connection conn) throws DAOException {
 * //code that use a sql connection
 * }
 * }
 * </code>
 *
 * @author Catalin Grigoroscuta
 * @version $Revision: 1.1 $ $Date: 2007/01/31 12:02:52 $
 * @see DAOCommandRunner
 */
public interface DAOCommand {
    /**
     * The commands that implement this interface must implement this method.
     * The parameter conn is a valid sql connection, and can not be null
     *
     * @param conn a valid database connection
     * @return an Object that will be returned by
     *         <code>DOACOmmandRunner.executeDAOCommand</code>
     * @throws DAOException
     */
    public Object execute(Connection conn) throws DAOException, SQLException;
}
