package com.mdm.crawl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CrawlSurface extends SurfaceView implements SurfaceHolder.Callback {
	public boolean surfaceCreated;
	private CrawlRunThread thread;
	private Crawl crawl;
	private Paint textPaint;
	private boolean isStopped = true;
	FileOutputStream stream;
	int pixelText;

	/** Variables for the counter */
	private int frameSamplesCollected = 0;
	private int frameSampleTime = 0;
	private long mLastTime;
	private int fps = 0;

	public CrawlSurface(Context context) {
		super(context);
		prepareSheep();
		this.setZOrderOnTop(true);
		getHolder().addCallback(this);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		surfaceCreated = false;
		setFocusable(false);
	}

	public void convertText(final String text) throws IOException {
		 textPaint = new Paint() {
			{
				setColor(Color.WHITE);
				setTextAlign(Paint.Align.LEFT);
				setTextSize(35f);
				setAntiAlias(true);
				setTypeface(Typeface.SANS_SERIF);
			}
		};
		pixelText = (int) textPaint.measureText(text.toUpperCase() + 0.5f);
		final Bitmap bmp = Bitmap.createBitmap(pixelText, 38,Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bmp);
		
		
		
		
		canvas.drawText(text.toUpperCase(), 0, 35, textPaint);
		stream = new FileOutputStream("/sdcard/text.jpg"); 
		bmp.compress(CompressFormat.JPEG, 100, stream);
		bmp.recycle();
		stream.close();
       
	}

	private void prepareSheep() {
		crawl = new Crawl();
		List<Bitmap> lstFrames = new ArrayList<Bitmap>();
		lstFrames.add(BitmapFactory.decodeFile("/sdcard/text.jpg"));
		lstFrames.add(BitmapFactory.decodeFile("/sdcard/text.jpg"));
		crawl.setLstFrames(lstFrames);
		crawl.setSpeed(3);
		crawl.setX(800);
		crawl.setY(0);
	}

	// CALCULATE NEXT POSITION
	public void updatePhysics(int poz) {
		isStopped = false;
		crawl.setX(crawl.getX() - crawl.getSpeed());
		long now = System.currentTimeMillis();
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
		
		
		
		
		
		canvas.drawText(fps + " fps", 300, crawl.getY() + 50, textPaintSmall);
		canvas.drawText("ScrollPosition" + "/"+crawl.getX()+"//"+pixelText, 50, crawl.getY() + 50,textPaintSmall);
		if (isStopped) {
			canvas.drawBitmap(crawl.getLstFrames().get(0), crawl.getX(),crawl.getY(), null);	
			updatePhysics(50);
		} else {
			if (SystemClock.elapsedRealtime() % 2 == 0) {
				canvas.drawBitmap(crawl.getLstFrames().get(0), crawl.getX(),crawl.getY(), null);
				if (Math.abs(crawl.getX())>=pixelText){
					crawl.setX(0);
					updatePhysics(50);
				}

			} else {
				canvas.drawBitmap(crawl.getLstFrames().get(1), crawl.getX(),crawl.getY(), null);			
				if (Math.abs(crawl.getX())>=pixelText){
					crawl.setX(0);
					updatePhysics(50);
				}

			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (surfaceCreated == false) {
			createThread();
			surfaceCreated = true;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		surfaceCreated = false;
	}

	public void createThread() {
		thread = new CrawlRunThread(this);
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
