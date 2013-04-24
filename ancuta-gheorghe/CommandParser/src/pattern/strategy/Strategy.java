/**
 * 
 */
package pattern.strategy;

import java.util.Map;


/**
 * @author Ancuta Gheorghe
 *
 */
public interface Strategy {

	public Map<String, String> execute(Map<String, String> mss);
	
}
