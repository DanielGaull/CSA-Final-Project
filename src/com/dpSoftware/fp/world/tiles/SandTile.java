package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.*;

public class SandTile extends Tile {

	private static final Color BEACH_COLOR = new Color(255, 215, 176);
	private static final Color DESERT_COLOR = new Color(232, 208, 75);
	
	private static final double DESERT_CACTUS_CHANCE = 2.5;
	private static final double DESERT_MOUND_CHANCE = 1.75;
	private static final double BEACH_SEASHELL_CHANCE = 0.75;
	
	public SandTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand) {
		super(size, TileTypes.Sand, biome, variation, elevation, moisture, worldX, worldY);
		
		switch (biome) {
			case Beach:
				setColor(varyColor(BEACH_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, BEACH_SEASHELL_CHANCE)) {
					decoration = new SeashellsDecoration(size, worldX, worldY);
				}
				break;
			case Desert:
				setColor(varyColor(DESERT_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, DESERT_CACTUS_CHANCE)) {
					decoration = new CactusDecoration(size, worldX, worldY);
				} else if (RandomUtils.doesChanceSucceed(rand, DESERT_MOUND_CHANCE)) {
					decoration = new SandMoundDecoration(size, worldX, worldY);
				}
				break;
		}
	}

}
