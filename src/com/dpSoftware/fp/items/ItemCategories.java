package com.dpSoftware.fp.items;

public enum ItemCategories {

	None(0),
	Tool(1),
	Sword(2),
	Shield(3),
	Food(4),
	Summon(5),
	;
	
	private final int value;
	ItemCategories(final int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
}
