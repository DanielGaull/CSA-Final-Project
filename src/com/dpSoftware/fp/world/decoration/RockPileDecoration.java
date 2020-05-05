package com.dpSoftware.fp.world.decoration;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;

public class RockPileDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "rockpile";
	
	private static final double HB1_LEFT_RATIO = 17.0 / 128;
	private static final double HB1_TOP_RATIO = 98.0 / 128;
	private static final double HB1_RIGHT_RATIO = 107.0 / 128;
	private static final double HB1_BOTTOM_RATIO = 128.0 / 128;
	private static final double HB2_LEFT_RATIO = 26.0 / 128;
	private static final double HB2_TOP_RATIO = 68.0 / 128;
	private static final double HB2_RIGHT_RATIO = 88.0 / 128;
	private static final double HB2_BOTTOM_RATIO = 98.0 / 128;
	private static final double HB3_LEFT_RATIO = 39.0 / 128;
	private static final double HB3_TOP_RATIO = 44.0 / 128;
	private static final double HB3_RIGHT_RATIO = 72.0 / 128;
	private static final double HB3_BOTTOM_RATIO = 68.0 / 128;
	
	public RockPileDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Pickaxe, LONG_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Stone, 2, 5, 100));
		drops.addEntry(new LootEntry(Items.Metal, 1, 3, 75));
		drops.addEntry(new LootEntry(Items.Gem, 1, 1, 25));
		drops.addEntry(new LootEntry(Items.Cobalt, 1, 1, 5));
		
		addHitbox(HB1_LEFT_RATIO, HB1_TOP_RATIO, HB1_RIGHT_RATIO, HB1_BOTTOM_RATIO);
		addHitbox(HB2_LEFT_RATIO, HB2_TOP_RATIO, HB2_RIGHT_RATIO, HB2_BOTTOM_RATIO);
		addHitbox(HB3_LEFT_RATIO, HB3_TOP_RATIO, HB3_RIGHT_RATIO, HB3_BOTTOM_RATIO);
	}

}
