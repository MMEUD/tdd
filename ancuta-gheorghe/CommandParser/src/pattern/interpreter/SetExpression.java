package pattern.interpreter;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */

public class SetExpression extends Expression {
	
	private Parameter parameter;
	private String[] commandParameters;
	
	public SetExpression(String[] commandParameters){
		this.setCommandParameters(commandParameters);
		if (this.validateCommandParameters()) {
			Console console = Console.getInstance();
			this.setParameter(new Parameter(this.getCommandParameters()[0], this.getCommandParameters()[1]));
			Namespace nsTemp = console.getCurrentNamespace();
			nsTemp.setParameter(this.getParameter());
		} 
	}
	
	public Parameter getParameter() {
		return this.parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
	public String[] getCommandParameters() {
		return commandParameters;
	}

	public void setCommandParameters(String[] commandParameters) {
		this.commandParameters = commandParameters;
	}
	
	public boolean validateCommandParameters() {
		if (this.commandParameters.length == 2){
			return true;
		}
		return false;
	}
	
	public String interpretCommand(){
		Console console = Console.getInstance();
		if (this.validateCommandParameters()){
			return console.getCurrentNamespace().getName() + ": " + this.getParameter().getName() + " = " + this.getParameter().getValue();
		} else {
			return "Set command is not formed properly. Correct format: set {parameter_name} {parameter_value}";
		}
	}

}