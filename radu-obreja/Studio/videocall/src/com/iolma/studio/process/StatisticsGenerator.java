package com.iolma.studio.process;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.iolma.studio.log.ILogger;

public class StatisticsGenerator extends AbstractClock {

	private ConcurrentHashMap<String, IProcess> inputs  = new ConcurrentHashMap<String, IProcess>();

	public StatisticsGenerator(double ticksPerSecond, ILogger logger) {
		this.ticksPerSecond = ticksPerSecond;
		this.logger = logger;
	}
	
	protected void fireOutputs() {
		StringBuffer sb = new StringBuffer();
		sb.append("Statistics -> ");
		Iterator<String> it = inputs.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			sb.append(name + " : " + inputs.get(name).getFPS() + "fps/" + inputs.get(name).getQueueSize() + "q   ");
			inputs.get(name).clearFPS();
		}
		logger.info(sb.toString());
	}

	public synchronized void addInput(IProcess input) {
		inputs.put(input.getProcessName(), input);
	}

	public synchronized void removeInput(IProcess input) {
		inputs.remove(input.getProcessName());
	}

	@Override
	public void push(IFrame frame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFrame execute(IFrame frame) {
		// TODO Auto-generated method stub
		return frame;
	}
	
	@Override
	public long getQueueSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getFPS() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clearFPS() {
		// TODO Auto-generated method stub
		
	}
	
}
