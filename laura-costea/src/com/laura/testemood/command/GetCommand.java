package com.laura.testemood.command;

import com.laura.testemood.PropertiesLoader;

public class GetCommand implements ICommand {

	@Override
	public String execute(Context context) {
		PropertiesLoader pl = PropertiesLoader.getInstance();
		final String args[] = context.getArgs();
		if(args.length>1){
			return context.getNamespace()+" : "+args[1]+" = "+pl.getProperty(args[1],context.getNamespace());
		}else
			return "Usage: get {parameter_name}";
	}

	@Override
	public String getCommandName() {
		return "get";
	}

}
