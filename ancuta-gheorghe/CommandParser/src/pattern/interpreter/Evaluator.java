package pattern.interpreter;

import java.util.Arrays;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class Evaluator extends Expression{
	
	private Expression expression;
	private String[] commandParameters;
	 
    public Evaluator(String command) {
    	commandParameters = command.split(" ");
    	String commandName = commandParameters[0];
    	commandParameters = Arrays.copyOfRange(commandParameters, 1, commandParameters.length);
    	if (commandName == null){
    		System.out.println("Please enter a command.");
        	return;
    	} else {
    		if (commandName.equals("ns")){
            	expression = new NsExpression(this.commandParameters);
            } else if (commandName.equals("set")){
            	expression = new SetExpression(this.commandParameters);
            } else if (commandName.equals("get")){
            	expression = new GetExpression(this.commandParameters);
            } else if (commandName.equals("list")){
            	expression = new ListExpression(this.commandParameters);
            } else if (commandName.equals("load")){
            	expression = new LoadExpression(this.commandParameters);
            } else if (commandName.equals("save")){
            	expression = new SaveExpression(this.commandParameters);
            } else {
            	System.out.println("This command does not exist.");
            	return;
            }
    	}
    }
    
    public Expression getExpression(){
    	return this.expression;
    }
 
	public boolean validateCommandParameters() {
		return false;
	}

	public void interpretCommand() {
        expression.interpretCommand();
    }
	
}
