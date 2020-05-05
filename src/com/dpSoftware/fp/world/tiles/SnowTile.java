package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.*;

public class SnowTile extends Tile {

	private static final Color BASE_COLOR = new Color(240, 240, 240);
	
	private static final double SNOWDRIFT_CHANCE = 7.5;
	private static final double ROCKPILE_CHANCE = 1.5;
	private static final double CRANBERRYBUSH_CHANCE = 2.5;
	
	public SnowTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand) {
		super(size, TileTypes.Snow, biome, variation, elevation, moisture, worldX, worldY);
		
		setColor(varyColor(BASE_COLOR, variation));
		if (RandomUtils.doesChanceSucceed(rand, SNOWDRIFT_CHANCE)) {
			decoration = new SnowDriftDecoration(size, worldX, worldY);
		} else if (RandomUtils.doesChanceSucceed(rand, ROCKPILE_CHANCE)) {
			decoration = new RockPileDecoration(size, worldX, worldY);
		} else if (RandomUtils.doesChanceSucceed(rand, CRANBERRYBUSH_CHANCE)) {
			decoration = new CranberryBushDecoration(size, worldX, worldY);
		}
	}

}
