package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.BufferedImageUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Skeleton extends SwingingHumanoidEnemy {

	private static final Color BODY_COLOR = new Color(220, 220, 220);
	private static final Color HAND_COLOR = new Color(200, 200, 200);
	
	private static final int MAX_HEALTH = 100;
	private static final int DEFENSE = 50;
	private static final int SIGHT_RANGE = 25;
	private static final double ATTACK_REACH = 1.25;
	private static final double MOVE_SPEED = 4;
	private static final int BASE_DMG = 20;
	private static final int ATTACK_CD = 2500;
	
	private static final int COINS_MIN = 15;
	private static final int COINS_MAX = 20;
	private static final int XP_MIN = 20;
	private static final int XP_MAX = 30;
	
	private BufferedImage skullImg;
	private static final String SKULL_PATH = getDirectory() + "\\skeleton\\skull";
	
	public Skeleton(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, BODY_COLOR, HAND_COLOR, HAND_COLOR, random, 
				"Skeleton", ATTACK_CD, Items.BasicSword, BASE_DMG, SIGHT_RANGE, ATTACK_REACH);
		
		setMaxHealth(MAX_HEALTH);
		setBaseDefense(DEFENSE);
		setSpeed(MOVE_SPEED);
		
		dropTable.addEntry(new LootEntry(Items.Bone, 0, 3, 100));
		dropTable.addEntry(new LootEntry(Items.Skull, 1, 1, 25));
		dropTable.addEntry(new LootEntry(Items.BasicSword, 1, 1, 5));
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
		
		skullImg = Images.loadImage(SKULL_PATH);
		setBodyVisible(false);
	}
	
	public void draw(Graphics2D g, Camera c) {
		super.draw(g, c);
		
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		
		BufferedImage coloredImg = BufferedImageUtils.getColoredImage(skullImg, getDamageColor());
		GraphicsUtils.drawRotatedImg(coloredImg, getOrientation() - Math.PI / 2, drawX, drawY, getWidth(), getHeight(), 0, 0, true, g);
	}
}
