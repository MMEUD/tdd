package com.agenda.layout;




import com.agenda.utils.SharedData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class UserActivity extends Activity {
	protected SharedData sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = new SharedData(getApplicationContext());
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
		setContentView(R.layout.agenda_user);
	//	Intent i = new Intent();
	//	i.setClassName("com.carrefour.service", "com.carrefour.service.NotificationService");
	//	stopService(i);
	//	ScannerTimer scannerTimer = new ScannerTimer(5000, 1000);
	//	scannerTimer.start();

	}

	
	public void newEvents(View v){
	 Intent notification = new Intent(UserActivity.this,NewEvents.class);
	 startActivityForResult(notification, 0);
	}
	
	
	public void btnTodayClick(View v) {
		//this.overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
		Intent notification = new Intent(UserActivity.this, EventsDetail.class);
		startActivityForResult(notification, 0);
	}

	// Initiating Menu XML file (menu.xml)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_logout:
			sharedPrefs.setUserName("");
			Intent i = new Intent();
			i.setClassName("com.carrefour.service", "com.carrefour.service.NotificationService");
			stopService(i);
			startService(i);
			android.os.Process.killProcess(android.os.Process.myPid());
			finish();
			
			return true;
		case R.id.menu_settings:
			Intent settings = new Intent(UserActivity.this, SettingsActivity.class);
			settings.putExtra("idUser", sharedPrefs.getUserName());
			startActivityForResult(settings, 0);
			return true;

		case R.id.menu_search:
			Toast.makeText(this, "Search is Selected", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		//overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
		super.onPause();
	}

	// TIMER TO RESCAN SOURCE
	class ScannerTimer extends CountDownTimer {
		public ScannerTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			System.gc();
			System.gc();
			Intent i = new Intent();
			i.setClassName("com.carrefour.service", "com.carrefour.service.NotificationService");
			startService(i);
		}

		public void onTick(long millisUntilFinished) {
		}
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
