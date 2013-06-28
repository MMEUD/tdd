package com.mdm.scroll;

import com.mdm.surface.SurfaceContainer;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class StartActivity extends Activity implements SurfaceHolder.Callback{
	int screenHeight;
	int screenWidth;
	SurfaceContainer surface ;
	ImageClass image ;
	
	RelativeLayout containerBase;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = (dm.widthPixels);
		screenHeight = (dm.heightPixels);
		surface = new SurfaceContainer(this);
		//ADD IMAGE 
		// IMAGE
	
		//surface.getHolder().setFormat(PixelFormat.RGBA_8888);
		containerBase = new RelativeLayout(this);
		
		image = (ImageClass) new ImageClass(this, "/sdcard/poza.jpg","sdcard", 0);
	//	
		containerBase.addView(image);
		containerBase.addView(surface);
		
			
		setContentView(containerBase);

	}
	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(getClass().getSimpleName(), "DestroyActivities");
		surface.terminateThread();
		System.gc();
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
		surface.terminateThread();
		System.gc();
		System.gc();
	}
}
