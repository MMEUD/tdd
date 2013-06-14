package com.mood.stomp;

import com.mood.stomp.config.Configuration;
import com.mood.stomp.server.pool.DatabaseQuery;
import com.mood.stomp.server.pool.DatabaseService;
import com.mood.stomp.server.stomp.StompDao;
import com.mood.stomp.server.stomp.StompService;
import com.mood.stomp.server.stomp.notification.JukeboxRetriever;
import com.mood.stomp.server.stomp.notification.UpdatesRetriever;

public class Stomp {

	public static void main(String[] args) {
		
		try {
			Configuration config = new Configuration();
			config.configureLogger("/logger.properties");
			config.configureStomp("/conf/stomp.properties");
	
			DatabaseService pool = new DatabaseService(config);
			pool.startup();
			
			DatabaseQuery query = new DatabaseQuery(pool);
			StompDao dao = new StompDao(query);
			
			StompService stomp = new StompService(config, dao);
			stomp.addNotificationRetriever(new UpdatesRetriever());
			stomp.addNotificationRetriever(new JukeboxRetriever());
			stomp.startup();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}

}
