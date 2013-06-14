package com.agenda.layout;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationView  extends Activity {
	ImageView btnBack;
	TextView txtHeader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.dock_right_enter, R.anim.hold);
	//	setContentView(R.layout.agenda_notification);
		
	/*	txtHeader=(TextView)findViewById(R.id.txtHeaderAgenda);
		Typeface tf = Typeface.createFromAsset(getAssets(),"Omnes_Medium.otf");
		txtHeader.setTypeface(tf,Typeface.BOLD);
		txtHeader.setText("Agenda Notification");*/
		
	/*	btnBack = (ImageView)findViewById(R.id.btnBackHeader);	
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent signIn = new Intent(NotificationView.this, UserActivity.class);
				startActivityForResult(signIn, 0);
			}
		});*/
	}

  @Override
  protected void onPause() {
      // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
      // Override how this activity is animated out of view
      // The new activity is kept still and this activity is pushed out to the left
      overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
      super.onPause();
  }
}
