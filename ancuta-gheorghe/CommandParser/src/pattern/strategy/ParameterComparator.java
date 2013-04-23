/**
 * 
 */
package pattern.strategy;

import java.util.Comparator;

import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ParameterComparator implements Comparator<Parameter> {

	public int compare(Parameter pr1, Parameter pr2) {
		return pr1.getName().compareTo(pr2.getName());
	}

}
