/**
 * 
 */
package pattern.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class OrderArraylist implements Strategy {

	public Map<String, String> execute(Map<String, String> mss) {
		Map<String, String> copy = new TreeMap<String, String>(mss);
		return copy;
	}

	public ArrayList<Parameter> execute(ArrayList<Parameter> mss) {
		Collections.sort(mss, new Comparator<Parameter>() {
		    public int compare(Parameter ns1, Parameter ns2) {
				return ns1.getName().compareTo(ns2.getName());
		    }
		});
		return mss;
	}

}
