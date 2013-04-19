package pattern.interpreter;

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
        if (expression.split(" ")[0] != null && expression.split(" ")[0].equals("ns")){
        	syntaxTree = new NsExpression(expression.split(" ")[1]!=null?expression.split(" ")[1].toString():"");
        } else if (expression.split(" ")[0] != null && expression.split(" ")[0].equals("set")){
        	syntaxTree = new SetExpression(expression.split(" ")[1] != null?expression.split(" ")[1].toString():"", 
            		expression.split(" ")[2] != null?expression.split(" ")[2].toString():"");
        } else if (expression.split(" ")[0] != null && expression.split(" ")[0].equals("get")){
        	syntaxTree = new GetExpression(expression.split(" ")[1] != null?expression.split(" ")[1].toString():"");
        }
    }
 
    public String interpret() {
        return syntaxTree.interpret();
    }
}
