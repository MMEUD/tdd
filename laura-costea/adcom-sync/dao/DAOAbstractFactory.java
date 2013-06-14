// %1042710791281:mood.catalogic.dao%

/* $Id: DAOAbstractFactory.java,v 1.1 2007/01/31 12:02:52 sorina Exp $ */
package com.moodmedia.adcom.dao;

import com.moodmedia.adcom.dao.mssql.MsSQLDAOFactory;
import com.moodmedia.adcom.dao.mysql.MySQLDAOFactory;

/**
 * DAOAbstractFactory.java Created: Sun Jan  5 14:01:32 2003
 *
 * @author <a href="mailto:d.pomohaci@moodmedia.ro">Dan Pomohaci</a>
 * @version $Revision: 1.1 $
 */
public abstract class DAOAbstractFactory {
    /**
     * current DB in use
     */
    private static int currentDB = DAOAbstractFactory.TYPE_MYSQL;

    /**
     * Type constant for MySQLProdatDocumentNumberDAO
     */
    public static final int TYPE_MYSQL = 1;
    public static final int TYPE_MSSQL = 2;


    /**
     * Changes the DB type
     *
     * @param newDAO DOCUMENT ME!
     */
    public static void setCurrentDAO(int newDAO) {
        currentDB = newDAO;
    }

    /**
     * returns the default DAOFactory
     *
     * @return DOCUMENT ME!
     */
    public static DAOFactory getDAOFactory() {
        return getDAOFactory(currentDB);
    }

    /**
     * Reurn a specific DAOFactory
     *
     * @param type int defining the db (TYPE_)
     * @return a DAOFactory
     */
    public static DAOFactory getDAOFactory(int type) {
        switch (type) {
            case TYPE_MYSQL:
                return new MySQLDAOFactory();
            case TYPE_MSSQL:
                return new MsSQLDAOFactory();

            default:
                return null;
        }
    }
}
