package com.agenda.layout;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TodayActivity extends Activity {
    ListView listview;
    LinearLayout container;
    View view ;
    ImageView btnBack;
    TextView txtHeader;
    Context sharedContext = null;
    
      
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_events);
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
		
		
	/*	
		btnBack = (ImageView)findViewById(R.id.btnBackHeader);		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent user = new Intent(TodayActivity.this, UserActivity.class);
				startActivityForResult(user, 0);
				
			}
		});*/
		
		 
		  
	}
  @Override
  protected void onPause() {
	  overridePendingTransition(R.anim.slide_out_left, R.anim.hold);
      super.onPause();
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          moveTaskToBack(true);
          return false;
      }
      return super.onKeyDown(keyCode, event);
  }
  
  
}

