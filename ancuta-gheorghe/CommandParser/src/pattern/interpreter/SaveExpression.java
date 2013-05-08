/**
 * 
 */
package pattern.interpreter;

import java.util.ArrayList;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;
import pattern.utils.PropertiesFile;

/**
 * @author ancuta
 *
 */
public class SaveExpression extends Expression{

	//de refactorizat cod pentru factory method pattern.
	private ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
	private String[] commandParameters;
	
	public SaveExpression(String[] commandParameters){
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
	
	public String interpretCommand(){
		String output = "";
		if (this.validateCommandParameters()) {
			if (this.commandParameters.length > 0){
				Namespace nsTemp = this.namespaces.get(0);
				PropertiesFile.saveToFile(nsTemp.getName(), nsTemp.getParameters());
				return this.commandParameters[0] + ": saved " + nsTemp.getParameters().size() + " parameters.";
			} else {
				for (Namespace nsTemp: this.namespaces){
					PropertiesFile.saveToFile(nsTemp.getName(), nsTemp.getParameters());
					output += nsTemp.getName() + ": saved " + nsTemp.getParameters().size() + " parameters.\n";
				}
				return output;
			}
		} else {
			return "Load command is not formed properly. Correct format: save or save {namespace}";
		}
		
	}
	
}
