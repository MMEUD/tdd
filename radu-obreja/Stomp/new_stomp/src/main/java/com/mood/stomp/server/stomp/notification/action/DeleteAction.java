package com.mood.stomp.server.stomp.notification.action;

import com.mood.stomp.server.stomp.IStomp;
import com.mood.stomp.server.stomp.Notification;

public class DeleteAction implements IAction {
	
	public void execute(IStomp stomp, Notification notification) throws Exception {
		stomp.removeUser(notification.getCustomer().getName());
	}

}
