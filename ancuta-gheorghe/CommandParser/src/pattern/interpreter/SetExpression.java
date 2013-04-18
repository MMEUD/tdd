package pattern.interpreter;
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
		return "Namespace: " + this.parameter.getName() + " = " + this.parameter.getValue();
	}

}
