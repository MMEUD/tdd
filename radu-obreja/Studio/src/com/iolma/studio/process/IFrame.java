package com.iolma.studio.process;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IVideoPicture;

public interface IFrame {
	
	public void setVideoPicture(IVideoPicture picture);
	
	public IVideoPicture getVideoPicture();

	public void setAudioSamples(IAudioSamples audioSamples);
	
	public IAudioSamples getAudioSamples();

}
