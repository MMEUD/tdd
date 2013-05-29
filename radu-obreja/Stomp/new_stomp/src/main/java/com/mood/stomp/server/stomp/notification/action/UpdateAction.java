package com.mood.stomp.server.stomp.notification.action;

import com.mood.stomp.server.stomp.IStomp;
import com.mood.stomp.server.stomp.Notification;

public class UpdateAction implements IAction {
	
	public void execute(IStomp stomp, Notification notification) throws Exception {
		stomp.createUsers(notification.getCustomer().getName(), notification.getCustomer().getContactNameList());
	}

}
