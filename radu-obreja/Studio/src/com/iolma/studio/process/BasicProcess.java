package com.iolma.studio.process;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;

public abstract class BasicProcess implements IProcess {
	
	private ILogger logger = new ConsoleLogger();
	private String name = null;

	private ConcurrentHashMap<String, IProcess> inputs  = new ConcurrentHashMap<String, IProcess>();
	private ConcurrentHashMap<String, IProcess> outputs  = new ConcurrentHashMap<String, IProcess>();

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	public String getName() {
		if (name == null) {
			name = this.getClass().getSimpleName();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public synchronized void addInput(IProcess input) {
		inputs.put(input.getName(), input);
		input.getOutputs().put(this.getName(), this);
	}

	public synchronized void removeInput(IProcess input) {
		inputs.remove(input.getName());
		input.getOutputs().remove(this.getName());
	}

	public ConcurrentHashMap<String, IProcess> getOutputs() {
		return outputs;
	}

	protected void fireNextProcess() {
		Iterator<String> it = outputs.keySet().iterator();
		while (it.hasNext()) {
			IProcess output = outputs.get(it.next());
			output.execute();
		}
	}

}
