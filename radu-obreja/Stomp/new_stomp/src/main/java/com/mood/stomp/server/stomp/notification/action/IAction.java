package com.mood.stomp.server.stomp.notification.action;

import com.mood.stomp.server.stomp.IStomp;
import com.mood.stomp.server.stomp.Notification;

public interface IAction {

	public void execute(IStomp stomp, Notification notification) throws Exception;
	
}
