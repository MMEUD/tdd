package com.iolma.studio.process.capture;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IAudioSamples;

public class AudioCapture extends BasicProcess {

    // linear PCM 48kHz, 16 bits signed, mono-channel, little endian
	private int sampleRate = 48000;
	private AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 1, true, false);
	private DataLine.Info targetInfo = null;
	private TargetDataLine targetDataLine;
	
	public void startup() {
		super.startup();
		try {
			targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat, 12000);
            targetDataLine.start();
        } catch (LineUnavailableException e) {
            logger.error("microphone unavailable");
            return;
        }
	}

	public void shutdown() {
		super.shutdown();
        if (targetDataLine != null) {
            targetDataLine.close();
            targetDataLine = null;
        }
	}

	public IFrame execute(IFrame frame) {
		if (targetDataLine != null) {
	        if (targetDataLine.available() > 0) {
	        	byte[] buffer = new byte[12000];
	        	int numBytesToRead = targetDataLine.read(buffer, 0, buffer.length);	        	
	        	IBuffer iBuf = IBuffer.make(null, buffer, 0, numBytesToRead);
	        	IAudioSamples audioSamples = IAudioSamples.make(iBuf,audioFormat.getChannels(),IAudioSamples.Format.FMT_S16);
	        	long numSample = numBytesToRead / audioSamples.getSampleSize();
	    		audioSamples.setComplete(true, numSample, (int)audioFormat.getSampleRate(), audioFormat.getChannels(), IAudioSamples.Format.FMT_S16, System.nanoTime()/1000);     		
	    		frame.setAudioSamples(audioSamples);
	        }
		}
		return frame;
	}

}
