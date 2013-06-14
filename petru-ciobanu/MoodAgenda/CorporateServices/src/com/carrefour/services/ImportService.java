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
import org.json.JSONException;
import org.json.JSONObject;
import com.carrefour.database.DatabaseConnector;
import com.carrefour.servers.IServers;
import com.carrefour.servers.Utils;
import com.carrefour.utils.RestClient;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;

import android.util.Log;

public class ImportService extends Thread implements IServers {
	private static ImportService ref = null;
	Context sharedContext = null;
	private SharedPreferences sharedData;
	private String UserName;
	public ImportService(){
		
	}

	public static ImportService getInstance() {
		if (ref == null){
			ref = new ImportService();
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
			//	importData("frfsamb");
				user();
			}catch (Exception e) {
				  Log.i(getClass().getSimpleName(), "SHARED PREFFERENCE ERROR "+ e);
			}
			try {
				Thread.sleep(25 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //1min
			}
	}
	
	
	public String user() {

		Context otherAppsContext = null;
		try {
			otherAppsContext.createPackageContext("com.agenda.carrefour",0);
			sharedData = otherAppsContext.getSharedPreferences("Agenda",sharedContext.MODE_WORLD_READABLE);
			
			UserName = sharedData.getString("userName", "");
			if (UserName.length() < 1) {
				Log.i(getClass().getSimpleName(), "UserNameNotFound !!!"+ UserName);

			} else {
				Log.i(getClass().getSimpleName(), "UserNameFound !!!"+ UserName);
			}

		} catch (NameNotFoundException e) {
			Log.i(getClass().getSimpleName(), "SHARED PREFERENCES ERRORsdsdsds !!!"+ e);
		}
		return UserName;

	}
	
	
	//CHECK  FOR NEW  EVENTS ON CENTRAL SERVER 
	public void importData(String username) {
			RestClient client = new RestClient();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("sync", "0"));
			String response = client.getBaseURI(RestClient.SERVICE_URL_SYNC_NEW_EVENTS, nameValuePairs); // Json//																								// format
			try {
				JSONObject jso;
				jso = new JSONObject(response);
				if (jso.getInt("id_evenement") == 0) {
					Log.i(getClass().getSimpleName(), "NO EVENTS  ON CENTRAL SERVER  FOUND FOR IMPORT OR  UPDATE!!!");
				} else {
					// CHECK IF RECORDS EXISTS ON  LOCAL  ANDROID  DATABASE
					DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
					dbConnector.open();
					Cursor cursor = dbConnector.checkIdEvenement(jso.getInt("id_evenement"));
					dbConnector.close();
					Log.i(getClass().getSimpleName(), "RECORDS FOUNT FOR IMPORT OR UPDATE " + cursor.getCount());
					if (cursor.getCount() > 0) {
						if (cursor.moveToFirst()) {
							dbConnector.open();
							dbConnector.updateSync(cursor.getInt(cursor.getColumnIndex("_id")), jso.getInt("id_evenement"), jso.getString("evenement_libelle"),
									jso.getString("evenement_datedebutvalidite"), jso.getString("evenement_datefinvalidite"), jso.getString("evenement_datedebut"),
									jso.getString("evenement_datefin"), jso.getString("evenement_heuredebut"), jso.getString("evenement_heurefin"), jso.getString("evenement_detail"),
									jso.getString("evenement_nomfichier"), jso.getString("evenement_priorite"), jso.getString("evenement_stpdatemodif"),
									jso.getString("evenement_stpdatecrea"), jso.getString("evenement_stputilcrea"), jso.getString("evenement_stputilmodif"),
									jso.getString("evenement_stpstatut"), jso.getString("evenement_stpdatepubli"), jso.getString("evenement_stputilpubli"),
									jso.getString("evenement_sync"), jso.getString("evenement_deleted"));
							dbConnector.close();
					    Log.i(getClass().getSimpleName(), "RECORDS ALREADY EXISTS ON LOCAL DB > UPDATE");
						}
					} else {
						
						dbConnector.open();
						dbConnector.insertFromRest(jso.getInt("id_evenement"), jso.getString("evenement_libelle"), jso.getString("evenement_datedebutvalidite"),
								jso.getString("evenement_datefinvalidite"), jso.getString("evenement_datedebut"), jso.getString("evenement_datefin"),
								jso.getString("evenement_heuredebut"), jso.getString("evenement_heurefin"), jso.getString("evenement_detail"), jso.getString("evenement_nomfichier"),
								jso.getString("evenement_priorite"), jso.getString("evenement_stpdatemodif"), jso.getString("evenement_stpdatecrea"),
								jso.getString("evenement_stputilcrea"), jso.getString("evenement_stputilmodif"), jso.getString("evenement_stpstatut"),
								jso.getString("evenement_stpdatepubli"), jso.getString("evenement_stputilpubli"), jso.getString("evenement_sync"), jso.getString("evenement_deleted"));
						dbConnector.close();
						Log.i(getClass().getSimpleName(), "RECORDS NOT EXISTS ON LOCAL DB > INSERT");
					}
					cursor.close();
				}
			} catch (JSONException e) {
				Log.i(getClass().getSimpleName(), "JSON EXCEPTION" + e);
			}
		}
	
	
	// CALL REST END POINT SYNC_NEW_EVENTS AND INSERT NEW EVENTS TO DATABASE
	public void getNewEvents(String username, String sync) {
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
			JSONObject jso = new JSONObject(s);
			if (jso.getInt("id_evenement") == 0) {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT NO DATA TO SYNC FOUND!!!");
			} else {
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
			Log.i(getClass().getSimpleName(), "ERROR FROM REST ENDPOINT !!!" + e.getMessage()+sharedContext);
		}
	}

	// SELECT ANDROID ID RECORD FROM LOCAL DATABASE AND SEND TO REST POINT
	// SYNC_UPDATE_ID
	public void selectEventsId() {
		try {
			DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
			dbConnector.open();
			Cursor cursor = dbConnector.getIdAndroid();
			dbConnector.close();
			if (cursor.getCount() > 0) {
				if (cursor.moveToLast()) {
					String id_evenement = cursor.getString(cursor.getColumnIndex("id_evenement"));
					String evenement_id_android = cursor.getString(cursor.getColumnIndex("_id"));
					syncUpdateId("frfsamb", id_evenement, evenement_id_android, "2", "1");
				}
			} else {
				Log.i(getClass().getSimpleName(), "NO RECORDS  FOUND FOR UPDATE!!");
			}
			cursor.close();
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "UPDATE EVENTS CURSOR ERROR!!!" + e.toString());
		}
	}

	// CALL REST ENDPOINT SYNC_UPDATE_ID TO UPDATE RECORDS WITH ANDROID ID
	public void syncUpdateId(String username, String id_evenement, String evenement_id_android, String sync, String where_sync) {
		try {
			HttpClient httpclient = new DefaultHttpClient(Utils.getHttpParams());
			HttpPost httppost = new HttpPost(Utils.SERVICE_URL_SYNC_UPDATE_ID);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("id_evenement", id_evenement));
			nameValuePairs.add(new BasicNameValuePair("evenement_id_android", evenement_id_android));
			nameValuePairs.add(new BasicNameValuePair("evenement_sync", sync));
			nameValuePairs.add(new BasicNameValuePair("where_sync", where_sync));
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
			if (s.toString().contains("0")) {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT SYNC_UPDATE_ID IS NULL");
			} else {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT SYNC_UPDATE_ID IS NOT NULL");
				// SET SYNC STATUS TO 2 WHICH MEANS THE RECORDS FROM BOTH SIDE
				// IS THE SAME
				DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
				dbConnector.open();
				dbConnector.updateSyncStatus(Integer.parseInt(id_evenement), sync);
				dbConnector.close();
			}
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "ERROR ENDPOINT  SYNC_UPDATE_ID" + e.toString());
		}
	}

	// CALL REST ENDPOINT SYNC _UPDATE_EVENTS FOR CHECK IF RECORDS IS UPDATE OR
	// DELETED
	public void syncUpdateEvents(String username, String sync) {
		try {
			HttpClient httpclient = new DefaultHttpClient(Utils.getHttpParams());
			HttpPost httppost = new HttpPost(Utils.SERVICE_URL_SYNC_UPDATE_EVENTS);
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
			JSONObject jso = new JSONObject(s);
			if (jso.getInt("id_evenement") == 0) {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT SYNC_UPDATE_EVENTS QUERY IS NULL ");
			} else {
				Log.i(getClass().getSimpleName(), "REST ENDPOINT SYNC_UPDATE_EVENTS QUERY IS NOT NULL");
				DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
				dbConnector.open();
				dbConnector.updateSync(2,jso.getInt("id_evenement"), jso.getString("evenement_libelle"), jso.getString("evenement_datedebutvalidite"),
						jso.getString("evenement_datefinvalidite"), jso.getString("evenement_datedebut"), jso.getString("evenement_datefin"),
						jso.getString("evenement_heuredebut"), jso.getString("evenement_heurefin"), jso.getString("evenement_detail"), jso.getString("evenement_nomfichier"),
						jso.getString("evenement_priorite"), jso.getString("evenement_stpdatemodif"), jso.getString("evenement_stpdatecrea"),
						jso.getString("evenement_stputilcrea"), jso.getString("evenement_stputilmodif"), jso.getString("evenement_stpstatut"),
						jso.getString("evenement_stpdatepubli"), jso.getString("evenement_stputilpubli"), "3", jso.getString("evenement_deleted"));
				;
				dbConnector.close();
				// SET SYNC STATUS 2 ON BOTH SIZE
				syncUpdateId("frfsamb", String.valueOf(jso.getInt("id_evenement")), String.valueOf(jso.getInt("evenement_id_android")), "2", "3");
			}
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "ERROR REST ENDPOINT SYNC_UPDATE_EVENTS" + e.toString());
		}
	}

	

	
	
	
	
	
}
