package com.iolma.studio.process;

import java.util.Iterator;

import com.iolma.studio.log.ILogger;


public class FrameGenerator extends AbstractClock {
	
	public FrameGenerator(int ticksPerSecond, ILogger logger) {
		this.ticksPerSecond = ticksPerSecond;
		this.logger = logger;
	}

	protected void fireOutputs() {
		Iterator<String> it = outputs.keySet().iterator();
		while (it.hasNext()) {
			IProcess output = outputs.get(it.next());
			output.push(new Frame());
		}
	}

	@Override
	public void addInput(IProcess input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeInput(IProcess input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void push(IFrame frame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(IFrame frame) {
		// TODO Auto-generated method stub
		
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
