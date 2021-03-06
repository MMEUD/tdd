package example;
import java.util.Map;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class MinusExpression extends Expression {
	Expression left;
	Expression right;
	
	public MinusExpression(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	
	public int interpret(Map<String, Expression> vars){
		return this.left.interpret(vars) - this.right.interpret(vars);
	}

}
