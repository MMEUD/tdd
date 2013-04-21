package pattern.interpreter;

import pattern.singleton.Console;

/**
 * @author Ancuta Gheorghe
 *
 */

public class NsExpression extends Expression {
	
	private String activeNamespace;
	private String[] commandParameters;
	
	public NsExpression(String[] commandParameters){
		this.setCommandParameters(commandParameters);
		if (this.validateCommandParameters()) {
			this.setActiveNamespace(this.getCommandParameters()[0]);
		}
	}
	
	public String getActiveNamespace() {
		return this.activeNamespace;
	}

	public void setActiveNamespace(String activeNamespace) {
		this.activeNamespace = activeNamespace;
	}

	public String[] getCommandParameters() {
		return commandParameters;
	}

	public void setCommandParameters(String[] commandParameters) {
		this.commandParameters = commandParameters;
	}


	public boolean validateCommandParameters() {
		if (this.commandParameters.length == 1){
			return true;
		}
		return false;
	}

	public void interpretCommand(){
		Console console = Console.getInstance();
		if (this.validateCommandParameters()){
			console.setCurrentNamespace(this.activeNamespace);
			System.out.println("Current namespace: " + console.getCurrentNamespace().getName());
		} else {
			System.out.println("Ns command is not formed properly. Correct format: ns {namespace}");
		}
	}

}