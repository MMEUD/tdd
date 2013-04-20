/**
 * 
 */
package pattern.interpreter;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class GetExpression extends Expression {
	
	Namespace namespace;
	Parameter parameter;
	
	public GetExpression(String name){
		Console console = Console.getInstance();
		this.namespace = console.getCurrentNamespace();
		this.parameter = this.namespace.getParameter(name);
	}
	
	public String interpret(){
		Console console = Console.getInstance();
		if (this.parameter == null) return console.getCurrentNamespace().getName() + ": this parameter does not exist.";
		return console.getCurrentNamespace().getName() + ": " + this.parameter.getName() + " = " + this.parameter.getValue();
	}

}
