package com.peter.motion_detection.video;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoClass  extends VideoView{

	public VideoClass(Context context, AttributeSet attrs) {
		super(context, attrs);
		/// TODO Auto-generated constructor stub
		
	}
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
		int width = getDefaultSize(0, widthMeasureSpec); 
		int height = getDefaultSize(0, heightMeasureSpec); 
		setMeasuredDimension(width, height); 
	} 
	public VideoClass(Context context,String src,int dellay) {
		super(context);
    	 Uri uri = Uri.parse(src);  
		// MediaController mc = new MediaController(this.getContext());  
	    // this.setMediaController(mc);    	    
	     this.setVideoPath(src);    
	     
	     this.start();       
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
           VideoClass.this.stopPlayback();
           VideoClass.this.destroyDrawingCache();
           VideoClass.this.requestLayout(); 
           
           System.gc();
          }
    	 
     	@Override
     	public void onTick(long millisUntilFinished) {
         }
}
		
}
