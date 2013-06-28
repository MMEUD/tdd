package com.mdm.scroll;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
public class ImageClass extends ImageView{
	Drawable drawable ;
	InputStream inputStream;
	public ImageClass(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	    
	public ImageClass(Context context ,String source, String type,int dellay){
		super(context);
		Drawable drawable = loadImage(source, type);
		this.setScaleType(ScaleType.FIT_XY);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setImageDrawable(drawable);
		
		if(dellay==0){	
	    }else{
	    	CounterContainer timer=new CounterContainer(dellay,1000);
	    	timer.start();
	    }
	}
	
	public class CounterContainer extends CountDownTimer{
     	public CounterContainer(long millisInFuture, long countDownInterval) {
     	super(millisInFuture, countDownInterval);
     	     }    	 
     	@Override
     	public void onFinish() {
           ImageClass.this.setImageDrawable(null);
           System.gc();
          }
    	 
     	@Override
     	public void onTick(long millisUntilFinished) {
         }
}
	
	
	
	private Drawable loadImage(String source, String type) {
		try {
			if (type.equals("sdcard")) {
				inputStream = null;
				inputStream = (InputStream) new FileInputStream(source);
				//Log.i(getClass().getSimpleName(), "LOAD IMAGE!!!"+source);
			}
			if (type.equals("http_server")) {
				inputStream = null;
				inputStream = (InputStream) new URL(source).getContent();
			}
			Drawable drawable = Drawable.createFromStream(inputStream,	"src name");
			return drawable;
		} catch (Exception e) {
			return null;
		}
	}
 
}
