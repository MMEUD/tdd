package example;
import java.util.Map;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class VariableExpression extends Expression {
	
	private String name;
	
	public VariableExpression(String name){
		this.name = name;
	}
	
	public int interpret(Map<String, Expression> vars) {
		if (null == vars.get(name)) return 0;
		return vars.get(name).interpret(vars);
	}

}
