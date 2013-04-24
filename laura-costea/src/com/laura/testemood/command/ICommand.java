package com.laura.testemood.command;

public interface ICommand {
	String getCommandName();
	String execute(Context context);
}
