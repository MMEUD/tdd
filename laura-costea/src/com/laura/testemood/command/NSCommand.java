package com.laura.testemood.command;


public class NSCommand implements ICommand {

	@Override
	public String execute(Context context) {
		final String args[] = context.getArgs();
		if(args.length>1) {
			context.setNamespace(args[1]);
			return "Current namespace: "+context.getNamespace();
		}else
			return "Usage : ns {namespace_name}";
		
		
		
	}

	@Override
	public String getCommandName() {
		return "ns";
	}

}
