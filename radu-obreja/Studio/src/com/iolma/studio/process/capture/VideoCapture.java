package com.iolma.studio.process.capture;

import java.util.List;

import org.bridj.Pointer;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.iolma.studio.process.capture.video.Device;
import com.iolma.studio.process.capture.video.OpenIMAJGrabber;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;

public class VideoCapture extends BasicProcess {
	
	private int width = 640;
	private int height = 480;
	private int fps = 25;
	
	private OpenIMAJGrabber grabber = new OpenIMAJGrabber();
	private List<Device> devices = grabber.getVideoDevices().get().asArrayList();
	private Device selectedDevice = null; 

	public VideoCapture() {		
	}

	public VideoCapture(int width, int height, int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String geDevicesAsString() {
		StringBuffer sb = new StringBuffer();
		for(Device d : devices) {
			sb.append(d.getIdentifierStr() + " -> " + d.getNameStr() + "\n");
		}
		logger.info(sb.toString());
		return sb.toString();
	}
	
	public List<Device> getDevices() {
		return devices;
	}
	
	public boolean setDevice(String deviceID) {
		boolean result = false; 
		for(Device d : devices) {
			if (deviceID.equals(d.getIdentifierStr())) {
				selectedDevice = d;
				result = true;
				logger.info(processName + " : using device " + d.getNameStr());
				break;
			}
		}
		return result;
	}
	
	public void startup() {
		super.startup();
		if (selectedDevice != null) {
			grabber.startSession(width, height, fps, Pointer.pointerTo(selectedDevice));
			width = grabber.getWidth();
			height = grabber.getHeight();
		}
	}

	public void shutdown() {
		super.shutdown();
		if (selectedDevice != null) {
			grabber.stopSession();
		}
	}
	
	public IFrame execute(IFrame frame) {
		try {
			grabber.nextFrame();
			final Pointer<Byte> data = grabber.getImage();
			if (data != null) {
				IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.RGB24, width, height);
				picture.put(data.getBytes(width * height * 3), 0, 0, width * height * 3);
				picture.setComplete(true, picture.getPixelType(), width, height, System.currentTimeMillis());
				frame.setVideoPicture(picture);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return frame;
	}

}
