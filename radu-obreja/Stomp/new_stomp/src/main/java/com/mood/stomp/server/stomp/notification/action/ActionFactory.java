package com.mood.stomp.server.stomp.notification.action;

import com.mood.stomp.server.stomp.Notification;

public class ActionFactory {

	public static IAction createAction(Notification notification) {
		if ("Create".equals(notification.getAction())) {
			return new CreateAction();
		}
		return new DefaultAction();
	}
}
