package com.carrefour.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.carrefour.servers.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExportService extends Service  {
	private Timer timerDownload = new Timer();
	private static int updateInterval = 1;// min
	Context sharedContext = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void startup() {
		startServices();
	}
	
	public void shutdown(){
		shutdownServices();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sharedContext = getApplicationContext();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.stopSelf();
	}

	private void startServices() {
		timerDownload.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Log.i(getClass().getSimpleName(), "Service Export Running!!");
				//syncUpdateId("frfsamb", "test", "566","1");
			}
		}, 0,updateInterval* 10000);
		Log.i(getClass().getSimpleName(), "Service Export Started!!!");
	}
	
	private void shutdownServices() {
		if (timerDownload != null)
			timerDownload.cancel();
		
		this.stopSelf();
		Log.i(getClass().getSimpleName(), "Service Export Stopped!!!");
	}

	public void syncUpdateId(String username ,String evenement_libelle,String evenement_id_android,String evenement_sync) {
		try {
			HttpClient httpclient = new DefaultHttpClient(Utils.getHttpParams());
			HttpPost httppost = new HttpPost(Utils.SERVICE_URL_SYNC_INSERT_EVENTS);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("evenement_libelle", evenement_libelle));
			nameValuePairs.add(new BasicNameValuePair("evenement_id_android", evenement_id_android));
			nameValuePairs.add(new BasicNameValuePair("evenement_sync", evenement_sync));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			is.close();
			JSONObject jso = new JSONObject(s);
			if (jso.getInt("id_evenement") == 0) {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT NO DATA TO INSERT FOUND!!!");
			} else {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT  DATA TO INSERT FOUND!!!" + String.valueOf(jso.getInt("id_evenement")) );
			}
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "ERROR ENDPOINT  SYNC_INSERT_EVENTS" + e.toString());
		}
	}
}
