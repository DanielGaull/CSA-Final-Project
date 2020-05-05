package com.dpSoftware.fp.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.spi.ImageInputStreamSpi;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.BufferedImageUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Bee extends Creature {

	private static final String SPRITESHEET_FOLDER = getDirectory() + "\\bee\\beesheet";
	private static final int ANIMATION_FRAMES = 5;
	private static final int ANIMATION_WAIT_TIME = 25;
	private BufferedImage spriteSheet;
	private BufferedImage[] flightAnimationFrames;
	private int currentFrame;
	private boolean animationDirection;
	private Timer animationTimer;
	
	private static final int HEALTH = 1; // One-shot no matter what
	private static final int ATT_DMG = 10;
	private static final double ATTACK_RANGE = 0.5;
	private static final double SIGHT_RANGE = 10;
	private Timer attackTimer;
	private static final int ATTACK_COOLDOWN = 1500; // Attack somewhat quickly
	private static final double MOVE_SPEED = 5;
	
	private static final int COINS_MIN = 0;
	private static final int COINS_MAX = 1;
	private static final int XP_MIN = 3;
	private static final int XP_MAX = 7;
	
	public Bee(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, size, tileSize, world, random, "Bee");
		
		animationTimer = new Timer(ANIMATION_WAIT_TIME);
		animationDirection = true;
		
		spriteSheet = Images.loadImage(SPRITESHEET_FOLDER);
		flightAnimationFrames = new BufferedImage[ANIMATION_FRAMES];
		int frameWidth = spriteSheet.getWidth() / ANIMATION_FRAMES;
		int frameHeight = spriteSheet.getHeight();
		for (int i = 0; i < ANIMATION_FRAMES; i++) {
			flightAnimationFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
		}
		
		setHealth(HEALTH);
		setBaseDamage(ATT_DMG);
		setSpeed(MOVE_SPEED);
		setCanPhase(true);
		attackReach = ATTACK_RANGE;
		
		attackTimer = new Timer(ATTACK_COOLDOWN);
		
		dropTable.addEntry(new LootEntry(Items.Honey, 0, 2, 100));
		dropTable.addEntry(new LootEntry(Items.Stinger, 1, 1, 50));
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
	}
	
	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		if (targetEntity == null || targetEntity.isDisposed()) {
			targetNearestPlayer(SIGHT_RANGE);
		}
		updateFollowingTarget(passedTime);
		if (inTargetRange && targetEntity != null) {
			attackTimer.update(passedTime);
			if (attackTimer.query()) {
				targetEntity.damage(getDamage(), new DamageCause(DamageCauses.Entity, this));
				attackTimer.reset();
			}
		}
		if (targetEntity != null) {
			face(getFaceAngleTo(targetEntity.getDrawCenter(c), c));
		}
		
		if (isMoving) {
			animationTimer.update(passedTime);
			if (animationTimer.query()) {
				if (animationDirection) {
					currentFrame++;
					if (currentFrame + 1 >= ANIMATION_FRAMES) {
						animationDirection = false;
					}
				} else {
					currentFrame--;
					if (currentFrame - 1 <= 0) {
						animationDirection = true;
					}
				}
			}
		}
	}

	public void draw(Graphics2D g, Camera c) {
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		
		BufferedImage frameImg = BufferedImageUtils.getColoredImage(flightAnimationFrames[currentFrame], getDamageColor());
		GraphicsUtils.drawRotatedImg(frameImg, facingAngle + Math.PI / 2, drawX, drawY, 
				getWidth(), getHeight(), 0, 0, true, g);
	}

}
