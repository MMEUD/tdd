package com.moodmedia.adcom.dao.mysql;

import com.moodmedia.adcom.dao.*;


/**
 * MySQLDAOFactory.java Created: Sun Jan  5 15:33:34 2003
 *
 * @author <a href="mailto:d.pomohaci@moodmedia.ro">Dan Pomohaci</a>
 * @version $Revision: 1.2 $
 */
public class MySQLDAOFactory implements DAOFactory {


    public UsersDAO getUsersDAO() {
        return new MySQLUsersDAO();
    }

    public MessageDAO getMessageDAO() {
        return new MySQLMessageDAO();
    }

    public TimesheetDAO getTimesheetDAO() {
        return new MySQLTimesheetDAO();
    }

    public CategorieMessageDAO getCategorieMessageDAO() {
        return new MySQLCategorieMessageDAO();
    }

    public TypeMessageDAO getTypeMessageDAO() {
        return new MySQLTypeMessageDAO();
    }

    public UniteDAO getUniteDAO() {
        return new MySQLUniteDAO();
    }

    public AdressageDAO getAdressageDAO() {
        return null;
    }

    public DbSynchronizerDAO getDbSynchronizerDAO() {
        return null;
    }

	public JingleDAO getJingleDAO() {
		return new MySQLJingleDAO(); 
	}

	public ImporterDAO getImporterDAO() {
		return new MySQLImporterDAO();
	}
}

