package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.*;

public class StoneTile extends Tile {

	private static final Color BASE_COLOR = new Color(127, 127, 127);
	
	private static final double ROCK_PILE_CHANCE = 7.5;
	
	public StoneTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand) {
		super(size, TileTypes.Stone, biome, variation, elevation, moisture, worldX, worldY);
		
		setColor(varyColor(BASE_COLOR, variation));
		if (RandomUtils.doesChanceSucceed(rand, ROCK_PILE_CHANCE)) {
			decoration = new RockPileDecoration(size, worldX, worldY);
		}
	}

}
