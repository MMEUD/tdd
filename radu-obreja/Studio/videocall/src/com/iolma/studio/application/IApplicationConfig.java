package com.iolma.studio.application;

import com.iolma.studio.call.ICallAgentConfig;

public interface IApplicationConfig extends ICallAgentConfig {

	public String getRemoteUser();

	public void setRemoteUser(String remoteUser);
	
}
