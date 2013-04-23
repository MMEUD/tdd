/**
 * 
 */
package pattern.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pattern.utils.Namespace;

/**
 * @author Ancuta Gheorghe
 *
 */
public class OrderNamespace implements Strategy{

	public ArrayList<Namespace> cast(ArrayList<Object> arrayList){
		ArrayList<Namespace> arrayListTemp = new ArrayList<Namespace>();
		for (Object o: arrayList){
			arrayListTemp.add((Namespace) o);
		}
		return arrayListTemp;
	}
	public ArrayList<Object> execute(ArrayList<Object> arrayList) {
		ArrayList<Namespace> arrayListTemp = this.cast(arrayList);
		Collections.sort(arrayListTemp, new Comparator<Namespace>() {
		    public int compare(Namespace ns1, Namespace ns2) {
				return ns1.getName().compareTo(ns2.getName());
		    }
		});
		return null;
	}

}
