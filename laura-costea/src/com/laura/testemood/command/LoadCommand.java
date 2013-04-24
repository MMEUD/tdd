package com.laura.testemood.command;

import com.laura.testemood.PropertiesLoader;

public class LoadCommand implements ICommand {
	
	@Override
	public String execute(Context context) {
		final String args[] = context.getArgs();
		if(args.length>1)
			return PropertiesLoader.getInstance().load(args[1]);
		else 
			return PropertiesLoader.getInstance().load(null);
	}


	@Override
	public String getCommandName() {
		return "load";
	}

}
