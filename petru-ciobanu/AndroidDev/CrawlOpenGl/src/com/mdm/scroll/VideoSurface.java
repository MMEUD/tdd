package com.mdm.scroll;

import java.io.IOException;

import com.mdm.crawl.CrawlSurface;

import com.mdm.surface.SurfaceContainer;
import com.mdm.tween.ViewContainer;
import com.mdm.tween.ViewContainerAccessor;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.effect.Effect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

public class VideoSurface extends Activity implements SurfaceHolder.Callback {
	RelativeLayout containerBase;
	RelativeLayout containerElement;
	MediaPlayer mediaPlayer;
	private int width = 0;
	private int height = 0;
	int screenHeight;
	int screenWidth;
	CrawlSurface crawlSurface;
	SurfaceContainer surfaceAnimation;
	SurfaceView videoView;
	SurfaceHolder holderVideo;
	DigitalClock clock;
	ImageClass image;
	boolean pausing = false;
	
	int idx = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = (dm.widthPixels);
		screenHeight = (dm.heightPixels);

		
		
		containerBase = new RelativeLayout(this);
		// containerBase.setBackgroundColor(Color.parseColor("#6495ED"));
	
		//FAKE INDEX
		while (idx <20) {
			ImageView imag = new ImageView(this);
			imag.setImageResource(R.drawable.pixel);
			containerElement = new RelativeLayout(this);
			containerElement.setPadding(screenWidth-15, 0, 0, 0);
			containerElement.addView(imag, 10, 10);
			containerBase.addView(containerElement, idx);
			idx++;
		}

		//SURFACE ANIMATION 
		
		containerElement = new RelativeLayout(this);
		containerElement.setPadding(0, 0, 0, 0);
		surfaceAnimation = new SurfaceContainer(this);
	    containerElement.addView(surfaceAnimation, screenWidth, screenHeight);
		containerBase.addView(containerElement);
		
		
		
		// CRAWL
		containerElement = new RelativeLayout(this);
		containerElement.setPadding(0, screenHeight-60, 0, 0);
		crawlSurface = new CrawlSurface(this);
		containerElement.addView(crawlSurface, screenWidth, 60);
	//	containerBase.addView(containerElement);

		// VIDEO
		videoView = (SurfaceView) new SurfaceView(this);
		holderVideo = videoView.getHolder();
		holderVideo.addCallback(this);
		containerElement = new RelativeLayout(this);
		containerElement.setPadding(0, 0, 0, 0);
		containerElement.addView(videoView, screenWidth, screenHeight);
		containerBase.addView(containerElement);

		Button buttonPlayVideo = (Button) new Button(this);
		buttonPlayVideo.setWidth(125);
		buttonPlayVideo.setText("PLAY");
		buttonPlayVideo.setId(666);
		containerElement = new RelativeLayout(this);
		containerElement.setPadding(0, 0, 0, 0);
		containerElement.addView(buttonPlayVideo);
		containerBase.addView(containerElement, 2);

	

		// IMAGE
		image = (ImageClass) new ImageClass(this, "/sdcard/poza.jpg","sdcard", 0);
		containerElement = new RelativeLayout(this);
		containerElement.setPadding(0,0, 0, 0);	
		containerElement.setId(100);
		containerElement.addView(image,screenWidth,screenHeight);
		//containerBase.addView(containerElement, 5);

		setContentView(containerBase);
	//	createCrawl();
		

		mediaPlayer = new MediaPlayer();
		

		buttonPlayVideo.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				pausing = false;
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.reset();
				}
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDisplay(holderVideo);
				try {
					mediaPlayer.setDataSource("/sdcard/video/movie1080.mp4");
					mediaPlayer.prepare();

					// mediaPlayer.prepareAsync();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				mediaPlayer.start();

			}
		});

		

		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);

			}
		});

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						width = mediaPlayer.getVideoWidth();
						height = mediaPlayer.getVideoHeight();

					}
				});

	}

	public void createCrawl() {

		try {
			crawlSurface.convertText("You can create a Bitmap of the appropriate size, create a Canvas for the Bitmap, and then draw your text into it. You can use a Paint object to measure the text so you'll know the size needed for the bitmap. You can do something like this (untested):");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(getClass().getSimpleName(), "Convert Text " + e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		 mediaPlayer.stop();
		// mediaPlayer.release();
		Log.i(getClass().getSimpleName(), "DestroyActivities");
		crawlSurface.terminateThread();
		surfaceAnimation.terminateThread();
		System.gc();
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
	//	crawlSurface.terminateThread();
		surfaceAnimation.terminateThread();
		System.gc();
		System.gc();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*if (crawlSurface.surfaceCreated == true) {
			crawlSurface.createThread();
		}*/
		
		
		if (surfaceAnimation.surfaceCreated == true) {
			surfaceAnimation.createThread();
		}
		
		
		
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




}