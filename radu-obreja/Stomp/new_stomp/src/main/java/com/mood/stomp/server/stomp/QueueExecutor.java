package com.mood.stomp.server.stomp;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.mood.stomp.server.IService;
import com.mood.stomp.server.stomp.notification.action.ActionFactory;
import com.mood.stomp.server.stomp.notification.action.IAction;

public class QueueExecutor extends Thread implements IService {

	private static Logger log = Logger.getLogger(QueueExecutor.class.getName());
	private LinkedBlockingQueue<Notification> queue = null;
	private IStomp stomp = null;
	
	public QueueExecutor(IStomp stomp, LinkedBlockingQueue<Notification> queue) {
		this.stomp = stomp;
		this.queue = queue;
	}
	
	public void startup() {
		start();
	}

	public void shutdown() {
		if (isAlive()) {
			interrupt();
		}
	}

	public void run() {
		while (true) {
			try {
				Notification notification = queue.take();
				IAction action = ActionFactory.createAction(notification);
				try {
					action.execute(stomp, notification);
				} catch (Exception e) {
					log.severe(e.getMessage());
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
