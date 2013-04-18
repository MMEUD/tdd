package example;
import java.util.Map;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class Expression {

	public abstract int interpret(Map<String, Expression> vars);
	
}
