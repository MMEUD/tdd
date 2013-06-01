/**
 * 
 */
package main.java.kata.one;

/**
 * @author ancuta
 *
 */
public class Inventory {

	private static final String CONJURED_MANA_CAKE = "Conjured Mana Cake";
	private static final String BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
	private static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
	private static final String ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose";
	private static final String AGED_BRIE = "Aged Brie";
	private static final String _5_DEXTERITY_VEST = "+5 Dexterity Vest";
	public Item[] items;

	public Item[] getItems() {
		return items;
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

	public void updateQuality() {
		for (int i = 0; i < items.length; i++) {
			if (items[i].diffrentFromString(AGED_BRIE)
					&& items[i].diffrentFromString(BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
				if (items[i].qualityIsBiggerThan0()) {
					if (items[i].diffrentFromString(SULFURAS_HAND_OF_RAGNAROS)) {
						items[i].decreaseQuality();
					}
				}
			} else {
				if (items[i].qualityIsSmallerThan50()) {
					items[i].increaseQuality();

					if (items[i].diffrentFromString(BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
						if (items[i].getSellIn() < 11) {
							if (items[i].qualityIsSmallerThan50()) {
								items[i].increaseQuality();
							}
						}

						if (items[i].getSellIn() < 6) {
							if (items[i].qualityIsSmallerThan50()) {
								items[i].increaseQuality();
							}
						}
					}
				}
			}

			if (items[i].diffrentFromString(SULFURAS_HAND_OF_RAGNAROS)) {
				items[i].decreaseSellIn();
			}

			if (items[i].sellInIsSmallerThan0()) {
				if (items[i].diffrentFromString(AGED_BRIE)) {
					if (items[i].diffrentFromString(BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT)) {
						if (items[i].qualityIsBiggerThan0()) {
							if (items[i].diffrentFromString(SULFURAS_HAND_OF_RAGNAROS)) {
								items[i].decreaseQuality();
							}
						}
					} else {
						items[i].setQuality(items[i].getQuality()
								- items[i].getQuality());
					}
				} else {
					if (items[i].qualityIsSmallerThan50()) {
						items[i].increaseQuality();
					}
				}
			}
		}
	}


	
	public String toString(){
		String output = "";
		for (Item item: items){
       	 output += item.getName() + " " + item.getSellIn() + " " + item.getQuality() + "\n";
        }
		return output;
	}
}