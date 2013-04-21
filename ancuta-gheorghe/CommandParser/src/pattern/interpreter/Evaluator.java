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
	
	private Expression syntaxTree;
	private String[] commandParameters;
	 
    public Evaluator(String expression) {
    	commandParameters = expression.split(" ");
    	String command = commandParameters[0];
    	commandParameters = Arrays.copyOfRange(commandParameters, 1, commandParameters.length);
    	if (command == null){
    		System.out.println("Please enter a command.");
        	return;
    	} else {
    		if (command.equals("ns")){
            	syntaxTree = new NsExpression(this.commandParameters);
            } else if (command.equals("set")){
            	syntaxTree = new SetExpression(this.commandParameters);
            } else if (command.equals("get")){
            	syntaxTree = new GetExpression(this.commandParameters);
            } else if (command.equals("list")){
            	syntaxTree = new ListExpression(this.commandParameters);
            } else if (command.equals("load")){
            	syntaxTree = new LoadExpression(this.commandParameters);
            } else if (command.equals("save")){
            	syntaxTree = new SaveExpression(this.commandParameters);
            } else {
            	System.out.println("This command does not exist.");
            	return;
            }
    	}
    }
 
	public boolean validateCommandParameters() {
		return false;
	}

	public void interpretCommand() {
        syntaxTree.interpretCommand();
    }
	
}
