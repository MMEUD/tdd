package com.iolma.tests;

import com.iolma.studio.log.ConsoleLogger;
import com.iolma.studio.log.ILogger;
import com.iolma.studio.process.FrameGenerator;
import com.iolma.studio.process.StatisticsGenerator;

public class ClockTest {

	public static void main(String[] args) {

		// Create logger
		ILogger logger = new ConsoleLogger();
		
		// Create statistics
		StatisticsGenerator statistics = new StatisticsGenerator(1, logger);		
		statistics.startup();
		
		// Create audio generator
		FrameGenerator audioGenerator = new FrameGenerator(100, logger);
		statistics.addInput(audioGenerator);
		
		audioGenerator.startup();
		
	}

}
