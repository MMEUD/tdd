/**
 * 
 */
package pattern.strategy;

import java.util.Map;

/**
 * @author Ancuta Gheorghe
 *
 */
public class Context {

	private Strategy strategy;
	
	public Context(Strategy strategy){
		this.strategy = strategy;
	}
	
	public Map<String, String> executeStrategy(Map<String, String> mss){
		return this.strategy.execute(mss);
	}
}
