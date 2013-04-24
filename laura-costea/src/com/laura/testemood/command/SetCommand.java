package com.laura.testemood.command;

import com.laura.testemood.PropertiesLoader;

public class SetCommand implements ICommand {

	@Override
	public String execute(Context context) {
		PropertiesLoader pl = PropertiesLoader.getInstance();
		final String args[] = context.getArgs();
		
		if(args.length>2){
			pl.setProperty(args[1], args[2], context.getNamespace());
			return context.getNamespace()+" : "+args[1]+" = "+pl.getProperty(args[1],context.getNamespace());
		}else
			return "Usage: set {parameter_name} {parameter_value}";
	}

	@Override
	public String getCommandName() {
		return "set";
	}

}
