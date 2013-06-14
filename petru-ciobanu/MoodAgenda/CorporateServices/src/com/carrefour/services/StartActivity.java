package com.carrefour.services;
import java.io.File;
import com.carrefour.database.DatabaseConnector;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.SQLException;
import android.util.Log;
public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		if(doesDatabaseExist(this, "carrefour_events")==false){
			try {
				DatabaseConnector dbConnector = new DatabaseConnector(this);
				dbConnector.open();
				dbConnector.close();
			} catch (SQLException e) {
				Log.i(getClass().getSimpleName(), "DATABASE ERROR!!!" + e);
			}	
		}else{
			Log.i(getClass().getSimpleName(), "DATABASE EXISTS!!!");	
		}
		startService(new Intent(this, MasterService.class));
		finish();
		
	
	}

	private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
	    File dbFile=context.getDatabasePath(dbName);
	    return dbFile.exists();
	}

	
}
