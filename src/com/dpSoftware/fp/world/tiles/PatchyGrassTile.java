package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.decoration.AmaryllisDecoration;
import com.dpSoftware.fp.world.decoration.BushDecoration;

public class PatchyGrassTile extends Tile {

	private static final Color SHRUBLAND_COLOR = new Color(171, 173, 28);
	
	private static final double SHRUBLAND_BUSH_CHANCE = 20;
	private static final double SHRUBLAND_AMARYLLIS_CHANCE = 4;
	
	public PatchyGrassTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand) {
		super(size, TileTypes.PatchyGrass, biome, variation, elevation, moisture, worldX, worldY);
		
		switch (biome) {
			case Shrubland:
				setColor(varyColor(SHRUBLAND_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, SHRUBLAND_BUSH_CHANCE)) {
					decoration = new BushDecoration(size, worldX, worldY, rand);
				} else if (RandomUtils.doesChanceSucceed(rand, SHRUBLAND_AMARYLLIS_CHANCE)) {
					decoration = new AmaryllisDecoration(size, worldX, worldY);
				}
				break;
		}
	}

}
