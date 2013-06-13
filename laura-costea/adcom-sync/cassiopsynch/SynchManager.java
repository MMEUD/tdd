package com.moodmedia.adcom.cassiopsynch;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.moodmedia.adcom.dao.DAOException;
import com.moodmedia.adcom.dao.ImporterDAO;


public class SynchManager {
	private static final Logger logger = Logger.getLogger(SynchManager.class);
	private final ISynchParser parser;
	

	private SynchManager(ISynchParser parser){
		this.parser  = parser;
	}	
	public static SynchManager getSyncManager(ISynchParser parser){
		return new SynchManager(parser);
	}
	
	public void synch() throws DAOException, SQLException{
		List tables = parser.getTableNames();
		for(Iterator it = tables.iterator(); it.hasNext();){
			String tableName = (String) it.next();
			if(ImporterDAO.Helper.importTable(tableName, parser))
				logger.info("CassSynch: Table "+ tableName + " has been successfully synchronized");
		}
	}
	 
}
