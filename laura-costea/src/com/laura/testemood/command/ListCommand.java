package com.laura.testemood.command;

import com.laura.testemood.PropertiesLoader;

public class ListCommand implements ICommand {
	
	@Override
	public String execute(Context context) {
		final String args[] = context.getArgs();
		String result;
		if(args.length>1)
			result = PropertiesLoader.getInstance().list(args[1]);
		else 
			result = PropertiesLoader.getInstance().list(null);
		return result;
	}

	@Override
	public String getCommandName() {
		return "list";
	}

}
