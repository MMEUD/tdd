/**
 * 
 */
package main.java.kata.two;

import java.util.Arrays;

/**
 * @author ancuta
 *
 */
public class Inventory {

	private static final int _ZERO = 0;
	private static final String _5_DEXTERITY_VEST = "+5 Dexterity Vest";
	private static final String CONJURED_MANA_CAKE = "Conjured Mana Cake";
	private static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
	private static final String ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose";
	private static final String BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
	private static final String AGED_BRIE = "Aged Brie";
	private Item[] items;

	@Override
	public String toString() {
		String output = "";
		for (int i = 0; i < items.length; i++){
			output += items[i].getName() + " " + items[1].getSellIn() + " " + items[i].getQuality() + "\n";
		}
		return output;
	}

	
	public Inventory(Item[] items) {
		super();
		this.items = items;
	}

	public Inventory() {
		super();
		items = new Item[] {
					new Item(_5_DEXTERITY_VEST, 10, 20), 
					new Item(AGED_BRIE, 2, 0),
					new Item(ELIXIR_OF_THE_MONGOOSE, 5, 7),
					new Item(SULFURAS_HAND_OF_RAGNAROS, 0, 80),
					new Item(BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT, 15, 20),
					new Item(CONJURED_MANA_CAKE, 3, 6) 
				};
	}
	
	public void updateQualityAndSellIn() {
		for (int i = 0; i < items.length; i++) {
			if (itemsNameIsDifferentFrom(i, AGED_BRIE)
					&& itemsNameIsDifferentFrom(i, BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
				if (items[i].getQuality() > 0) {
					if (itemsNameIsDifferentFrom(i, SULFURAS_HAND_OF_RAGNAROS)) {
						decreaseQuality(i);
					}
				}
			} else {
				if (items[i].getQuality() < 50) {
					increaseQuality(i);

					if (!itemsNameIsDifferentFrom(i, BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
						if (items[i].getSellIn() < 11) {
							if (items[i].getQuality() < 50) {
								increaseQuality(i);
							}
						}

						if (items[i].getSellIn() < 6) {
							if (items[i].getQuality() < 50) {
								increaseQuality(i);
							}
						}
					}
				}
			}

			if (itemsNameIsDifferentFrom(i, SULFURAS_HAND_OF_RAGNAROS)) {
				decreaseSellIn(i);
			}

			if (items[i].getSellIn() < 0) {
				if (itemsNameIsDifferentFrom(i, AGED_BRIE)) {
					if (itemsNameIsDifferentFrom(i, BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
						if (items[i].getQuality() > 0) {
							if (itemsNameIsDifferentFrom(i, SULFURAS_HAND_OF_RAGNAROS)) {
								decreaseQuality(i);
							}
						}
					} else {
						items[i].setQuality(_ZERO);
					}
				} else {
					if (items[i].getQuality() < 50) {
						increaseQuality(i);
					}
				}
			}
		}
	}


	private boolean itemsNameIsDifferentFrom(int i, String string) {
		return items[i].getName() != string;
	}


	private void decreaseSellIn(int i) {
		items[i].setSellIn(items[i].getSellIn() - 1);
	}


	private void increaseQuality(int i) {
		items[i].setQuality(items[i].getQuality() + 1);
	}


	private void decreaseQuality(int i) {
		items[i].setQuality(items[i].getQuality() - 1);
	}
}