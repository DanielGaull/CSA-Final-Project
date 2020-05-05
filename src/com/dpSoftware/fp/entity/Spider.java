package com.dpSoftware.fp.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Vector;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Spider extends Creature {

	private double[] legAngles;
	private double[] legAngleOffsets;
	private BufferedImage eyesImg; // An image of all the eyes, that will be easily rotated
	private static final int LEGS = 8;
	private static final int EYES = 8;
	
	// Variables/constants for the leg rotations (when the spider is moving)
	private int legDirection;
	private double legProgress;
	private static final double LEG_SPD = 0.5;
	private static final double LEG_PROGRESS_MAX = 0.1;
	
	protected static final Color BODY_COLOR = new Color(46, 40, 33);
	protected static final Color EYE_COLOR = new Color(179, 0, 15);
	private Color bodyColor;
	private Color eyeColor;
	
	private static final double LEG_LENGTH_RATIO = 0.9;
	private final int legLength;
	private static final double HEAD_SIZE_RATIO = 0.75;
	private final int headSize;
	private static final double HEAD_SPACING_RATIO = 0.75;
	private final int headSpacing;
	private static final double EYE_SIZE_RATIO = 0.09;
	private final int eyeSize;
	private static final double LEG_WIDTH_RATIO = 0.15;
	private final int legWidth;
	private static final double EYE_SPACING_RATIO = 0.05;
	private final double eyeSpacing;
	
	private static final int MAX_HEALTH = 75;
	private static final int DEFENSE = 50;
	private static final int ATT_DMG = 20;
	private static final int SIGHT_RANGE = 20;
	private static final double ATT_RANGE = 1.5;
	
	private Timer attackTimer;
	private static final long ATTACK_COOLDOWN = 2500;
	private static final double MOVE_SPEED = 7; // Slightly faster than player's walking speed
	
	private static final int COINS_MIN = 0;
	private static final int COINS_MAX = 2;
	private static final int XP_MIN = 5;
	private static final int XP_MAX = 15;
	
	private static final double START_LEG_ANGLE = Math.PI / 3;
	private static final double LEG_ANGLE_SPACING = ((START_LEG_ANGLE % Math.PI + Math.PI) - START_LEG_ANGLE) / LEGS;
	
	public Spider(double worldX, double worldY, int size, int tileSize, World world, Random random,
			Color bodyColor, Color eyeColor) {
		super(worldX, worldY, size, size, tileSize, world, random, "Spider");
		
		this.eyeColor = eyeColor;
		this.bodyColor = bodyColor;
		
		setMaxHealth(MAX_HEALTH);
		setBaseDefense(DEFENSE);
		setBaseDamage(ATT_DMG);
		
		legLength = (int) (LEG_LENGTH_RATIO * size);
		headSize = (int) (HEAD_SIZE_RATIO * size);
		headSpacing = (int) (HEAD_SPACING_RATIO * size);
		eyeSize = (int) (EYE_SIZE_RATIO * size);
		legWidth = (int) (LEG_WIDTH_RATIO * size);
		eyeSpacing = EYE_SPACING_RATIO * size;
		
		legAngles = new double[LEGS];
		legAngleOffsets = new double[LEGS];
		
		double legAngle = START_LEG_ANGLE;
		// Do the first half of the legs on the left side
		for (int i = 0; i < LEGS / 2; i++) {
			legAngles[i] = legAngle;
			legAngleOffsets[i] = 0;
			legAngle += LEG_ANGLE_SPACING;
		}
		// Now do the other half of the legs (just flipped upside down versions of the bottom legs)
		legAngle = START_LEG_ANGLE + Math.PI;
		for (int i = LEGS / 2; i < LEGS; i++) {
			legAngles[i] = legAngle;
			legAngleOffsets[i] = 0;
			legAngle += LEG_ANGLE_SPACING;
		}
		double eyeX = 0;
		double eyeY = 0;
		eyesImg = new BufferedImage((int) (eyeSize + eyeSpacing) * 2, (int) (eyeSize * EYES / 2 + eyeSpacing * (EYES / 2 - 1)), BufferedImage.TYPE_INT_ARGB);
		Graphics2D eyeGraphics = (Graphics2D) eyesImg.getGraphics();
		eyeGraphics.setColor(eyeColor);
		for (int i = 0; i < EYES; i++) {
			eyeGraphics.fillOval((int) eyeX, (int) eyeY, eyeSize, eyeSize);
			if (i == EYES / 2 - 1) {
				eyeX += eyeSpacing + eyeSize;
				eyeY = 0;
			} else {
				eyeY += eyeSpacing + eyeSize;
			}
		}
		
		attackReach = ATT_RANGE;
		setSpeed(MOVE_SPEED);
		
		attackTimer = new Timer(ATTACK_COOLDOWN);
		
		dropTable.addEntry(new LootEntry(Items.Web, 0, 2, 100));
		dropTable.addEntry(new LootEntry(Items.Poison, 1, 1, 75));
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
		
		legDirection = 1;
		legProgress = 0;
	}
	public Spider(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		this(worldX, worldY, size, tileSize, world, random, BODY_COLOR, EYE_COLOR);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		updateBehavior(passedTime, c, updateCenter);
	}
	protected void updateMovingLegs(long passedTime) {
		if (isMoving || isRotating) {
			// Need to spin the legs around
			double legChange = passedTime / 1000.0 * LEG_SPD * legDirection;
			legProgress += Math.abs(legChange);
			for (int i = 0; i < LEGS; i++) {
				if (i % 2 == 0) {
					legAngleOffsets[i] += legChange;
				} else {
					legAngleOffsets[i] -= legChange;
				}
			}
			if (legProgress >= LEG_PROGRESS_MAX) {
				// Reverses the leg rotate direction, and resets progress to 0
				legProgress = 0;
				legDirection *= -1;
			}
		}
	}
	
	public void draw(Graphics2D g, Camera c) {
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		int size = getWidth();
		
		// Draw the body first
		g.setColor(getDamageColor(bodyColor));
		g.fillOval(drawX, drawY, size, size);
		// Draw the head next
		int centerX = drawX + size / 2;
		int centerY = drawY + size / 2;
		int headX = centerX + (int) (headSpacing * Math.cos(facingAngle)) - headSize / 2;
		int headY = centerY + (int) (headSpacing * Math.sin(facingAngle)) - headSize / 2;
		g.fillOval(headX, headY, headSize, headSize);
		// Now draw the legs
		g.setStroke(new BasicStroke(legWidth));
		for (int i = 0; i < legAngles.length; i++) {
			// Leg angles are offsets off of the center
			g.drawLine(centerX, centerY, centerX + (int) (legLength * Math.cos(legAngles[i] + legAngleOffsets[i] + facingAngle)),
					centerY + (int) (legLength * Math.sin(legAngles[i] + legAngleOffsets[i] + facingAngle)));
		}
		// Now draw the eyes
		int eyesX = headX + eyesImg.getWidth();
		int eyesY = headY + headSize / 2 - eyesImg.getHeight() / 2;
		GraphicsUtils.drawRotatedImg(eyesImg, facingAngle, eyesX, eyesY, eyesImg.getWidth(), eyesImg.getHeight(), 
				0, 0, new Point(eyesImg.getWidth() / 2, eyesImg.getHeight() / 2), g);
	}
	
	public void updateBehavior(long passedTime, Camera c, Point updateCenter) {
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
		
		updateMovingLegs(passedTime);
	}

}
