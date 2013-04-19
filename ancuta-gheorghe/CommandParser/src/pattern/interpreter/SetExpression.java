package pattern.interpreter;
import pattern.singleton.Console;
import pattern.utils.Parameter;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class SetExpression extends Expression {
	
	Parameter parameter;
	
	public SetExpression(String name, String value){
		this.parameter = new Parameter(name, value);
	}
	
	public String interpret(){
		Console console = Console.getInstance();
		console.setParameter(this.parameter);
		return console.getNamespace() + ": " + this.parameter.getName() + " = " + this.parameter.getValue();
	}

}
