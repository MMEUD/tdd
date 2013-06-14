package com.iolma.studio.process.record;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;

public class MP4Recorder extends BasicProcess {

	private IMediaWriter writer = null;
	private long start = 0;
	private long keyFrameCounter = 0;
	
	public void startup() {
		super.startup();
		start = System.nanoTime() / 1000;
	}

	public void shutdown() {
		super.shutdown();
		writer.flush();
		writer.close();
	}
	
	public MP4Recorder(String fileName, int width, int height) {
		writer = ToolFactory.makeWriter(fileName);
		writer.addAudioStream(0, 0, 1, 48000);
		writer.addVideoStream(1, 0, ICodec.ID.CODEC_ID_MPEG4, width, height);
	}
	
	public synchronized IFrame execute(IFrame frame) {
		IAudioSamples audioSamples = frame.getAudioSamples();
		IVideoPicture picture = frame.getVideoPicture();
		if (audioSamples != null) {
			audioSamples.setTimeStamp(System.nanoTime()/1000 - start);
			writer.encodeAudio(0, audioSamples);
		}
		if (picture != null) {
			IVideoResampler resampler = IVideoResampler.make(picture.getWidth(), picture.getHeight(), IPixelFormat.Type.YUV420P, picture.getWidth(), picture.getHeight(), IPixelFormat.Type.RGB24); 
			IVideoPicture convertedFrame = IVideoPicture.make(IPixelFormat.Type.YUV420P, picture.getWidth(), picture.getHeight()); 
			resampler.resample(convertedFrame, picture);
			convertedFrame.setComplete(true, IPixelFormat.Type.YUV420P, picture.getWidth(), picture.getHeight(), System.nanoTime()/1000 - start);
			convertedFrame.setKeyFrame((keyFrameCounter % 50) == 0);
			convertedFrame.setQuality(0);
			writer.encodeVideo(1, convertedFrame);
			writer.flush();
			keyFrameCounter++;
		}
		return frame;
	}

}
