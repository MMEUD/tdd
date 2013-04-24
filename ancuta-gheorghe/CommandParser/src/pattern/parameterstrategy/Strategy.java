/**
 * 
 */
package pattern.parameterstrategy;

import java.util.ArrayList;

import pattern.utils.Parameter;


/**
 * @author Ancuta Gheorghe
 *
 */
public interface Strategy {

	public ArrayList<Parameter> execute(ArrayList<Parameter> mss);
	
}
