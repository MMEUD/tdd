package com.laura.testemood;


public final class CommandLineParser {
	
	public static String[] parseArgs(final String cmdLine){
		return  cmdLine.split("\\s+");
	}
	


}
