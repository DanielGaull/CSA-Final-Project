package com.dpSoftware.fp.world.decoration;

import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class BlueberryBushDecoration extends BushDecoration {

	private static final String TEXTURE_NAME = "blueberrybush";
	
	public BlueberryBushDecoration(int tileSize, int worldX, int worldY, Random rand) {
		super(TEXTURE_NAME, tileSize, worldX, worldY, rand);
		
		drops.addEntry(new LootEntry(Items.Blueberries, 2, 6, 100));
	}

}
