/**
 * 
 */
package pattern.strategy;

import java.util.Comparator;

import pattern.utils.Namespace;

/**
 * @author Ancuta Gheorghe
 *
 */
public class NamespaceComparator implements Comparator<Namespace> {

	public int compare(Namespace ns1, Namespace ns2) {
		return ns1.getName().compareTo(ns2.getName());
	}

}
