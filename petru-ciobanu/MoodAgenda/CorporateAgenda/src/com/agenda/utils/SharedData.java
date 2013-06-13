package com.agenda.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;

import android.content.SharedPreferences.Editor;

public class SharedData {
private static final String APP_SHARED_PREFS = "Agenda"; 
public SharedPreferences sharedData;
public Editor sharedEditor;
public SharedData(Context context) {
 this.sharedData = context.getSharedPreferences(APP_SHARED_PREFS,Activity.MODE_WORLD_READABLE|Activity.MODE_WORLD_WRITEABLE);
 this.sharedEditor = sharedData.edit();
}


public String getUserName() {
	return sharedData.getString("userName", "");
}

public void setUserName(String userName) {
	sharedEditor.putString("userName", userName);
	sharedEditor.commit();
}


public String getUserId() {
	return sharedData.getString("userId", "");
}

public void setUserId(String userId) {
	sharedEditor.putString("userId", userId);
	sharedEditor.commit();
}




}
