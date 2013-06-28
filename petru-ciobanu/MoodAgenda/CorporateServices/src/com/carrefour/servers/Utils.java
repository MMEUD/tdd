package com.carrefour.servers;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;



public class Utils {
	
	public static final String SERVICE_URL = "http://192.168.26.102:8080/Agenda/login";
	public static final String SERVICE_URL_SYNC_NEW_EVENTS = "http://192.168.26.102:8080/Agenda/sync_new_events";
	public static final String SERVICE_URL_SYNC_UPDATE_ID = "http://192.168.26.102:8080/Agenda/sync_update_id";
	public static final String SERVICE_URL_SYNC_UPDATE_EVENTS = "http://192.168.26.102:8080/Agenda/sync_update_events";
	public static final String SERVICE_URL_SYNC_INSERT_EVENTS = "http://192.168.26.102:8080/Agenda/sync_insert_events";
	public static final int CONN_TIMEOUT = 3000;
	public static final int SOCKET_TIMEOUT = 5000;
	public static HttpParams getHttpParams() {
			HttpParams htpp = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
			return htpp;
		}
}
