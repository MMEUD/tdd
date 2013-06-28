package pattern.interpreter;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */

public class GetExpression extends Expression {
	
	private Parameter parameter;
	private String[] commandParameters;
	
	public GetExpression(String[] commandParameters){
		Console console = Console.getInstance();
		this.setCommandParameters(commandParameters);
		if (this.validateCommandParameters()) {
			Namespace nsTemp = console.getCurrentNamespace();
			this.setParameter(nsTemp.getParameter(this.getCommandParameters()[0]));
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
		if (this.commandParameters.length == 1){
			return true;
		}
		return false;
	}
	
	public String interpretCommand(){
		Console console = Console.getInstance();
		if (this.validateCommandParameters()){
			if (this.parameter == null) {
				return console.getCurrentNamespace().getName() + ": this parameter does not exist.";
			} else {
				return console.getCurrentNamespace().getName() + ": " + this.getParameter().getName() + " = " + this.getParameter().getValue();
			}
		} else {
			return "Get command is not formed properly. Correct format: get {parameter_name}";
		}
	}

}
