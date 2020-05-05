package com.dpSoftware.fp.world.decoration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.world.LootEntry;

public class TreeStumpDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "treestump";
	
	private static final double HB1_LEFT_RATIO = 51.0 / 128;
	private static final double HB1_TOP_RATIO = 85.0 / 128;
	private static final double HB1_RIGHT_RATIO = 79.0 / 128;
	private static final double HB1_BOTTOM_RATIO = 125.0 / 128;
	
	public TreeStumpDecoration(int tileSize, int worldX, int worldY) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Axe, MODERATE_BREAK_TIME, true, false);
		
		drops.addEntry(new LootEntry(Items.Wood, 1, 3, 100));
		
		addHitbox(HB1_LEFT_RATIO, HB1_TOP_RATIO, HB1_RIGHT_RATIO, HB1_BOTTOM_RATIO);
	}
}
