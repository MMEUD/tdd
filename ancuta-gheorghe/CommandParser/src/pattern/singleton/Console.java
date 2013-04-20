/**
 * 
 */
package pattern.singleton;

import java.util.ArrayList;
import pattern.utils.Namespace;

/**
 * @author ancuta
 *
 */
public class Console {

	private static volatile Console instance = null;
	private Namespace currentNamespace = new Namespace();
	private ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
	
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


	public Namespace getCurrentNamespace() {
		return currentNamespace;
	}

	public void setCurrentNamespace(String name) {
		Namespace namespace = new Namespace();
		boolean exists = false;
		for (Namespace namespaceTemp : getNamespaces()) {
	        if (namespaceTemp.getName().equals(name)) {
	        	exists = true;
	        	namespace = namespaceTemp;
	        }
	    }
		if (exists){
			this.currentNamespace = namespace;
		} else {
			namespace.setName(name);
			this.setNamespace(namespace);
			this.setCurrentNamespace(name);
		}
		
	}

	public ArrayList<Namespace> getNamespaces() {
		return this.namespaces;
	}

	public Namespace getNamespace(String name){
		Namespace namespace = null;
		for (Namespace namespaceTemp : getNamespaces()) {
	        if (namespaceTemp.getName().equals(name)) {
	        	namespace = namespaceTemp;
	        }
	    }
		return namespace;
	}
	
	public void setNamespaces(ArrayList<Namespace> namespaces) {
		this.namespaces = namespaces;
	}
	
	public void setNamespace(Namespace namespace){
		this.namespaces.add(namespace);
	}
	
	public void clearNamespaceList(){
		namespaces.clear();
	}
	
}
