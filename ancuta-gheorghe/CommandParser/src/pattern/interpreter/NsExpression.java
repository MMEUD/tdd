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
	
	private String name;
	
	public NsExpression(String name){
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String interpret(){
		Console console = Console.getInstance();
		console.setCurrentNamespace(name);
		return "Current namespace: " + console.getCurrentNamespace().getName();
	}

}
