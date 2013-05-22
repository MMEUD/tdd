package com.iolma.studio.process.tick;

import java.util.concurrent.locks.LockSupport;

import com.iolma.studio.process.BasicTicker;
import com.iolma.studio.process.ITicker;

public class Ticker extends BasicTicker implements ITicker, Runnable {
	
	private final static int ONE_SECOND_IN_NANOS = 1000000000;
	private int ticksPerSecond = 1;
	private Thread runner = null;
	private boolean alive = false;
	
	public Ticker(int ticksPerSecond) {
		this.ticksPerSecond = ticksPerSecond;
	}
	
	public synchronized void startup() {
		if (runner == null) {
			runner = new Thread(this);
			alive = true;
			runner.start();
			getLogger().info("Ticker(" + ticksPerSecond + ") started.");
		}
	}

	public synchronized void shutdown() {
		alive = false;
	}

	public void run() {
		while (alive) {
			LockSupport.parkNanos(ONE_SECOND_IN_NANOS / ticksPerSecond);
			fireNextProcess();
		}
		getLogger().info("Ticker(" + ticksPerSecond + ") killed.");
	}

}
