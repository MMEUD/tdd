package com.iolma.studio.process;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;

public abstract class BasicProcess extends Thread implements  IProcess, IServer {
	
	private ILogger logger = new ConsoleLogger();
	private String processName = this.getClass().getSimpleName();
	private AtomicLong fps = new AtomicLong(0);

	private ConcurrentHashMap<String, IProcess> inputs  = new ConcurrentHashMap<String, IProcess>();
	private ConcurrentHashMap<String, IProcess> outputs  = new ConcurrentHashMap<String, IProcess>();
	private LinkedBlockingQueue<IFrame> queue = new LinkedBlockingQueue<IFrame>();
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

	public void push(IFrame frame) {
		try {
			queue.put(frame);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		logger.info(processName + " started.");
	}

	public synchronized void shutdown() {
		if (isAlive()) {
			interrupt();
		}
	}

	public void run() {
		while (true) {
			try {
				currentFrame = queue.take();
				fps.incrementAndGet();
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
