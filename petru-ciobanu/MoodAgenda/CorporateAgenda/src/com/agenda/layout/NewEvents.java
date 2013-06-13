package com.agenda.layout;

import java.util.Calendar;


import com.agenda.database.DatabaseConnector;
import com.agenda.utils.CalendarInterface.DateDialogFragmentListener;
import com.agenda.utils.CalendarInterface.TimeDialogFragmentListener;
import com.agenda.utils.DateDialogFragment;
import com.agenda.utils.TimeDialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

public class NewEvents extends Activity {
	DateDialogFragment dateDialog;
	TimeDialogFragment timeDialog;
	EditText eventTitle;
	EditText dateStart;
	EditText hourStart;
	EditText dateEnd;
	EditText hourEnd;
	Spinner eventPriorite;
	EditText eventDetails;
	Calendar newCalendar;
	Calendar mCalendar;
	int[] mToday;
	Context sharedContext = null;
	GridView mGridView;
	ListView dayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_new_events);
		newCalendar = Calendar.getInstance();
		eventTitle = (EditText) findViewById(R.id.txtEventTitle);
		dateStart = (EditText) findViewById(R.id.dateDebut);
		hourStart = (EditText) findViewById(R.id.hourDebut);
		dateEnd= (EditText) findViewById(R.id.dateFin);
		hourEnd = (EditText) findViewById(R.id.hourFin);
		eventDetails=(EditText)findViewById(R.id.txtDetails);
		eventPriorite=(Spinner)findViewById(R.id.spinner1);
		
		
		eventPriorite.getItemAtPosition(eventPriorite.getSelectedItemPosition()).toString();
		
		
		
		/*// get date
	     mCalendar = Calendar.getInstance();
		 int month = mCalendar.get(Calendar.MONTH); // zero based
		 int year = mCalendar.get(Calendar.YEAR);
         int week = mCalendar.get(Calendar.WEEK_OF_MONTH);
		
	

		// get display metrics
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);


		// set adapter
		//mGridView = (GridView)findViewById(R.id.gridview);
		//mGridView.setAdapter(new MonthAdapter(this, mToday[1], mToday[2], metrics));
		//mGridView.setAdapter(new MonthAdapter(this, month, year, metrics));
	 
		dayList = (ListView)findViewById(R.id.listaZile);
		TextView txtweek=(TextView)findViewById(R.id.weekNumber);
		txtweek.setText(String.valueOf(week));
		//mGridView.setAdapter(new MonthAdapter(this, mToday[1], mToday[2], metrics));
		dayList.setAdapter(new DayAdapter(this,month , year, metrics));
		*/
		
		
		
		
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
		
	}


	public void insertEvent(View view) {
		 try {
			sharedContext = this.createPackageContext("com.carrefour.services", Context.CONTEXT_INCLUDE_CODE);
			DatabaseConnector dbConnector = new DatabaseConnector(sharedContext);
			dbConnector.open();
			dbConnector.newEvents(eventTitle.getText().toString(), dateStart.getText().toString(), hourStart.getText().toString(),dateEnd.getText().toString(), hourEnd.getText().toString(), eventDetails.getText().toString(),String.valueOf(eventPriorite.getSelectedItemPosition()),"0");
			dbConnector.close();
			Toast.makeText(getApplicationContext(), "EventsInserted", Toast.LENGTH_SHORT).show();
		} catch (NameNotFoundException e) {
			Toast.makeText(getApplicationContext(), "ErrorOpenDataBase", Toast.LENGTH_SHORT).show();
		}		 
	}

	public void showTimePicker(final View v) {
		FragmentTransaction fts = getFragmentManager().beginTransaction(); 
		timeDialog = TimeDialogFragment.newInstance(this, new TimeDialogFragmentListener() {
			public void updateChangedTime(int hour, int minute) {
				if (v.getTag().toString().contains("hourStart")) {
					hourStart.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
					Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
				} else {
					hourEnd.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
				}
			}
		}, newCalendar);
		timeDialog.show(fts, "TimeDialogFragment");
	}

	
	public void showDatePicker(final View v) {
		FragmentTransaction fts = getFragmentManager().beginTransaction(); 
		dateDialog = DateDialogFragment.newInstance(this, new DateDialogFragmentListener() {
			public void updateChangedDate(int year, int month, int day) {
				if (v.getTag().toString().contains("dateStart")) {
					dateStart.setText(String.valueOf(month+1)+"-"+String.valueOf(day)+"-"+String.valueOf(year));
				} else {
					dateEnd.setText(String.valueOf(month+1)+"-"+String.valueOf(day)+"-"+String.valueOf(year));
				}
			}
		}, newCalendar);
		dateDialog.show(fts, "DateDialogFragment");
	}

	
	
	
	public void getBack(View v){
		Intent user = new Intent(NewEvents.this, UserActivity.class);
		startActivityForResult(user, 0);
	}
	
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.slide_out_left, R.anim.hold);
		super.onPause();
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
