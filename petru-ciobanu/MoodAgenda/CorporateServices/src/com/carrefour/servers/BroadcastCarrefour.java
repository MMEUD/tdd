package com.carrefour.servers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.carrefour.services.MasterService;

public class BroadcastCarrefour extends  BroadcastReceiver {

	private static final String TAG = "BroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(TAG, "Starting MasterService");
		Intent bootServiceIntent = new Intent(context, MasterService.class);
		bootServiceIntent.setAction(MasterService.class.getName());
		context.startService(bootServiceIntent);
	
	}
	
}
