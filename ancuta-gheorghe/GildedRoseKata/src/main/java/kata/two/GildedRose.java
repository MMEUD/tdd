/**
 * 
 */
package main.java.kata.two;

/**
 * @author ancuta
 *
 */
public class GildedRose {

	public static void main(String[] args) {
		
    	 Inventory inventory = new Inventory();
         inventory.updateQualityAndSellIn();
         System.out.println(inventory.toString());
	}
		
}
