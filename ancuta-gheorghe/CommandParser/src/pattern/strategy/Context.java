/**
 * 
 */
package pattern.strategy;

import java.util.ArrayList;
import java.util.Map;

import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class Context {

	private Strategy strategy;
	
	public Context(Strategy strategy){
		this.strategy = strategy;
	}
	
	public ArrayList<Parameter> executeStrategy(ArrayList<Parameter> mss){
		return this.strategy.execute(mss);
	}
}
