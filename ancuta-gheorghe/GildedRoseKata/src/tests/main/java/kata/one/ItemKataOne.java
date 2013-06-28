package tests.main.java.kata.one;

import static org.junit.Assert.*;

import main.java.kata.one.Inventory;

import org.junit.Test;

public class ItemKataOne {

	@Test public void  
	check_output_for_updateQuality(){
		String output = "+5 Dexterity Vest 9 19\n" +
					"Aged Brie 1 1\n" +
					"Elixir of the Mongoose 4 6\n" +
					"Sulfuras, Hand of Ragnaros 0 80\n" +
					"Backstage passes to a TAFKAL80ETC concert 14 21\n" +
					"Conjured Mana Cake 2 5\n";
		Inventory inventory = new Inventory();
        inventory.updateQuality();
		assertEquals(output, inventory.toString());
	}

}
