package com.dpSoftware.fp.world.decoration;

import java.util.Random;

import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class AppleTreeDecoration extends TreeDecoration {

	private static final String TEXTURE_NAME = "appletree";
	
	public AppleTreeDecoration(int tileSize, int worldX, int worldY, Random rand, 
			EntitySpawner spawner, boolean canHaveHive, boolean canHaveWeb) {
		super(TEXTURE_NAME, tileSize, worldX, worldY, rand, spawner, canHaveHive, canHaveWeb);
		
		drops.addEntry(new LootEntry(Items.Apple, 1, 4, 100));
	}

}
