package pattern.interpreter;

import java.util.ArrayList;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;

public class ListExpression extends Expression{

	//de refactorizat cod pentru strategy pattern.
	private ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
	private String[] commandParameters;
	
	public ListExpression(String[] commandParameters){
		this.setCommandParameters(commandParameters);
		if (this.validateCommandParameters()) {
			Console console = Console.getInstance();
			if (this.commandParameters.length > 0){
				Namespace nsTemp = new Namespace();
				nsTemp = console.getNamespace(this.getCommandParameters()[0]);
				this.namespaces.add(nsTemp);
			} else {
				this.namespaces = console.getNamespaces();
			}
		}
	}
	
	public String[] getCommandParameters() {
		return commandParameters;
	}

	public void setCommandParameters(String[] commandParameters) {
		this.commandParameters = commandParameters;
	}
	
	public boolean validateCommandParameters() {
		if (this.commandParameters.length == 0 || this.commandParameters.length == 1){
			return true;
		}
		return false;
	}
	
	public void interpretCommand(){
		if (this.validateCommandParameters()){
			for (Namespace nsTemp: this.namespaces){
				ArrayList<Parameter> parameters = new ArrayList<Parameter>();
				parameters = nsTemp.getParameters();
				for (Parameter pTemp: parameters){
					System.out.println(nsTemp.getName() + ": " + pTemp.getName() + " = " + pTemp.getValue());
				}
			}
		} else {
			System.out.println("List command is not formed properly. Correct format: list or list {namespace}");
		}
	}
}