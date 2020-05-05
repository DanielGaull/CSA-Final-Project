package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class SeaweedDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "seaweed";
	
	public SeaweedDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Clippers, MED_BREAK_TIME, false, true);
		
		drops.addEntry(new LootEntry(Items.Seaweed, 1, 3, 100));
	}

}
