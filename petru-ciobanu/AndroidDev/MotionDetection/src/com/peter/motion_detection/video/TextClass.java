package com.peter.motion_detection.video;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.widget.TextView;

public class TextClass  extends TextView{

	public TextClass(Context context ,String color ,String background ,String text ,String fontFamily ,int size , int dellay) {
		super(context);
		// TODO Auto-generated constructor stub
	  //  Typeface font = Typeface.createFromFile("/sdcard/assets/fonts/"+fontFamily+".ttf");  
	 
		this.setTextColor(Color.parseColor(color));
		this.bringToFront();
	    
		if (background !="0"){
			this.setBackgroundColor(Color.parseColor(background));	
		}
		this.setTextSize(size);
		this.setText(text);
	//	this.setTypeface(font);
		if(dellay > 0){	
		CounterText timer=new CounterText(dellay,1000);
	    timer.start();	
	    }
	}

	public class CounterText extends CountDownTimer{
     	public CounterText(long millisInFuture, long countDownInterval) {
     	super(millisInFuture, countDownInterval);
     	     }    	 
     	@Override
     	public void onFinish() {
      TextClass.this.setText(null);
      TextClass.this.refreshDrawableState();
      System.gc();
     	}
    	 
     	@Override
     	public void onTick(long millisUntilFinished) {
         }
}

}
