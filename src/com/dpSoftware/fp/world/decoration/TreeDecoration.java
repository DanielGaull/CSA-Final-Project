package com.dpSoftware.fp.world.decoration;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.entity.SpawnableEntities;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.LootEntry;

public class TreeDecoration extends ImageDecoration {
	
	private BufferedImage cobwebImg;
	private boolean hasWeb;
	private static final double WEB_CHANCE = 2;
	
	private BufferedImage beehiveImg;
	private boolean hasHive;
	private static final double HIVE_CHANCE = 2.75;
	private EntitySpawner spawner;
	private Random random;
	private static final int BEES_MIN = 1;
	private static final int BEES_MAX = 3;
	
	private static final String TEXTURE_NAME = "tree";
	private static final String WEB_TEXTURE_NAME = "treeweb";
	private static final String HIVE_TEXTURE_NAME = "treehive";
	
	private static final double LEAVES_HB_LEFT_RATIO = 31.0 / 128;
	private static final double LEAVES_HB_TOP_RATIO = 18.0 / 128;
	private static final double LEAVES_HB_RIGHT_RATIO = 104.0 / 128;
	private static final double LEAVES_HB_BOTTOM_RATIO = 61.0 / 128;
	private static final double TRUNK_HB_LEFT_RATIO = 51.0 / 128;
	private static final double TRUNK_HB_TOP_RATIO = 61.0 / 128;
	private static final double TRUNK_HB_RIGHT_RATIO = 78.0 / 128;
	private static final double TRUNK_HB_BOTTOM_RATIO = 124.0 / 128;
	
	public TreeDecoration(int tileSize, int worldX, int worldY, Random rand, EntitySpawner spawner, boolean canHaveHive, boolean canHaveWeb) {
		this(TEXTURE_NAME, tileSize, worldX, worldY, rand, spawner, canHaveHive, canHaveWeb);
	}
	public TreeDecoration(String textureName, int tileSize, int worldX, int worldY, Random rand, EntitySpawner spawner, 
			boolean canHaveHive, boolean canHaveWeb) {
		super(tileSize, worldX, worldY, textureName, Items.Axe, LONG_BREAK_TIME, true, false);
		
		if (RandomUtils.doesChanceSucceed(rand, WEB_CHANCE) && canHaveWeb) {
			cobwebImg = super.loadDecorationImg(WEB_TEXTURE_NAME);
			hasWeb = true;
			drops.addEntry(new LootEntry(Items.Web, 1, 2, 100));
		}
		if (RandomUtils.doesChanceSucceed(rand, HIVE_CHANCE) && canHaveHive) {
			beehiveImg = super.loadDecorationImg(HIVE_TEXTURE_NAME);
			hasHive = true;
			drops.addEntry(new LootEntry(Items.Honey, 1, 3, 100));
		}
		this.spawner = spawner;
		this.random = rand;
		
		drops.addEntry(new LootEntry(Items.Wood, 1, 3, 100));
		drops.addEntry(new LootEntry(Items.Leaves, 2, 5, 100));
		
		addHitbox(LEAVES_HB_LEFT_RATIO, LEAVES_HB_TOP_RATIO, LEAVES_HB_RIGHT_RATIO, LEAVES_HB_BOTTOM_RATIO);
		addHitbox(TRUNK_HB_LEFT_RATIO, TRUNK_HB_TOP_RATIO, TRUNK_HB_RIGHT_RATIO, TRUNK_HB_BOTTOM_RATIO);
	}
	
	public void draw(Graphics2D g, Camera c) {
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		
		if (hasHive) {
			g.drawImage(beehiveImg, drawX, drawY, getWidth(), getHeight(), null);
		}
		
		super.draw(g, c);
		
		if (hasWeb) {
			g.drawImage(cobwebImg, drawX, drawY, getWidth(), getHeight(), null);
		}
	}
	
	public void onBreak() {
		if (hasHive) {
			spawner.spawnEntity(SpawnableEntities.Bee, (int) getWorldX(), (int) getWorldY(), 
					RandomUtils.randomIntBetween(random, BEES_MIN, BEES_MAX));
		}
	}
}
