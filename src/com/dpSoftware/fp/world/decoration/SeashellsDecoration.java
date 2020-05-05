package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class SeashellsDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "seashells";
	
	public SeashellsDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Pickaxe, INSTANT_BREAK_TIME, false, false);
		
		drops.addEntry(new LootEntry(Items.Seashell, 1, 5, 100));
	}

}
