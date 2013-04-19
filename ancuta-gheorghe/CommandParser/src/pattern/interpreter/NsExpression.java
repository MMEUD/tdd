package pattern.interpreter;

import pattern.singleton.Console;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class NsExpression extends Expression {
	
	private String namespace;
	
	public NsExpression(String namespace){
		this.setNamespace(namespace);
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String interpret(){
		Console console = Console.getInstance();
		console.setNamespace(this.getNamespace());
		console.clearParameterList();
		return "Current namespace: " + this.getNamespace();
	}

}
