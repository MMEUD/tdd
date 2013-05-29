package com.mood.stomp.server.stomp.notification.action;

import org.hornetq.api.core.HornetQException;

import com.mood.stomp.server.stomp.IStomp;
import com.mood.stomp.server.stomp.Notification;

public class DefaultAction implements IAction {
	
	public void execute(IStomp stomp, Notification notification) throws HornetQException {
		stomp.sendMessage(notification.getAction(), notification.getCustomer().getName());
	}

}
