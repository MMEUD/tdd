package com.carrefour.services;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.carrefour.database.DatabaseConnector;
import com.carrefour.servers.IServers;
import com.carrefour.servers.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SyncroService  extends Thread implements IServers {
	private static SyncroService ref = null;
	Context sharedContext = null;
	public SyncroService(){
		
	}

	public static SyncroService getInstance() {
		if (ref == null){
			ref = new SyncroService();
		}
		return ref;
	}
	
	@Override
	public void startup(Context context) {
		sharedContext = context;
		start();
	}

	@Override
	public void shutdown(Context context) {
		if (isAlive()) {
			interrupt();	
		}
		Log.i(getClass().getSimpleName(), "Syncro Service Stop!!!");	
	}
	
	
	public void run(){
		while (true) {
			try {			
				Log.i(getClass().getSimpleName(), "Syncro Service RUN!!!");	
				getNewEvents("frfsamb",null);
			}catch (Exception e) {
				// TODO: handle exception
			}
			try {
				Thread.sleep(25 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //1min
			}
	}

	//GET NEW  EVENTS FROM SERVER
	public void getNewEvents(String username ,String sync){
		username="frfsamb";
		sync=null;
		try {
			HttpClient httpclient = new DefaultHttpClient(Utils.getHttpParams());
			HttpPost httppost = new HttpPost(Utils.SERVICE_URL_SYNC_NEW_EVENTS);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("sync", sync));
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
			// PARSE JSON RESPONSE
			JSONObject jso = new JSONObject(s);	
			if (jso.getInt("id_evenement")==0) {
				Log.i(getClass().getSimpleName(), "NO DATA TO SYNC  FOUND!!!");
			} else {
				Log.i(getClass().getSimpleName(), "DATA TO  SYNC  FOUND!!!"+jso.getString("evenement_datedebutvalidite"));
				
				DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
				dbConnector.open();
				dbConnector.insertFromRest(jso.getInt("id_evenement"), jso.getString("evenement_libelle"), jso.getString("evenement_datedebutvalidite"),
						jso.getString("evenement_datefinvalidite"), jso.getString("evenement_datedebut"), jso.getString("evenement_datefin"),
						jso.getString("evenement_heuredebut"), jso.getString("evenement_heurefin"), jso.getString("evenement_detail"), jso.getString("evenement_nomfichier"),
						jso.getString("evenement_priorite"), jso.getString("evenement_stpdatemodif"), jso.getString("evenement_stpdatecrea"),
						jso.getString("evenement_stputilcrea"), jso.getString("evenement_stputilmodif"), jso.getString("evenement_stpstatut"),
						jso.getString("evenement_stpdatepubli"), jso.getString("evenement_stputilpubli"), jso.getString("evenement_sync"), jso.getString("evenement_deleted"));
				dbConnector.close();
			
			}
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "SYNC ERROR!!!"+e.getMessage());
		}
	}


	
	

	
}