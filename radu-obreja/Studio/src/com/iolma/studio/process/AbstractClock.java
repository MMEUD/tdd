package com.iolma.studio.process;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

import com.iolma.studio.log.ILogger;


public abstract class AbstractClock implements IProcess, IServer, Runnable {
	
	protected ILogger logger = null;
	private String processName = this.getClass().getSimpleName();
	private Thread runner = null;

	protected ConcurrentHashMap<String, IProcess> outputs  = new ConcurrentHashMap<String, IProcess>();

	private final static int ONE_SECOND_IN_NANOS = 1000000000;
	protected int ticksPerSecond = 1;
	private boolean alive = false;
	
	
	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public ConcurrentHashMap<String, IProcess> getOutputs() {
		return outputs;
	}	
	
	public synchronized void startup() {
		if (runner == null) {
			runner = new Thread(this);
			alive = true;
			runner.start();
			getLogger().info("Clock [" + ticksPerSecond + "] started.");
		}
	}

	public synchronized void shutdown() {
		alive = false;
	}

	public void run() {
		while (alive) {
			LockSupport.parkNanos(ONE_SECOND_IN_NANOS / ticksPerSecond);
			fireOutputs();
		}
		getLogger().info("Clock [" + ticksPerSecond + "] killed.");
	}

	protected abstract void fireOutputs();

}
