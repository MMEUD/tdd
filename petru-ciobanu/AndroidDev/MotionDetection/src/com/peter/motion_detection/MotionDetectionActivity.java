package com.peter.motion_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.peter.motion_detection.R;
import com.peter.motion_detection.data.GlobalData;
import com.peter.motion_detection.data.Preferences;
import com.peter.motion_detection.detection.AggregateLumaMotionDetection;
import com.peter.motion_detection.detection.IMotionDetection;
import com.peter.motion_detection.detection.LumaMotionDetection;
import com.peter.motion_detection.detection.RgbMotionDetection;
import com.peter.motion_detection.image.ImageProcessing;
import com.peter.motion_detection.video.ImageClass;
import com.peter.motion_detection.video.TextClass;
import com.peter.motion_detection.video.VideoClass;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;

import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class extends Activity to handle a picture preview, process the frame
 * for motion, and then save the file to the SD card.
 * 
 */
public class MotionDetectionActivity extends SensorsActivity {

	private static final String TAG = "MotionDetectionActivity";
	private static SurfaceView preview = null;
	private static SurfaceHolder previewHolder = null;
	private static Camera mCamera = null;
	private static boolean inPreview = false;
	private static boolean inMotion = false;
	private static boolean secondPlaylist = false;
	private static boolean firstPlaylist = false;
	private static long mReferenceTime = 0;
	private static IMotionDetection detector = null;
	private static volatile AtomicBoolean processing = new AtomicBoolean(false);
	private CameraPreview mCameraPreview;
	private Document docXml = null;
	private NodeList nodeList;
	private static String playlistPath;
	private VideoClass video;
	private TextClass text;
	private ImageClass image;
	private LinearLayout containerElement;
	RelativeLayout containerVideo;
	private static TextView txtInfo;
	private static TextView txtInfoMotion;
	private static TextView txtMotion;
	private static TextView txtMotionPixel;
	private NextPosition nextTimer;
	private TimerMotion motionTimer;
	private int idx = 0;
	private int errorCount;
	private int countElements;
	private int screenHeight;
	private int screenWidth;
	private long timeElapsed;
    private int seconds=0;
    private String path;
	/**
	 * {@inheritDoc}
	 */

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.screen_base);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
		mCameraPreview = new CameraPreview(this, mCamera);
		previewHolder = mCameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		FrameLayout preview = new FrameLayout(this);
		preview.addView(mCameraPreview, new LayoutParams(1, 1));
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (Preferences.USE_RGB) {
			detector = new RgbMotionDetection();
		} else if (Preferences.USE_LUMA) {
			detector = new LumaMotionDetection();
		} else {
			// Using State based (aggregate map)
			detector = new AggregateLumaMotionDetection();
		}

		// MOTION DETECTION SENSOR
		containerVideo = new RelativeLayout(this);
		containerElement = new LinearLayout(this);
		containerElement.setPadding(400, 200, 0, 0);
		containerElement.addView(preview);
		containerVideo.addView(containerElement, 0);
		// TXT INFO SECTION
		txtInfo = new TextView(this);
		txtInfo.setText("Video:");
		txtInfo.setTextColor(Color.WHITE);
		txtInfo.setTextSize(16);
		txtInfo.setTypeface(null, Typeface.BOLD);
		txtInfo.setBackgroundColor(Color.BLACK);
		containerElement = new LinearLayout(this);
		containerElement.setPadding(650, 450, 0, 0);
		containerElement.addView(txtInfo);
		containerVideo.addView(containerElement, 1);

		// TXT INFO PLAYLIST STATUS
		txtInfoMotion = new TextView(this);
		txtInfoMotion.setText("Playlist:");
		txtInfoMotion.setTextColor(Color.RED);
		txtInfoMotion.setTextSize(16);
		txtInfoMotion.setTypeface(null, Typeface.BOLD);
		txtInfoMotion.setBackgroundColor(Color.BLACK);
		containerElement = new LinearLayout(this);
		containerElement.setPadding(0, 0, 0, 0);
		containerElement.addView(txtInfoMotion);
		containerVideo.addView(containerElement, 1);

		// TXT INFO MOTION SECTION
		txtMotion = new TextView(this);
		txtMotion.setText("MotionStatus :");
		txtMotion.setTextColor(Color.RED);
		txtMotion.setTextSize(16);
		txtMotion.setTypeface(null, Typeface.BOLD);
		txtMotion.setBackgroundColor(Color.BLACK);
		containerElement = new LinearLayout(this);
		containerElement.setPadding(0, 450, 0, 0);
		containerElement.addView(txtMotion);
		containerVideo.addView(containerElement, 1);
		
		
		 path=Environment.getExternalStorageDirectory().getPath();
		
		
		// TXT INFO MOTION PIXEL SECTION
				txtMotionPixel = new TextView(this);
				txtMotionPixel.setText("MotionStatus :"+path);
				txtMotionPixel.setTextColor(Color.RED);
				txtMotionPixel.setTextSize(20);
				txtMotionPixel.setTypeface(null, Typeface.BOLD);
				txtMotionPixel.setBackgroundColor(Color.BLACK);
				containerElement = new LinearLayout(this);
				containerElement.setPadding(225, 425, 0, 0);
				containerElement.addView(txtMotionPixel);
				containerVideo.addView(containerElement, 1);

		setContentView(containerVideo);
		readXml(path+"/data/playlist_static.xml", false);
		evaluateMotion(5000);
	}

	// READ XML PLAYLIST FROM DEVICE
	public void readXml(String xmlPath, boolean secondPlaylists) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		System.gc();
		System.gc();
		try {
			if (secondPlaylists == true) {
				xmlPath = path+"/data/playlist_detection.xml";
				firstPlaylist = false;
				secondPlaylist = true;
			} else {
				xmlPath = path+"/data/playlist_static.xml";
				firstPlaylist = true;
				secondPlaylist = false;
			}
			countElements = 0;
			DocumentBuilder db = dbf.newDocumentBuilder();
			docXml = db.parse(new FileInputStream(xmlPath));
		//	Log.i("VideoPlayer", "Load Playlist From Sdcard!");
			docXml.getDocumentElement().normalize();
		} catch (javax.xml.parsers.ParserConfigurationException pce) {
			Context context = getApplicationContext();
			CharSequence text = "NO CONNECTION !\n" + pce.getMessage();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		  //Log.i("VideoPlayer", pce.getMessage());
			errorCount++;
		} catch (java.io.IOException ie) {
			Context context = getApplicationContext();
			CharSequence text = "NO FILE FOUND!" + ie.getMessage();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		//	Log.i("VideoPlayer", ie.getMessage());
			errorCount++;
		} catch (org.xml.sax.SAXException se) {
			Context context = getApplicationContext();
			CharSequence text = "XML PARSE ERROR !\n" + se.getMessage();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		//	Log.i("VideoPlayer", se.getMessage());
			errorCount++;
		} catch (java.lang.IllegalArgumentException ae) {
			Context context = getApplicationContext();
			CharSequence text = "OTHER ERRORS !\n" + ae.getMessage();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			Log.i("VideoPlayer", ae.getMessage());
			errorCount++;
		}
		if (errorCount > 0) {
			Context context = getApplicationContext();
			CharSequence text = "ERROR!\n" + errorCount;
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		//	Log.i("VideoPlayer", "Number of Errors is =>\n" + errorCount);
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			setContentView(containerVideo);
			NodeList nodeList = docXml.getElementsByTagName("items");
			countElements = nodeList.getLength();
			evalElements();
			//Log.i("VideoPlayer", "PlayList Elements Count" + countElements);

		}
	}

	// EVALUATE MOTION DETECTION
	public void evaluateMotion(int time) {
		try {
			seconds=0;
			motionTimer.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			motionTimer = new TimerMotion(time, 1000);
			motionTimer.start();
		}

	}

	// EVALUATE ELEMENTS COUNT
	public void evalElements() {
		if (idx < countElements) {
			evalObject(idx);
		} else {
			if (idx == countElements) {
			//	Log.i("VideoPlayer", "End Play List Reading");
			}
			idx = 0;
			countElements = 0;
		}
	}

	private void evalObject(int nr) {
		NodeList nodes = docXml.getElementsByTagName("items");
		Element element = (Element) nodes.item(nr);
		String type = element.getAttribute("type").toString();
		try {
			if (type.equals("image")) {
				int width = (int) (screenWidth * Integer.parseInt(element.getAttribute("width").toString()) / 100);
				int height = (int) (screenHeight * Integer.parseInt(element.getAttribute("height").toString()) / 100);
				int x = (int) (screenWidth * Integer.parseInt(element.getAttribute("x").toString()) / 100);
				int y = (int) (screenHeight * Integer.parseInt(element.getAttribute("y").toString()) / 100);
				@SuppressWarnings("unused")
				int z = 0;// Integer.parseInt(element.getAttribute("z").toString());
				int delay = Integer.parseInt(element.getAttribute("delay").toString()) * 1000;
				String src = element.getAttribute("src").toString();
				image = new ImageClass(this, src, "sdcard", delay);
				containerElement = new LinearLayout(this);
				containerElement.setPadding(x, y, 0, 0);
				containerElement.addView(image, width, height);
				containerVideo.addView(containerElement);

				if (delay > 0) {
					nextTimer = new NextPosition(delay, 1000);
					nextTimer.start();
				} else {
					idx++;
					evalElements();
				}
			}

			if (type.equals("video")) {

				int width = (int) (screenWidth * Integer.parseInt(element.getAttribute("width").toString()) / 100);
				int height = (int) (screenHeight * Integer.parseInt(element.getAttribute("height").toString()) / 100);
				int x = (int) (screenWidth * Integer.parseInt(element.getAttribute("x").toString()) / 100);
				int y = (int) (screenHeight * Integer.parseInt(element.getAttribute("y").toString()) / 100);
				@SuppressWarnings("unused")
				int z = 0;// Integer.parseInt(element.getAttribute("z").toString());
				int delay = Integer.parseInt(element.getAttribute("delay").toString()) * 1000;
				String src =path+"/"+element.getAttribute("src").toString();
				video = new VideoClass(this, src, delay);
				containerElement = new LinearLayout(this);
				containerElement.setPadding(x, y, 0, 0);
				containerElement.addView(video, width, height);
				containerVideo.addView(containerElement, 1);
				txtInfo.setText(element.getAttribute("name").toString());
				if (delay > 0) {
					nextTimer = new NextPosition(delay, 1000);
					nextTimer.start();
				} else {
					// idx++;
					// evalElements(connectionType);
				}
				video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer arg0) {
						video.destroyDrawingCache();
						video.requestLayout();
						System.gc();
						System.gc();
						containerVideo.removeViewAt(1);
						idx++;
						evalElements();
					}
				});

				video.setOnErrorListener(new OnErrorListener() {
					public boolean onError(MediaPlayer mp, int what, int extra) {
						idx++;
						evalElements();
						android.os.Process.killProcess(android.os.Process.myPid());
						return true;
						// return false;
					}
				});

			}

			if (type.equals("text")) {

				int x = (int) (screenWidth * Integer.parseInt(element.getAttribute("x").toString()) / 10);
				int y = (int) (screenHeight * Integer.parseInt(element.getAttribute("y").toString()) / 10);
				int z = Integer.parseInt(element.getAttribute("z").toString());
				// int width =
				// Integer.parseInt(element.getAttribute("w").toString());
				// int height =
				// Integer.parseInt(element.getAttribute("h").toString());
				int delay = Integer.parseInt(element.getAttribute("delay").toString()) * 1000;
				int size = Integer.parseInt(element.getAttribute("size").toString());
				String src = element.getAttribute("src").toString();
				String color = element.getAttribute("color").toString();
				String background = element.getAttribute("background").toString();
				String fontFamily = element.getAttribute("font").toString();
				text = new TextClass(this, color, background, src, fontFamily, size, delay);
				containerElement = new LinearLayout(this);
				containerElement.setPadding(x, y, 0, 0);
				containerElement.addView(text);
				containerVideo.addView(containerElement, z);
				if (delay > 0) {
					nextTimer = new NextPosition(delay, 1000);
					nextTimer.start();
				} else {
					idx++;
					evalElements();
				}
			}
		} catch (java.lang.IllegalArgumentException ers) {
		//	Log.i("VideoPlayer", "Errors " + ers.getMessage());
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	class NextPosition extends CountDownTimer {

		public NextPosition(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		public void onFinish() {
			idx++;
			evalElements();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}

	}

	class TimerMotion extends CountDownTimer {

		public TimerMotion(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		public void onFinish() {
			if (RgbMotionDetection.movment == true) {
				txtMotion.setText("MotionStatus :" + RgbMotionDetection.movment);
				txtMotion.setTextColor(Color.RED);
			} else {
				txtMotion.setText("MotionStatus :" + RgbMotionDetection.movment);
				txtMotion.setTextColor(Color.GREEN);
			}
               //NO MOVMENT DETECTION > PLAY FIRST PLAYLISTS
			if (RgbMotionDetection.movment == false && firstPlaylist == true) {
				txtInfoMotion.setText("STATIC PLAYLIST PLAYING ");
				txtInfoMotion.setTextColor(Color.GREEN);
				evaluateMotion(5000);
			}
                //MOTION  DETECT  CHANGE  FROM PLAYLIST 1  TO PLAYLIST 2 
			if (RgbMotionDetection.movment == true && firstPlaylist == true) {
				txtInfoMotion.setText("CHANGE PLAYLIST FROM STATIC TO MOTION");
				txtInfoMotion.setTextColor(Color.GREEN);
				video.destroyDrawingCache();
				video.requestLayout();
				System.gc();
				System.gc();
				containerVideo.removeViewAt(1);
				idx = 0;
				countElements = 0;
				readXml(path+"/data/playlist_detection.xml", true);
				evaluateMotion(5000);
			}
                // MOTION  TRUE  AND PLAYLIST 2   PLAY PLAYLIST 2 
			if (RgbMotionDetection.movment == true && secondPlaylist == true) {
				txtInfoMotion.setText("MOTION PLAYLIST PLAYING");
				txtInfoMotion.setTextColor(Color.RED);
				evaluateMotion(5000);
			}
			    //MOTION  FALSE  CHANGE  FROM PLAYLIST 2  TO PLAYLIST 1  
			if (RgbMotionDetection.movment == false && secondPlaylist == true) {
				txtInfoMotion.setText("CHANGE PLAYLIST FROM MOTION TO STATIC");
				txtInfoMotion.setTextColor(Color.GREEN);
				video.destroyDrawingCache();
				video.requestLayout();
				System.gc();
				System.gc();
				containerVideo.removeViewAt(1);
				idx = 0;
				countElements = 0;
				readXml(path+"/data/playlist_static.xml", false);
				evaluateMotion(5000);
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			seconds++;
			Log.i(TAG, "RGB PIXEL DIFF  : "+RgbMotionDetection.pixtot + "  Seconds: "+ seconds);
			txtMotionPixel.setText("PixelDiff:"+RgbMotionDetection.pixtot);
		}
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	
	@Override
	public void onPause() {
		super.onPause();
		mCamera.setPreviewCallback(null);
		if (inPreview)
			mCamera.stopPreview();
		inPreview = false;
		mCamera.release();
		mCamera = null;
	}


	@Override
	public void onResume() {
		super.onResume();
		mCamera = Camera.open(1);
	}

	private PreviewCallback previewCallback = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera cam) {
			// FOR SENSOR
			if (data == null)
				return;
			Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null)
				return;
			if (!GlobalData.isPhoneInMotion()) {
				DetectionThread thread = new DetectionThread(data, size.width, size.height);
				thread.start();

			}
		}
	};

	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {

				mCamera.setPreviewDisplay(previewHolder);
				mCamera.setPreviewCallback(previewCallback);

			} catch (Throwable t) {
				Log.d("TAG", "Exception in setPreviewDisplay()", t);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Camera.Parameters parameters = mCamera.getParameters();
			Camera.Size size = getBestPreviewSize(width, height, parameters);
			if (size != null) {
				parameters.setPreviewSize(size.width, size.height);
				// parameters.setPreviewSize(800, 480);
				Log.i(TAG, "Using width=" + size.width + " height=" + size.height);
			}
			mCamera.setParameters(parameters);
			mCamera.startPreview();
			inPreview = true;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};

	private static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {

		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea)
						result = size;
				}
			}
		}
		return result;
	}

	private static final class DetectionThread extends Thread {

		private byte[] data;
		private int width;
		private int height;

		public DetectionThread(byte[] data, int width, int height) {
			this.data = data;
			this.width = width;
			this.height = height;

		}

		@Override
		public void run() {

			if (!processing.compareAndSet(false, true))
				return;
			// Log.i(TAG, "MOTION STATUS" + RgbMotionDetection.movment)
			try {
				// Previous frame
				int[] pre = null;
				if (Preferences.SAVE_PREVIOUS)
					pre = detector.getPrevious();
				// Current frame (with changes)
				// long bConversion = System.currentTimeMillis();
				int[] img = null;
				if (Preferences.USE_RGB) {
					img = ImageProcessing.decodeYUV420SPtoRGB(data, width, height);
				} else {
					img = ImageProcessing.decodeYUV420SPtoLuma(data, width, height);
				}
				// Current frame (without changes)
				int[] org = null;
				if (Preferences.SAVE_ORIGINAL && img != null)
					org = img.clone();

				if (img != null && detector.detect(img, width, height)) {
					long now = System.currentTimeMillis();
					if (now > (mReferenceTime + Preferences.PICTURE_DELAY)) {
						mReferenceTime = now;

						Bitmap previous = null;
						if (Preferences.SAVE_PREVIOUS && pre != null) {
							if (Preferences.USE_RGB)
								previous = ImageProcessing.rgbToBitmap(pre, width, height);
							else
								previous = ImageProcessing.lumaToGreyscale(pre, width, height);
						}

						Bitmap original = null;
						if (Preferences.SAVE_ORIGINAL && org != null) {
							if (Preferences.USE_RGB)
								original = ImageProcessing.rgbToBitmap(org, width, height);
							else
								original = ImageProcessing.lumaToGreyscale(org, width, height);
						}

						Bitmap bitmap = null;
						if (Preferences.SAVE_CHANGES && img != null) {
							if (Preferences.USE_RGB)
								bitmap = ImageProcessing.rgbToBitmap(img, width, height);
							else
								bitmap = ImageProcessing.lumaToGreyscale(img, width, height);
						}

						// Log.i(TAG, "MOTION DETECT");

						Looper.prepare();
						// new SavePhotoTask().execute(previous, original, bitmap);
					} else {
						// Log.i(TAG,
						// "Not taking picture because not enough time has passed since the creation of the Surface");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				processing.set(false);
			}
			// Log.d(TAG, "END PROCESSING...");

			processing.set(false);
		}
	};

	private static final class SavePhotoTask extends AsyncTask<Bitmap, Integer, Integer> {

		@Override
		protected Integer doInBackground(Bitmap... data) {
			for (int i = 0; i < data.length; i++) {
				Bitmap bitmap = data[i];
				String name = String.valueOf(System.currentTimeMillis());
				if (bitmap != null)
					save(name, bitmap);
			}
			return 1;
		}

		private void save(String name, Bitmap bitmap) {
			File photo = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
			if (photo.exists())
				photo.delete();

			try {
				FileOutputStream fos = new FileOutputStream(photo.getPath());
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
			} catch (java.io.IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}
		}
	}
}
