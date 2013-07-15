package com.iolma.studio.application.modules.component;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.iolma.studio.application.module.IVideoComponent;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.IProcess;
import com.iolma.studio.process.IServer;
import com.iolma.studio.process.capture.VideoCapture;
import com.iolma.studio.process.capture.video.Device;
import com.iolma.studio.process.play.VideoPlay;

public class VideoCaptureComponent implements IServer, IVideoComponent, ChangeListener<Boolean> {

	private static final String LABEL = "_label";
	private static final String DISPLAY = "_display";
	private static final String FULLSCREEN = "_fullscreen";
	private static final String VISIBLE = "_visible";
	private static final String ENABLE = "_enable";
	
	private Stage stage = null;
	private ILogger logger = null;
	private Device device = null;
	private String fxPrefix = null;
	private int width = 640;
	private int height = 480;
	private int fps = 25;
	
	private boolean enable = false;
	private boolean visible = false;
	private boolean fullscreen = false;
	private Label label = null;
	private ImageView imageView = null;
	private double imageWidth = 0;
	private double imageHeight = 0;
	private double imageLayoutX = 0;
	private double imageLayoutY = 0;
	private boolean imageFullscreen = false;

	
	private IProcess statistics = null;
	private IProcess videoInput = null;
	private VideoCapture videoCapture = null;
	private VideoPlay videoPlay = null;
	
	private HashMap<String, Node> nodes = new HashMap<String, Node>();
	 
	
	public VideoCaptureComponent(Stage stage, IProcess statistics, ILogger logger, Device device, String fxPrefix, int width, int height, int fps) {
		this.stage= stage;
		this.statistics = statistics;
		this.logger = logger;
		this.device = device;
		this.fxPrefix = fxPrefix + device.getIdentifierStr();
		this.width = width;
		this.height = height;
		this.fps = fps;
	}

	public VideoCaptureComponent(Stage stage, ILogger logger, int deviceId, String fxPrefix, int width, int height, int fps) {
		this.stage= stage; 
		this.fxPrefix = fxPrefix + deviceId;
		this.width = width;
		this.height = height;
		this.fps = fps;
	}

	public void startup() {
		if (device != null) {
			logger.info("VideoCapture : " + device.getNameStr());
		}
		detectControl(ENABLE);
		detectControl(VISIBLE);
		detectControl(FULLSCREEN);
		detectImageView(DISPLAY);
		detectLabel(LABEL);
		stage.fullScreenProperty().addListener(this);
	}

	public void shutdown() {
		stage.fullScreenProperty().removeListener(this);
		visible = false;
		onChangeVisible();
		enable = false;
		onChangeEnabled();
	}

	public void setVideoEnabled(boolean enable) {
		if (this.enable != enable) {
			onChange(ENABLE);
		}
	}

	public boolean isVideoEnabled() {
		return enable;
	}

	public void setVideoVisible(boolean visible) {
		if (this.visible != visible) {
			onChange(VISIBLE);
		}
	}

	public boolean isVideoVisible() {
		return visible;
	}

	public void setVideoFullscreen(boolean fullscreen) {
		if (this.fullscreen != fullscreen) {
			onChange(FULLSCREEN);
		}
	}

	public boolean isVideoFullscreen() {
		return fullscreen;
	}

	public void addVideoInput(IProcess input) {
		videoInput = input;
	}

	public void removeVideoInput(IProcess input) {
		videoInput = null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void detectControl(final String controlName) {
		Node control = stage.getScene().lookup(fxPrefix + controlName);
		if (control != null) {
			if (device == null) {
				control.setDisable(true);
				control.setVisible(false);
			} else {
				if (control instanceof Button) {
					Button button = (Button)control;
					button.setOnAction(new EventHandler() {
						public void handle(final Event arg0) {
							Platform.runLater(new Runnable() {
								public void run() {
									onChange(controlName);
								}
							});
						}
					});
					nodes.put(controlName, button);
				}
				if (control instanceof CheckBox) {
					CheckBox button = (CheckBox)control;
					button.setOnAction(new EventHandler() {
						public void handle(final Event arg0) {
							Platform.runLater(new Runnable() {
								public void run() {
									onChange(controlName);
								}
							});
						}
					});
					nodes.put(controlName, button);
				}
				if (control instanceof ToggleButton) {
					ToggleButton button = (ToggleButton)control;
					button.setOnAction(new EventHandler() {
						public void handle(final Event arg0) {
							Platform.runLater(new Runnable() {
								public void run() {
									onChange(controlName);
								}
							});
						}
					});
					nodes.put(controlName, button);
				}
				if (control instanceof Hyperlink) {
					Hyperlink button = (Hyperlink)control;
					button.setOnAction(new EventHandler() {
						public void handle(final Event arg0) {
							Platform.runLater(new Runnable() {
								public void run() {
									onChange(controlName);
								}
							});
						}
					});
					nodes.put(controlName, button);
				}
			}
		}				
	}
	
	private void detectImageView(String controlName) {
		Node control = stage.getScene().lookup(fxPrefix + controlName);
		if (control != null) {
			if (device == null) {
				control.setDisable(true);
				control.setVisible(false);
			} else {
				if (control instanceof ImageView) {
					imageView = (ImageView)control;
				}
			}
		}				
	}

	private void detectLabel(String controlName) {
		Node control = stage.getScene().lookup(fxPrefix + controlName);
		if (control != null) {
			if (device == null) {
				control.setDisable(true);
				control.setVisible(false);
			} else {
				if (control instanceof Label) {
					label = (Label)control;
					label.setText(device.getNameStr());
				}
			}
		}				
	}

	private synchronized void onChange(String action) {
		if (ENABLE.equals(action)) {
			enable = !enable;
			onChangeEnabled();
		}
		if (VISIBLE.equals(action)) {
			visible = !visible;
			onChangeVisible();
		}
		if (FULLSCREEN.equals(action)) {
			fullscreen = !fullscreen;
			onChangeFullscreen();
		}
	}

	private void onChangeEnabled() {
		if (visible) {
			visible = !visible;
			onChangeVisible();
		}
		if (enable) {
			if ((videoCapture == null || !videoCapture.isStarted()) && videoInput != null && videoInput.isStarted()) {
				videoCapture = new VideoCapture(width, height, fps);
				videoCapture.setDevice(device.getIdentifierStr());
				videoCapture.setProcessName(fxPrefix + "VideoCapture");
				videoCapture.addInput(videoInput);
				videoCapture.startup();
				statistics.addInput(videoCapture);
			} else {
				enable = false;
			}			
		} else {
			if (videoCapture != null && videoCapture.isStarted()) {
				statistics.removeInput(videoCapture);
				videoCapture.removeInput(videoInput);
				videoCapture.shutdown();
				videoCapture = null;
			}
		}
	}

	private void onChangeVisible() {
		if (visible) {
			if ((videoPlay == null || !videoPlay.isStarted()) && videoCapture != null && videoCapture.isStarted()) {
				videoPlay = new VideoPlay(imageView);
				videoPlay.setProcessName(fxPrefix + "VideoPlay");
				videoPlay.addInput(videoCapture);
				videoPlay.startup();
				statistics.addInput(videoPlay);
			} else {
				visible = false;
			}
		} else {
			if (videoPlay != null && videoPlay.isStarted()) {
				statistics.removeInput(videoPlay);
				videoPlay.removeInput(videoCapture);
				videoPlay.shutdown();
				videoPlay = null;
			}
		}		
	}

	private void onChangeFullscreen() {
		imageFullscreen = true;
		stage.setFullScreen(fullscreen);
	}

	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		fullscreen = newValue.booleanValue();
		if (imageFullscreen) {
	        if (newValue.booleanValue() == Boolean.FALSE) {
	        	imageView.setFitWidth(imageWidth);
	        	imageView.setFitHeight(imageHeight);
	        	imageView.setLayoutX(imageLayoutX);
	        	imageView.setLayoutY(imageLayoutY);
	        	imageFullscreen = false;
	        } else {
				imageWidth = imageView.getFitWidth();
				imageHeight = imageView.getFitHeight();
				imageLayoutX = imageView.getLayoutX();
				imageLayoutY = imageView.getLayoutY();
	        	imageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
	        	imageView.setFitHeight(Screen.getPrimary().getBounds().getHeight());
	        	imageView.setLayoutX(0);
	        	imageView.setLayoutY(0);
	        }				
		}
	}

}
