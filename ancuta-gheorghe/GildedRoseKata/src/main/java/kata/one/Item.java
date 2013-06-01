/**
 * 
 */
package main.java.kata.one;

/**
 * @author ancuta
 *
 */
public class Item {

	private String name;
	private int sellIn;

	private int quality;

	public Item(String name, int sellIn, int quality) {
		super();
		this.name = name;
		this.sellIn = sellIn;
		this.quality = quality;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSellIn() {
		return sellIn;
	}

	public void setSellIn(int sellIn) {
		this.sellIn = sellIn;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
	
	public boolean diffrentFromString(String string) {
		return this.name != string;
	}

	public boolean qualityIsSmallerThan50() {
		return this.quality < 50;
	}

	public boolean sellInIsSmallerThan0() {
		return this.sellIn < 0;
	}

	public boolean qualityIsBiggerThan0() {
		return this.quality > 0;
	}

	public void decreaseSellIn() {
		this.setSellIn(this.getSellIn() - 1);
	}

	public void increaseQuality() {
		this.setQuality(this.getQuality() + 1);
	}

	public void decreaseQuality() {
		this.setQuality(this.getQuality() - 1);
	}
}
