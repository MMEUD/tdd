package com.laura.testemood.command;

import java.util.HashMap;
import java.util.Iterator;

import com.laura.testemood.PropertiesLoader;


public class SaveCommand implements ICommand {

	@Override
	public String execute(Context context) {
		final String args[] = context.getArgs();
		HashMap<String, String> result;
		if(args.length>1)
			result = PropertiesLoader.getInstance().save(args[1]);
		else 
			result =  PropertiesLoader.getInstance().save(null);
		
		return display(result);
	}
	
	private String display(HashMap result){
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = result.keySet().iterator();
	
		if(it.hasNext())
		while (it.hasNext()) {
			String namespace = it.next();
			sb.append( namespace +":"+ "saved "+ result.get(namespace)+" parameters \n");
		}
		else sb.append("No properties to be saved");
		return sb.toString();
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "save";
	}

}
