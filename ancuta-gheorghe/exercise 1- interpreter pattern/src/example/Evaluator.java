package example;
import java.util.Map;
import java.util.Stack;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class Evaluator extends Expression{
	
	private Expression syntaxTree;
	 
    public Evaluator(String expression) {
        Stack<Expression> expressionStack = new Stack<Expression>();
        for (String token : expression.split(" ")) {
            if  (token.equals("+")) {
                Expression subExpression = new PlusExpression(expressionStack.pop(), expressionStack.pop());
                expressionStack.push( subExpression );
            }
            else if (token.equals("-")) {
                Expression right = expressionStack.pop();
                Expression left = expressionStack.pop();
                Expression subExpression = new MinusExpression(left, right);
                expressionStack.push( subExpression );
            }
            else                        
                expressionStack.push( new VariableExpression(token) );
        }
        syntaxTree = expressionStack.pop();
    }
 
    public int interpret(Map<String,Expression> context) {
        return syntaxTree.interpret(context);
    }
}
