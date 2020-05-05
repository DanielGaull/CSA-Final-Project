package com.dpSoftware.fp.world.decoration;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.LootEntry;

public class BushDecoration extends ImageDecoration {
	
	private BufferedImage cobwebImg;
	private boolean hasWeb;
	private static final double WEB_CHANCE = 3;
	
	private static final String TEXTURE_NAME = "bush";
	private static final String WEB_TEXTURE_NAME = "bushweb";
	
	private static final double HB_LEFT_RATIO = 23.0 / 128;
	private static final double HB_TOP_RATIO = 73.0 / 128;
	private static final double HB_RIGHT_RATIO = 114.0 / 128;
	private static final double HB_BOTTOM_RATIO = 113.0 / 128;
	
	public BushDecoration(String textureName, int tileSize, int worldX, int worldY, Random rand) {
		super(tileSize, worldX, worldY, textureName, Items.Clippers, MED_BREAK_TIME, false, true);
		
		cobwebImg = super.loadDecorationImg(WEB_TEXTURE_NAME);
		if (RandomUtils.doesChanceSucceed(rand, WEB_CHANCE)) {
			hasWeb = true;
			drops.addEntry(new LootEntry(Items.Web, 1, 2, 100));
		}
		
		drops.addEntry(new LootEntry(Items.Leaves, 1, 3, 100));
		
		addHitbox(HB_LEFT_RATIO, HB_TOP_RATIO, HB_RIGHT_RATIO, HB_BOTTOM_RATIO);
	}
	public BushDecoration(int tileSize, int worldX, int worldY, Random rand) {
		this(TEXTURE_NAME, tileSize, worldX, worldY, rand);
	}
	
	public void draw(Graphics2D g, Camera c) {
		super.draw(g, c);
		
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		
		if (hasWeb) {
			g.drawImage(cobwebImg, drawX, drawY, getWidth(), getHeight(), null);
		}
	}
	
}
