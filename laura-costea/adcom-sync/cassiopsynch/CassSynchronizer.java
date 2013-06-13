package com.moodmedia.adcom.cassiopsynch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class CassSynchronizer extends TimerTask {
	private static final Logger logger = Logger.getLogger(CassSynchronizer.class);

	public void run() {
		InputStream is = null;
		try {
			is = new FileInputStream(new File("d:/fiseretest/cass.xml"));
			SynchManager xms = SynchManager.getSyncManager(new XMLSynchParser(is));
			xms.synch();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}

	}

}
