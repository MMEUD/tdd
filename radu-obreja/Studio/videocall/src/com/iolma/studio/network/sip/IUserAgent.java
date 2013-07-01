package com.iolma.studio.network.sip;

public interface IUserAgent {
	
	public void addListener(String key, IUserAgentListener listener);
	
	public void removeListener(String key);
	
	public void register();

	public void unregister();

	public void call(String userId);
	
	public void answer();
	
	public void terminate();
	
}
