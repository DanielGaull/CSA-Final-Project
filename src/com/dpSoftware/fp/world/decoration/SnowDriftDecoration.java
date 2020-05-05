package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class SnowDriftDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "snowdrift";
	
	private static final double HB1_LEFT_RATIO = 34.0 / 128;
	private static final double HB1_TOP_RATIO = 77.0 / 128;
	private static final double HB1_RIGHT_RATIO = 97.0 / 128;
	private static final double HB1_BOTTOM_RATIO = 112.0 / 128;
	private static final double HB2_LEFT_RATIO = 52.0 / 128;
	private static final double HB2_TOP_RATIO = 49.0 / 128;
	private static final double HB2_RIGHT_RATIO = 86.0 / 128;
	private static final double HB2_BOTTOM_RATIO = 77.0 / 128;
	
	public SnowDriftDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Shovel, MODERATE_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Snow, 1, 4, 100));
		drops.addEntry(new LootEntry(Items.Ice, 0, 2, 100));
		
		addHitbox(HB1_LEFT_RATIO, HB1_TOP_RATIO, HB1_RIGHT_RATIO, HB1_BOTTOM_RATIO);
		addHitbox(HB2_LEFT_RATIO, HB2_TOP_RATIO, HB2_RIGHT_RATIO, HB2_BOTTOM_RATIO);
	}
	
}
