package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.*;
import com.dpSoftware.fp.world.decoration.*;

public class GrassTile extends Tile {

	private static final Color FOREST_COLOR = new Color(0, 125, 0);
	private static final Color FRUITGROVE_COLOR = new Color(0, 185, 0);
	private static final Color PLAINS_COLOR = new Color(117, 168, 50);
	private static final Color SWAMP_COLOR = new Color(0, 110, 5);
	
	private static final double FOREST_TREE_CHANCE = 30;
	private static final double FOREST_STUMP_CHANCE = 4.5;
	private static final double FOREST_BUSH_CHANCE = 20;
	private static final double FOREST_SPIDER_EGG_CHANCE = 1.5;
	private static final double FOREST_APPLETREE_CHANCE = 5.5;
	private static final double FOREST_BLUEBERRYBUSH_CHANCE = 5.5;
	private static final double FOREST_ROCKPILE_CHANCE = 2;
	private static final double FOREST_ROSE_CHANCE = 5;
	private static final double PLAINS_TREE_CHANCE = 2.5;
	private static final double PLAINS_APPLETREE_CHANCE = 15;
	private static final double PLAINS_SUNFLOWER_CHANCE = 4;
	private static final double FRUITGROVE_TREE_CHANCE = 15;
	private static final double FRUITGROVE_BUSH_CHANCE = 10;
	private static final double FRUITGROVE_REDTULIP_CHANCE = 5;
	private static final double FRUITGROVE_PINKTULIP_CHANCE = 5;
	private static final double FRUITGROVE_YELLOWTULIP_CHANCE = 5;
	private static final double SWAMP_TREE_CHANCE = 15;
	
	public GrassTile(int size, Biomes biome, double variation, double elevation,
			double moisture, int worldX, int worldY, Random rand, EntitySpawner spawner) {
		super(size, TileTypes.Grass, biome, variation, elevation, moisture, worldX, worldY);
		
		switch (biome) {
			case Forest:
				setColor(varyColor(FOREST_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, FOREST_TREE_CHANCE)) {
					// For each tree, there's a chance that it generates as a stump instead
					if (RandomUtils.doesChanceSucceed(rand, FOREST_STUMP_CHANCE)) {
						decoration = new TreeStumpDecoration(size, worldX, worldY);
					} else if (RandomUtils.doesChanceSucceed(rand, FOREST_APPLETREE_CHANCE)) {
						decoration = new AppleTreeDecoration(size, worldX, worldY, rand, spawner, true, true);
					} else {
						decoration = new TreeDecoration(size, worldX, worldY, rand, spawner, true, true);
					}
				} else if (RandomUtils.doesChanceSucceed(rand, FOREST_BUSH_CHANCE)) {
					if (RandomUtils.doesChanceSucceed(rand, FOREST_BLUEBERRYBUSH_CHANCE)) {
						decoration = new BlueberryBushDecoration(size, worldX, worldY, rand);
					} else {
						decoration = new BushDecoration(size, worldX, worldY, rand);
					}
				} else if (RandomUtils.doesChanceSucceed(rand, FOREST_SPIDER_EGG_CHANCE)) {
					decoration = new SpiderEggDecoration(size, worldX, worldY, spawner, rand);
				} else if (RandomUtils.doesChanceSucceed(rand, FOREST_ROCKPILE_CHANCE)) {
					decoration = new RockPileDecoration(size, worldX, worldY);
				} else if (RandomUtils.doesChanceSucceed(rand, FOREST_ROSE_CHANCE)) {
					decoration = new RoseDecoration(size, worldX, worldY);
				}
				break;
			case Plains:
				setColor(varyColor(PLAINS_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, PLAINS_TREE_CHANCE)) {
					if (RandomUtils.doesChanceSucceed(rand, PLAINS_APPLETREE_CHANCE)) {
						decoration = new AppleTreeDecoration(size, worldX, worldY, rand, spawner, false, true);
					} else {
						decoration = new TreeDecoration(size, worldX, worldY, rand, spawner, false, true);
					}
				} else if (RandomUtils.doesChanceSucceed(rand, PLAINS_SUNFLOWER_CHANCE)) {
					decoration = new SunflowerDecoration(size, worldX, worldY);
				}
				break;
			case Swamp:
				setColor(varyColor(SWAMP_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, SWAMP_TREE_CHANCE)) {
					decoration = new TreeDecoration(size, worldX, worldY, rand, spawner, false, true);
				}
				break;
			case FruitGrove:
				setColor(varyColor(FRUITGROVE_COLOR, variation));
				if (RandomUtils.doesChanceSucceed(rand, FRUITGROVE_TREE_CHANCE)) {
					decoration = new AppleTreeDecoration(size, worldX, worldY, rand, spawner, true, true);
				} else if (RandomUtils.doesChanceSucceed(rand, FRUITGROVE_BUSH_CHANCE)) {
					decoration = new BlueberryBushDecoration(size, worldX, worldY, rand);
				} else if (RandomUtils.doesChanceSucceed(rand, FRUITGROVE_REDTULIP_CHANCE)) {
					decoration = new RedTulipDecoration(size, worldX, worldY);
				} else if (RandomUtils.doesChanceSucceed(rand, FRUITGROVE_PINKTULIP_CHANCE)) {
					decoration = new PinkTulipDecoration(size, worldX, worldY);
				} else if (RandomUtils.doesChanceSucceed(rand, FRUITGROVE_YELLOWTULIP_CHANCE)) {
					decoration = new YellowTulipDecoration(size, worldX, worldY);
				}
				break;
		}
	}
	
}
