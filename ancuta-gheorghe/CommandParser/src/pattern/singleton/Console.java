/**
 * 
 */
package pattern.singleton;

import java.util.ArrayList;

import pattern.utils.Parameter;

/**
 * @author ancuta
 *
 */
public class Console {

	private static volatile Console instance = null;
	private String namespace;
	private ArrayList<Parameter> parameters;
	
    private Console() {
    	
    }

    public static Console getInstance() {
            if (instance == null) {
                    synchronized (Console .class){
                            if (instance == null) {
                                    instance = new Console();
                            }
                  }
            }
            return instance;
    }


	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	
}
