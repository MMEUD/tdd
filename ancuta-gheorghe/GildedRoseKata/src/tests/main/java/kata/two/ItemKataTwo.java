package tests.main.java.kata.two;

import static org.junit.Assert.*;

import main.java.kata.two.Inventory;

import org.junit.Test;

public class ItemKataTwo {

	@Test public void  
	check_output_for_updateQuality(){
		String output = "+5 Dexterity Vest 1 19\n" +
					"Aged Brie 1 1\n" +
					"Elixir of the Mongoose 1 6\n" +
					"Sulfuras, Hand of Ragnaros 1 80\n" +
					"Backstage passes to a TAFKAL80ETC concert 1 21\n" +
					"Conjured Mana Cake 1 5\n";
		Inventory inventory = new Inventory();
        inventory.updateQualityAndSellIn();
		assertEquals(output, inventory.toString());
	}

}
