package com.laura.testemood.command;
public class CommandFactory
{
	
    public CommandFactory()
    {
    }

  
    public static ICommand createCommand(String[] args)
    {
    	ICommand command = null;
    	String type = args[0];
    	
        if (type.equals("get"))
        {
            	command = new GetCommand();
        }else if (type.equals("list")){
            	command = new ListCommand();
        }else  if (type.equals("load")){   
            	command = new LoadCommand();
        }else  if (type.equals("ns")){ 
            	command = new NSCommand();
        }else  if (type.equals("save")){ 
            	command = new SaveCommand();
        }else  if (type.equals("set")){ 
            	command = new SetCommand();
        }

        return command;
    }
}