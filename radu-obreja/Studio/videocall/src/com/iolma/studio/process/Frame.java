package com.iolma.studio.process;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IVideoPicture;

public class Frame implements IFrame {
	
	private IVideoPicture picture = null;
	private IAudioSamples audioSamples = null;

	public void setVideoPicture(IVideoPicture picture) {
		this.picture = picture;
	}
	
	public IVideoPicture getVideoPicture() {
		return picture;
	}

	public void setAudioSamples(IAudioSamples audioSamples) {
		this.audioSamples = audioSamples;
	}

	public IAudioSamples getAudioSamples() {
		return audioSamples;
	}

}
