package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class CactusDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "cactus";
	
	private static final double MAIN_HB_LEFT_RATIO = 49.0 / 128;
	private static final double MAIN_HB_TOP_RATIO = 20.0 / 128;
	private static final double MAIN_HB_RIGHT_RATIO = 71.0 / 128;
	private static final double MAIN_HB_BOTTOM_RATIO = 106.0 / 128;
	
	public CactusDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Axe, MODERATE_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Cactus, 1, 3, 100));
		
		addHitbox(MAIN_HB_LEFT_RATIO, MAIN_HB_TOP_RATIO, MAIN_HB_RIGHT_RATIO, MAIN_HB_BOTTOM_RATIO);
	}

}
