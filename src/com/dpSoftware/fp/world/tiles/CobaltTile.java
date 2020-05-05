package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.CobaltRockDecoration;

public class CobaltTile extends Tile {

	private static final Color COBALT_COLOR = new Color(21, 0, 143);
	
	private static final double COBALT_ROCK_CHANCE = 25;
	
	public CobaltTile(int size, Biomes biome, double variation, double elevation, double moisture,
			int worldX, int worldY, Random rand) {
		super(size, TileTypes.Cobalt, biome, variation, elevation, moisture, worldX, worldY);
		
		setColor(varyColor(COBALT_COLOR, variation));
		
		if (RandomUtils.doesChanceSucceed(rand, COBALT_ROCK_CHANCE)) {
			decoration = new CobaltRockDecoration(size, worldX, worldY);
		}
	}

}
