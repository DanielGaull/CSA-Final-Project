package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.entity.projectile.Projectiles;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.BufferedImageUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.Input;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Wizard extends Humanoid {

	private static final Color SKIN_COLOR = new Color(150, 95, 65);
	private static final Color HAND_COLOR = new Color(0, 0, 25);
	
	private static final String FOLDER = getDirectory() + "\\wizard";
	
	private static final int FIREBALL_COOLDOWN = 3500;
	private Timer fireballTimer;
	
	private static final double SIGHT_RANGE = 20;
	private static final double ATTACK_RANGE = 5;
	
	private static final String WIZARD_HAT_FILE_NAME = "wizardhat";
	private BufferedImage hatImg;
	private BufferedImage upsideDownHatImg;
	private static final double WIZARD_HAT_OFFSET_RATIO = 0.2;
	private final int wizardHatOffset;
	
	private static final int COINS_MIN = 1;
	private static final int COINS_MAX = 5;
	private static final int XP_MIN = 10;
	private static final int XP_MAX = 20;

	public Wizard(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, SKIN_COLOR, HAND_COLOR, HAND_COLOR, random, "Wizard");
		
		hatImg = Images.loadImage(FOLDER + "\\" + WIZARD_HAT_FILE_NAME);
		upsideDownHatImg = BufferedImageUtils.getUpsideDownImage(hatImg);
		wizardHatOffset = (int) (WIZARD_HAT_OFFSET_RATIO * size);
		
		fireballTimer = new Timer(FIREBALL_COOLDOWN);
		targetEntity = null;
		
		attackReach = ATTACK_RANGE;
		
		dropTable.addEntry(new LootEntry(Items.Gem, 1, 2, 50));
		dropTable.addEntry(new LootEntry(Items.MoltenRock, 1, 3, 100));
		dropTable.addEntry(new LootEntry(Items.Scroll, 1, 3, 100));
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		if (targetEntity == null) {
			targetNearestPlayer(SIGHT_RANGE);
		}
		
		updateFollowingTarget(passedTime);
		
		if (inTargetRange) {
			fireballTimer.update(passedTime);
			if (fireballTimer.query()) {
				world.attemptLaunch(this, Projectiles.Fireball, targetEntity.getCenter());
				fireballTimer.reset();
			}
		}
		
		if (targetEntity != null) {
			face(getFaceAngleTo(targetEntity.getDrawCenter(c), c));
		}
	}
	public void draw(Graphics2D g, Camera c) {
		super.draw(g, c);
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		if (isUpsideDown()) {
			g.drawImage(upsideDownHatImg, drawX, drawY + getSize() - wizardHatOffset, getSize(), getSize(), null);
		} else {
			g.drawImage(hatImg, drawX, drawY - getSize() + wizardHatOffset, getSize(), getSize(), null); 
		}
	}
}
