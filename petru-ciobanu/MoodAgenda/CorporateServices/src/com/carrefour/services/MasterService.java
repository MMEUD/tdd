package com.carrefour.services;
import com.carrefour.servers.StartupServers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MasterService extends Service {
	private StartupServers servers = null;

	@Override
	public void onCreate() {
		servers = new StartupServers();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startid) {
		servers.startup(this);
		super.onStart(intent, startid);
	}

	@Override
	public void onDestroy() {
		servers.shutdown(this);		
	}

}
