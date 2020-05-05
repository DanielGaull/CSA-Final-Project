package com.dpSoftware.fp.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Monkey extends PunchingHumanoidEnemy {
	
	private static final Color FUR_COLOR = new Color(79, 54, 0);
	private static final Color HAND_COLOR = new Color(173, 156, 118);
	private BufferedImage tailImg;
	private static final int TAIL_WIDTH = 3;
	private static final double TAIL_POINT_SPACING_RATIO = 0.2;
	private final int tailPointSpacing;
	private static final int TAIL_POINTS = 5;
	
	private static final int ATTACK_COOLDOWN = 2500;
	private static final double SIGHT_RANGE = 15;
	private static final double ATT_RANGE = 1.5;
	private static final double ATT_DMG = 10;
	private static final int MAX_HEALTH = 30;
	private static final int DEFENSE = 20;
	private static final double MOVE_SPEED = 0.05;
	
	private static final int COINS_MIN = 1;
	private static final int COINS_MAX = 3;
	private static final int XP_MIN = 10;
	private static final int XP_MAX = 15;
	
	public Monkey(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, FUR_COLOR, HAND_COLOR, HAND_COLOR, random, "Monkey", ATTACK_COOLDOWN,
				SIGHT_RANGE, ATT_DMG, ATT_RANGE);
		
		dropTable.addEntry(new LootEntry(Items.Banana, 1, 4, 100));
		
		tailPointSpacing = (int) (TAIL_POINT_SPACING_RATIO * size);
		
		setSpeed(MOVE_SPEED);
		setMaxHealth(MAX_HEALTH);
		setBaseDefense(DEFENSE);
		
		forceHandOffset = true;
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
		
		generateTailImg(random);
	}
	
	public void draw(Graphics2D g, Camera c) {
		super.draw(g, c);
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);
		GraphicsUtils.drawRotatedImg(tailImg, facingAngle, drawX - tailImg.getWidth(), 
				drawY + getHeight() / 2 - tailImg.getHeight() / 2, tailImg.getWidth(), tailImg.getHeight(), 
				0, 0, new Point(tailImg.getWidth() + getWidth() / 2, tailImg.getHeight() / 2), g);
	}

	private void generateTailImg(Random random) {
		// The maximum width the tail can possibly be is the spacing in between the points times the number of points
		// We multiply the height times 2 since we start generating points in the middle
		tailImg = new BufferedImage(tailPointSpacing * TAIL_POINTS, tailPointSpacing * TAIL_POINTS * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tailGraphics = (Graphics2D) tailImg.getGraphics();
		tailGraphics.setStroke(new BasicStroke(TAIL_WIDTH));
		tailGraphics.setColor(FUR_COLOR);
		// Generate the tail. We do this by randomly generating some points and connecting them. The points are generated
		// by taking in a random angle and computing the sin and cosine of that angle
		double tailGenX = tailImg.getWidth(); // Start all the way on the right
		double tailGenY = tailImg.getHeight() / 2; // Start in the center
		for (int i = 0; i < TAIL_POINTS; i++) {
			// The angle is between 2pi/3 and 4pi/3
			double theta = RandomUtils.randomDoubleBetween(random, 2 * Math.PI / 3, 4 * Math.PI / 3);
			double sin = tailPointSpacing * Math.sin(theta);
			double cos = tailPointSpacing * Math.cos(theta);
			tailGraphics.drawLine((int) tailGenX, (int) tailGenY, (int) (tailGenX + cos), (int) (tailGenY + sin));
			tailGenX += cos;
			tailGenY += sin;
		}
		tailGraphics.dispose();
	}
}

