package com.agenda.layout;
import java.util.ArrayList;
import com.agenda.database.DatabaseConnector;
import com.agenda.utils.CalendarDay;
import com.agenda.utils.CursorTodayEvents;
import com.agenda.utils.CustomAdapter;



import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EventsDetail extends Activity {
	Context sharedContext = null;
	DatabaseConnector dbConnector ;
	CalendarDay dateUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_today_events);
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
		try {
			sharedContext = this.createPackageContext("com.carrefour.services", Context.CONTEXT_INCLUDE_CODE);	
		} catch (NameNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Error" +e, Toast.LENGTH_SHORT).show();
		}
		dateUtils = new CalendarDay();
		TextView txtdayName=(TextView)findViewById(R.id.events_dayName);
		txtdayName.setText(dateUtils.currentDay().get(1).toString());
		TextView txtdayNumber=(TextView)findViewById(R.id.events_dayNumber);
		txtdayNumber.setText(dateUtils.currentDay().get(2).toString());
		TextView txtmonthName=(TextView)findViewById(R.id.events_monthName);
		txtmonthName.setText(dateUtils.currentDay().get(0).toString());
		dbConnector = new DatabaseConnector(sharedContext);
		dbConnector.open();
		Cursor cursor =	dbConnector.todayEvents();
		dbConnector.close(); 	
		ListView listView = (ListView) findViewById(R.id.listEvent);   
		listView.setAdapter(new CursorTodayEvents(this,cursor));   
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				String eventId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
				Toast.makeText(getApplicationContext(), eventId, Toast.LENGTH_SHORT).show();
				DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
				dbConnector.open();
				dbConnector.deleteEvents(Integer.parseInt( eventId));
				dbConnector.close();
			}
		});
		//TextView txtHeader=(TextView)findViewById(R.id.txtHeaderAgenda);
		//Typeface tf = Typeface.createFromAsset(getAssets(),"Omnes_Medium.otf");
		//txtHeader.setTypeface(tf,Typeface.BOLD);
    	//txtHeader.setText(dateUtils.currentDay().get(1).toString());
		
		
		
	}

	
	
	 
	 
	public void getBack(View v){
		Intent user = new Intent(EventsDetail.this, UserActivity.class);
		startActivityForResult(user, 0);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
}
