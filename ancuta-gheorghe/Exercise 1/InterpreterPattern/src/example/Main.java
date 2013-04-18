package example;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String expression = "w x z - +";
		Evaluator sentence = new Evaluator(expression);
		Map<String, Expression> vars = new HashMap<String, Expression>();
		Expression w = new NumberExpression(6);
		Expression x = new NumberExpression(10);
		Expression z = new NumberExpression(43);
		vars.put("w", w);
		vars.put("x", x);
		vars.put("z", z);
		int result = sentence.interpret(vars);
		System.out.println(result);

	}

}
