package com.iolma.studio.application.module;

import com.iolma.studio.process.IProcess;


public interface IVideoComponent {

	public void setVideoEnabled(boolean enabled);
	
	public boolean isVideoEnabled();

	public void setVideoVisible(boolean visible);
	
	public boolean isVideoVisible();

	public void setVideoFullscreen(boolean fullscreen);

	public boolean isVideoFullscreen();

	public void addVideoInput(IProcess input);

	public void removeVideoInput(IProcess input);
}
