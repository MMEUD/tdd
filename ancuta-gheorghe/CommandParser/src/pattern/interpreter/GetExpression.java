/**
 * 
 */
package pattern.interpreter;

import pattern.singleton.Console;
import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class GetExpression extends Expression {
	
	Parameter parameter;
	
	public GetExpression(String name){
		Console console = Console.getInstance();
		this.parameter = console.getParameter(name);
	}
	
	public String interpret(){
		Console console = Console.getInstance();
		if (this.parameter == null) return console.getNamespace() + ": this parameter does not exist.";
		return console.getNamespace() + ": " + this.parameter.getName() + " = " + this.parameter.getValue();
	}

}
