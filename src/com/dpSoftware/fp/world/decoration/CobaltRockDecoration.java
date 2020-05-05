package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class CobaltRockDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "cobaltrock";
	
	private static final double HB_LEFT_RATIO = 18.0 / 128;
	private static final double HB_TOP_RATIO = 30.0 / 128;
	private static final double HB_RIGHT_RATIO = 107.0 / 128;
	private static final double HB_BOTTOM_RATIO = 108.0 / 128;
	
	public CobaltRockDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Pickaxe, LONG_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Cobalt, 1, 4, 100));
		
		addHitbox(HB_LEFT_RATIO, HB_TOP_RATIO, HB_RIGHT_RATIO, HB_BOTTOM_RATIO);
	}
	
}
