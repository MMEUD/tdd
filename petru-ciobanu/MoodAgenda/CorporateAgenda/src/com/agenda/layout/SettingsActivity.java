package com.agenda.layout;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity  extends Activity {
    ListView listview;
    LinearLayout container;
    View view ;
    ImageView btnBack;
    TextView txtHeader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_settings);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		
	
		
	/*	btnBack = (ImageView)findViewById(R.id.btnBackHeader);	
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent signIn = new Intent(SettingsActivity.this, UserActivity.class);
				startActivityForResult(signIn, 0);
			}
		});*/
	}
    
  @Override
  protected void onPause() {
      overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
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
