package example;
import java.util.Map;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class NumberExpression extends Expression {
	
	private int number;
	
	public NumberExpression(int number){
		this.number = number;
	}
	
	public int interpret(Map<String, Expression> vars) {
		return this.number;
	}
}
