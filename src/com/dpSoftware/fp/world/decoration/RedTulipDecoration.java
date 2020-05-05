package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class RedTulipDecoration extends ImageDecoration {
	private static final String TEXTURE_NAME = "redtulip";
	
	public RedTulipDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Clippers, SHORT_BREAK_TIME, false, false);
		
		drops.addEntry(new LootEntry(Items.Flower, 1, 1, 100));
	}
}
