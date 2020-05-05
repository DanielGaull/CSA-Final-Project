package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.*;

public class WaterTile extends Tile {

	private static final Color BASE_COLOR = new Color(14, 111, 196);
	private static final Color SWAMP_COLOR = new Color(7, 150, 120);
	
	private static final double OCEAN_SEAWEED_CHANCE = 25;
	
	public WaterTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand) {
		super(size, TileTypes.Water, biome, variation, elevation, moisture, worldX, worldY);
		
		switch (biome) {
			case Swamp:
				setColor(varyColor(SWAMP_COLOR, variation));
				break;
			case Ocean:
				setColor(varyColor(BASE_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, OCEAN_SEAWEED_CHANCE)) {
					decoration = new SeaweedDecoration(size, worldX, worldY);
				}
				break;
		}
	}

}
