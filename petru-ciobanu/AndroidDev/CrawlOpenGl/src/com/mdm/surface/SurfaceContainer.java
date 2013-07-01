package com.mdm.surface;

import java.io.FileOutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;


public class SurfaceContainer  extends SurfaceView implements SurfaceHolder.Callback {
	public boolean surfaceCreated;
	private SurfaceRunThread thread;
	private Surface surface;
	private Paint textPaint;
	private boolean isStopped = false;
	FileOutputStream stream;
	
	int pixelText;

	/** Variables for the counter */
	private int frameSamplesCollected = 0;
	private int frameSampleTime = 0;
	private long mLastTime;
	private int fps = 0;
	private TweenManager tweenManager ; 
	public SurfaceContainer(Context context) {
		super(context);
		prepareData();
		getHolder().addCallback(this);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		surfaceCreated = false;
		setFocusable(false);
		this.setZOrderOnTop(true);
		tweenManager = new TweenManager();
		Tween.registerAccessor(Surface.class, new SpriteAccessor());
		Tween.call(windCallback).start(tweenManager); 
	}

    public void prepareData(){
    	
    	surface = new Surface();
    	final Bitmap bmp = BitmapFactory.decodeFile("/sdcard/poza.jpg");
		Bitmap mBitmap = Bitmap.createScaledBitmap(bmp,125, 125, false);
    	surface.setBitmap(mBitmap);
    	
    }

    public void updatePhysics(int poz) {
    
    	long now = System.currentTimeMillis();
		tweenManager.update(SystemClock.elapsedRealtime()%2); 
		if (mLastTime != 0) {
			// Time difference between now and last time we were here
			int time = (int) (now - mLastTime);
			frameSampleTime += time;
			frameSamplesCollected++;
			// After 10 frames
			if (frameSamplesCollected == 10) {
				// Update the fps variable
				fps = (int) (10000 / frameSampleTime);
				// Reset the sampletime + frames collected
				frameSampleTime = 0;
				frameSamplesCollected = 0;
			}
		}
		mLastTime = now;
    }

    @Override
	public void onDraw(Canvas canvas) {
    	 canvas.drawColor(0, PorterDuff.Mode.CLEAR); 
		//canvas.drawColor(Color.argb(255, 69, 139, 00));
    	
		
	     final Paint textPaintSmall = new Paint() {
				{
					setColor(Color.RED);
					setTextAlign(Paint.Align.LEFT);
					setTextSize(15f);
					setAntiAlias(true);
					setTypeface(Typeface.SANS_SERIF);
				}
			};
			
		canvas.drawText(" Animation FPS:"+fps, 300,15, textPaintSmall);	
		canvas.drawBitmap(surface.getBitmap(), surface.getX(),surface.getY(), null);	
		}
	
    
    private final TweenCallback windCallback = new TweenCallback() {        
		public void onEvent(int type, BaseTween<?> source) {  
			Tween.to(surface,SpriteAccessor.POS_XY, 50).target(500,250).ease(Elastic.INOUT).delay(50).repeatYoyo(5, 0.5f).setCallback(windCallback).start(tweenManager);	 
			//Log.i(getClass().getSimpleName(), "Surface Position " + surface.getX());
			
		}
	};
    

public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
	// TODO Auto-generated method stub
	
}

public void surfaceCreated(SurfaceHolder holder) {
	// TODO Auto-generated method stub
	if (surfaceCreated == false) {
		createThread();
		surfaceCreated = true;
	}
}

public void surfaceDestroyed(SurfaceHolder holder) {
	// TODO Auto-generated method stub
	surfaceCreated = false;
}


public void createThread() {
	thread = new SurfaceRunThread(this);
	thread.setRunning(true);
	thread.start();
}

public void terminateThread() {
	boolean retry = true;
	thread.setRunning(false);
	while (retry) {
		try {
			thread.join();
			retry = false;
		} catch (InterruptedException e) {
			// we will try it again and again...
		}
	}
}
}







