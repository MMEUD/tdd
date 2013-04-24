/**
 * 
 */
package pattern.strategy;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ancuta Gheorghe
 *
 */
public class OrderMap implements Strategy {

	public Map<String, String> execute(Map<String, String> mss) {
		Map<String, String> copy = new TreeMap<String, String>(mss);
		return copy;
	}

}
