/**
 * 
 */
package pattern.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import pattern.singleton.Console;
import pattern.utils.Namespace;
import pattern.utils.Parameter;
import pattern.utils.PropertiesFile;

/**
 * @author ancuta
 *
 */
public class LoadExpression extends Expression{

	private ArrayList<String> files = new ArrayList<String>();
	private String[] commandParameters;
	
	public LoadExpression(String[] commandParameters){
		this.setCommandParameters(commandParameters);
	}
	
	public ArrayList<String> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<String> files) {
		this.files = files;
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
	
	@SuppressWarnings("rawtypes")
	public void interpretCommand(){
		if (this.validateCommandParameters()) {
			Console console = Console.getInstance();
			if (this.commandParameters.length > 0){
				Namespace nsTemp = new Namespace();
				nsTemp.setName(this.getCommandParameters()[0]);
				console.setNamespace(nsTemp);
				
				Map<String, String> paramsTemp = PropertiesFile.loadFile(this.getCommandParameters()[0]);
				int size = paramsTemp.size();
				Parameter paramTemp = null;
				Iterator it = paramsTemp.entrySet().iterator();
				while (it.hasNext()) {
				    Map.Entry pairs = (Map.Entry)it.next();
				    paramTemp = new Parameter(pairs.getKey().toString(), pairs.getValue().toString());
				    nsTemp.setParameter(paramTemp);
				    it.remove();
				}				
				System.out.println(this.commandParameters[0] + ": loaded " + size + " parameters.");
			} else {
				this.setFiles(PropertiesFile.getFiles());
				for (String file: this.getFiles()){
					Namespace nsTemp = new Namespace();
					nsTemp.setName(file);
					console.setNamespace(nsTemp);
					Map<String, String> paramsTemp = PropertiesFile.loadFile(file);
					int size = paramsTemp.size();
					Parameter paramTemp = null;
					Iterator it = paramsTemp.entrySet().iterator();
					while (it.hasNext()) {
					    Map.Entry pairs = (Map.Entry)it.next();
					    paramTemp = new Parameter(pairs.getKey().toString(), pairs.getValue().toString());
					    nsTemp.setParameter(paramTemp);
					    it.remove();
					}		
					System.out.println(file + ": loaded " + size + " parameters.");
				}
			}
		} else {
			System.out.println("Load command is not formed properly. Correct format: load or load {namespace}");
		}
	}
	
}
