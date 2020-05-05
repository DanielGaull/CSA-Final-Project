package com.dpSoftware.fp.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.BufferedImageUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.world.World;
import com.dpSoftware.fp.world.tiles.TileTypes;

public abstract class FishEntity extends Creature {

	private BufferedImage texture;
	 // Searches for a random tile to pathfind to, which is up to 5 away
	private static final int MOVE_SEARCH_RANGE = 5;
	private static final double MOVE_SPD = 0.5;
	
	private static final int HEALTH = 15;
	private static final int DEFENSE = 5;
	
	private static final int COINS_MIN = 0;
	private static final int COINS_MAX = 2;
	private static final int XP_MIN = 2;
	private static final int XP_MAX = 5;
	
	public FishEntity(double worldX, double worldY, int size, int tileSize, World world, Random random, String name,
			String textureName) {
		super(worldX, worldY, size, size, tileSize, world, random, name);
		texture = Images.loadImage(textureName);
		
		setMaxHealth(HEALTH);
		setBaseDefense(DEFENSE);
		setSpeed(MOVE_SPD);
		setCanSpeedSwim(true);
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
	}
	
	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		// Update moving
		updateWandering(passedTime, true, MOVE_SEARCH_RANGE, TileTypes.Water);
	}
	public void draw(Graphics2D g, Camera c) {
		double drawAngle = getOrientation();
		BufferedImage drawImage = texture;
		if (drawAngle > Math.PI * 3 / 4 && drawAngle < Math.PI * 5 / 4) {
			drawImage = BufferedImageUtils.getSideFlippedImage(texture);
			drawAngle = (drawAngle + Math.PI) % (Math.PI * 2);
		}
		GraphicsUtils.drawRotatedImg(BufferedImageUtils.getColoredImage(drawImage, getDamageColor()), drawAngle, 
				getDrawX(c), getDrawY(c), getWidth(), getHeight(), 0, 0, true, g);
	}
}
