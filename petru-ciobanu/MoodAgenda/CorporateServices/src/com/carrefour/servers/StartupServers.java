package com.carrefour.servers;

import android.content.Context;

import com.carrefour.services.ExportService;
import com.carrefour.services.ImportService;
import com.carrefour.services.SyncroService;

public class StartupServers implements IServers {
	// START & STOP REGISTER SERVICE (Service Import , Service Export);
	public void startup(Context context) {
		ImportService importService = new ImportService();
		importService.startup(context);

		//ExportService exportService = new ExportService();
		//exportService.startup();
  
	}

	public void shutdown(Context context) {
	
		 SyncroService  sync = new  SyncroService ();
	     sync.shutdown(context);
	     
	     
	     ImportService importService = new ImportService();
		 importService.shutdown(context);
	     
	     
	}

}
