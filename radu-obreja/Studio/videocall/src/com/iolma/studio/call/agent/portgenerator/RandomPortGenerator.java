package com.iolma.studio.call.agent.portgenerator;

import java.util.Random;

import com.iolma.studio.call.agent.IPortGenerator;


public class RandomPortGenerator implements IPortGenerator {

	private int min = 0;
	private Random seed = null;
	private int maxSeed = 0;
	
	public RandomPortGenerator(int min, int max) {
		this.min = min;
		maxSeed = Math.round((max - min) / 4);
		seed = new Random(System.currentTimeMillis());
	}
	
	public int getPort() {
		return min + (4 * seed.nextInt(maxSeed));
	}

}
