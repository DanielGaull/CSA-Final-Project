package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class SandMoundDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "sandmound";
	
	private static final double HB1_LEFT_RATIO = 15.0 / 128;
	private static final double HB1_TOP_RATIO = 55.0 / 128;
	private static final double HB1_RIGHT_RATIO = 56.0 / 128;
	private static final double HB1_BOTTOM_RATIO = 101.0 / 128;
	private static final double HB2_LEFT_RATIO = 36.0 / 128;
	private static final double HB2_TOP_RATIO = 61.0 / 128;
	private static final double HB2_RIGHT_RATIO = 109.0 / 128;
	private static final double HB2_BOTTOM_RATIO = 107.0 / 128;
	private static final double HB3_LEFT_RATIO = 72.0 / 128;
	private static final double HB3_TOP_RATIO = 25.0 / 128;
	private static final double HB3_RIGHT_RATIO = 106.0 / 128;
	private static final double HB3_BOTTOM_RATIO = 61.0 / 128;
	
	public SandMoundDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Shovel, MODERATE_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Sand, 1, 4, 100));
		
		addHitbox(HB1_LEFT_RATIO, HB1_TOP_RATIO, HB1_RIGHT_RATIO, HB1_BOTTOM_RATIO);
		addHitbox(HB2_LEFT_RATIO, HB2_TOP_RATIO, HB2_RIGHT_RATIO, HB2_BOTTOM_RATIO);
		addHitbox(HB3_LEFT_RATIO, HB3_TOP_RATIO, HB3_RIGHT_RATIO, HB3_BOTTOM_RATIO);
	}
	
}
