package com.iolma.studio.process;

import java.util.concurrent.ConcurrentHashMap;

import com.iolma.studio.log.ILogger;


public interface IProcess {
	
	public String getName();

	public ILogger getLogger();

	public void addInput(IProcess input);
	
	public void removeInput(IProcess input);

	public ConcurrentHashMap<String, IProcess> getOutputs();
	
	public void execute();

}
