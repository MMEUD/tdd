package com.iolma.studio.process;

import java.util.concurrent.ConcurrentHashMap;

import com.iolma.studio.log.ILogger;


public interface IProcess {
	
	public String getProcessName();

	public ILogger getLogger();
	
	public void addInput(IProcess input);
	
	public void removeInput(IProcess input);

	public ConcurrentHashMap<String, IProcess> getOutputs();
	
	public void push(IFrame frame);
	
	public IFrame execute(IFrame frame);
	
	public long getQueueSize();
	
	public long getFPS();
	
	public void clearFPS();
	
}
