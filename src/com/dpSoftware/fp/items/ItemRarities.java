package com.dpSoftware.fp.items;

import java.awt.Color;

public enum ItemRarities {
	Common(new Color(240, 240, 240), "Common"),
	Uncommon(new Color(0, 209, 3), "Uncommon"),
	Rare(new Color(44, 195, 245), "Rare"),
	Epic(new Color(175, 44, 245), "Epic"),
	Legendary(new Color(252, 186, 3), "Legendary");
	
	private final Color color;
	private final String name;
	ItemRarities(Color color, String name) {
		this.color = color;
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	public String toString() {
		return name;
	}
}
