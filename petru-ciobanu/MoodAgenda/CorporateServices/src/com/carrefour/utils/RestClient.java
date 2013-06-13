package com.carrefour.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.util.Log;
public class RestClient {
	public static final String SERVICE_URL = "http://192.168.26.102:8080/Agenda/login";
	public static final String SERVICE_URL_SYNC_NEW_EVENTS = "http://192.168.26.102:8080/Agenda/sync_new_events";
	public static final String SERVICE_URL_SYNC_UPDATE_ID = "http://192.168.26.102:8080/Agenda/sync_update_id";
	public static final String SERVICE_URL_SYNC_UPDATE_EVENTS = "http://192.168.26.102:8080/Agenda/sync_update_events";
	public static final String SERVICE_URL_SYNC_INSERT_EVENTS = "http://192.168.26.102:8080/Agenda/sync_insert_events";
    public RestClient() {
       
    }
    @SuppressWarnings("unchecked")
	public String getBaseURI(String str,@SuppressWarnings("rawtypes") List parameter) {
        String result = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(str);
            httppost.setEntity(new UrlEncodedFormEntity(parameter));   
            HttpResponse response = httpClient.execute(httppost);
            result = getResult(response).toString();
            httpClient.getConnectionManager().shutdown();  
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.i(getClass().getSimpleName(), "REST CLIENT ERROR " + e.getMessage());
        } 
        return result;
    }
   
 private StringBuilder getResult(HttpResponse response) throws IllegalStateException, IOException {
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())), 1024);
            String output;
            while ((output = br.readLine()) != null) 
                result.append(output);
            return result;      
      }
}