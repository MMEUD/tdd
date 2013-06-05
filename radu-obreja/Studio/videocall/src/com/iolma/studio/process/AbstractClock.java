package com.iolma.studio.process;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import com.iolma.studio.log.ILogger;


public abstract class AbstractClock extends Thread implements IProcess, IServer {
	
	protected ILogger logger = null;
	private String processName = this.getClass().getSimpleName();
	private boolean alive = true;
	private AtomicLong fps = new AtomicLong(0);

	protected ConcurrentHashMap<String, IProcess> outputs  = new ConcurrentHashMap<String, IProcess>();
	protected double ticksPerSecond = 1;
	
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	public ConcurrentHashMap<String, IProcess> getOutputs() {
		return outputs;
	}	
	
	public synchronized void startup() {
		start();
		setPriority(MAX_PRIORITY);
		getLogger().info("Clock [" + ticksPerSecond + "] started.");
	}

	public synchronized void shutdown() {
		alive = false;
	}

	public void run() {
		final long interval = Math.round(1000000000 / ticksPerSecond);
		while (alive) {
			long start = System.nanoTime();
			fps.incrementAndGet();
			fireOutputs();
			long waitTime = interval - (System.nanoTime() - start);
			if (waitTime > 0) {
				LockSupport.parkNanos(waitTime);
			}
		}
		getLogger().info("Clock [" + ticksPerSecond + "] killed.");
	}

	public long getFPS() {
		return fps.get();
	}
	
	public void clearFPS() {
		fps.set(0);
	}
	
	protected abstract void fireOutputs();

}
