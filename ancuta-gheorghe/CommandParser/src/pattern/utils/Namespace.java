/**
 * 
 */
package pattern.utils;

import java.util.ArrayList;

/**
 * @author Ancuta Gheorghe
 *
 */
public class Namespace {

	private String name;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public Parameter getParameter(String name){
		Parameter parameter = null;
		for (Parameter parameterTemp : this.getParameters()) {
	        if (parameterTemp.getName().equals(name)) {
	        	parameter = parameterTemp;
	        }
	    }
		return parameter;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void setParameter(Parameter parameter){
		boolean exists = false;
		for (Parameter parameterTemp : this.getParameters()) {
	        if (parameterTemp.getName().equals(parameter.getName())) {
	        	exists = true;
	        }
	    }
		if (exists){
			return;
		} else {
			this.parameters.add(parameter);
		}
	}

	public void clearParameterList(){
		this.parameters.clear();
	}

}
