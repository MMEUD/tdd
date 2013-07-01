package com.moodmedia.adcom.dao;

public interface DAOFactory {
    UsersDAO getUsersDAO();

    MessageDAO getMessageDAO();

    TimesheetDAO getTimesheetDAO();

    CategorieMessageDAO getCategorieMessageDAO();

    TypeMessageDAO getTypeMessageDAO();

    UniteDAO getUniteDAO();

    AdressageDAO getAdressageDAO();

    DbSynchronizerDAO getDbSynchronizerDAO();

	JingleDAO getJingleDAO();

	ImporterDAO getImporterDAO();
}

