package com.mood.stomp.server.stomp.notification;

import java.util.ArrayList;

import com.mood.stomp.server.stomp.Notification;

public interface INotificationRetriever {

	public ArrayList<Notification> execute();
	
}
