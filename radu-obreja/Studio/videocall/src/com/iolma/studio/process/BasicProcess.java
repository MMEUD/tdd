package com.iolma.studio.process;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;

public abstract class BasicProcess extends Thread implements  IProcess {
	
	protected ILogger logger = new ConsoleLogger();
	protected String processName = this.getClass().getSimpleName();
	private AtomicLong fps = new AtomicLong(0);
	private AtomicBoolean started = new AtomicBoolean(false);

	private ConcurrentHashMap<String, IProcess> inputs  = new ConcurrentHashMap<String, IProcess>();
	private ConcurrentHashMap<String, IProcess> outputs  = new ConcurrentHashMap<String, IProcess>();
	protected LinkedBlockingQueue<IFrame> queue = new LinkedBlockingQueue<IFrame>();
	private IFrame currentFrame = null;

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
	
	public synchronized void addInput(IProcess input) {
		inputs.put(input.getProcessName(), input);
		input.getOutputs().put(this.getName(), this);
	}

	public synchronized void removeInput(IProcess input) {
		inputs.remove(input.getProcessName());
		input.getOutputs().remove(this.getName());
	}

	public synchronized void push(IFrame frame) {
		try {
			queue.put(frame);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isStarted() {
		return started.get();
	}

	public long getQueueSize() {
		return queue.size();
	}
	
	public long getFPS() {
		return fps.get();
	}
	
	public void clearFPS() {
		fps.set(0);
	}
	
	public synchronized void startup() {
		start();
		setPriority(MAX_PRIORITY);
		started.set(true);
		logger.info(processName + " started.");
	}

	public synchronized void shutdown() {
		started.set(false);
		if (isAlive()) {
			interrupt();
		}
	}

	public void run() {
		while (true) {
			try {
				fps.incrementAndGet();
				currentFrame = queue.take();
				execute(currentFrame);
				fireNextProcess();
			} catch (InterruptedException e) {
				break;
			}
		}
		getLogger().info(processName + " killed.");
	}

	protected void fireNextProcess() {
		Iterator<String> it = outputs.keySet().iterator();
		while (it.hasNext()) {
			IProcess output = outputs.get(it.next());
			output.push(currentFrame);
		}
	}
	
}
