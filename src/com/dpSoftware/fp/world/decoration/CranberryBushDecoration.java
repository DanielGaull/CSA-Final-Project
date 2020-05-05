package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class CranberryBushDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "cranberrybush";
	
	private static final double HB_LEFT_RATIO = 40.0 / 128;
	private static final double HB_TOP_RATIO = 75.0 / 128;
	private static final double HB_RIGHT_RATIO = 85.0 / 128;
	private static final double HB_BOTTOM_RATIO = 121.0 / 128;
	
	public CranberryBushDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Clippers, MED_BREAK_TIME, false, true);
		
		drops.addEntry(new LootEntry(Items.Leaves, 1, 2, 100));
		drops.addEntry(new LootEntry(Items.Cranberries, 1, 5, 100));
		drops.addEntry(new LootEntry(Items.Snow, 1, 1, 25));
		
		addHitbox(HB_LEFT_RATIO, HB_TOP_RATIO, HB_RIGHT_RATIO, HB_BOTTOM_RATIO);
	}
	
}
