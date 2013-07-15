package com.iolma.studio.application.module;

import com.iolma.studio.process.IProcess;


public interface IAudioComponent {

	public void setAudioEnabled(boolean enabled);
	
	public boolean isAudioEnabled();

	public void addAudioInput(IProcess input);

	public void removeAudioInput(IProcess input);

}
