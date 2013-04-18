package pattern.interpreter;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class NsExpression extends Expression {
	String namespace;
	
	public NsExpression(String namespace){
		this.namespace = namespace;
	}
	
	public String interpret(){
		return "Current namespace: " + this.namespace;
	}

}
