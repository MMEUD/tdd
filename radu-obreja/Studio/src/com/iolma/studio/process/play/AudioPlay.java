package com.iolma.studio.process.play;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.iolma.studio.process.BasicProcess;
import com.iolma.studio.process.IFrame;
import com.xuggle.xuggler.IAudioSamples;

public class AudioPlay extends BasicProcess {

    // linear PCM 48kHz, 16 bits signed, mono-channel, little endian
	private AudioFormat audioFormat = new AudioFormat(48000.0F, 16, 1, true, false);
	private DataLine.Info sourceInfo = null;
	private SourceDataLine sourceDataLine;

	public void startup() {
		super.startup();
		try {
			sourceInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
        } catch (LineUnavailableException e) {
            logger.error("speakers unavailable");
            return;
        }
	}

	public void shutdown() {
		super.shutdown();
        if (sourceDataLine != null) {
            sourceDataLine.drain();
            sourceDataLine.stop();
            sourceDataLine.close();
            sourceDataLine = null;
        }
	}

	public IFrame execute(IFrame frame) {
		if (sourceDataLine != null) {
			IAudioSamples audioSamples = frame.getAudioSamples();
			if (audioSamples != null) {
				byte[] buffer = audioSamples.getData().getByteArray(0, audioSamples.getSize());
				sourceDataLine.write(buffer, 0, audioSamples.getSize());
			}
		}
		return frame;
	}

}
