package com.dpSoftware.fp.entity.projectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.Images;

public abstract class SpinningProjectile extends Projectile {

	private int size;
	private BufferedImage image;
	private double rotation;
	private final double spinSpd;
	
	public SpinningProjectile(double worldX, double worldY, int size, int tileSize, double speed,
			double damage, Projectiles projectileType, long lifeTime, double spinSpd, String fileName) {
		super(worldX, worldY, size, size, tileSize, speed, damage, projectileType, lifeTime);
		this.spinSpd = spinSpd;
		this.size = size;
		rotation = 0;
		image = Images.loadImage("entity\\" + fileName + "\\" + fileName);
	}
	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		rotation += spinSpd * (passedTime / 1000.0);
		// Keep the angle within the range of 0 to 2pi
		rotation %= Math.PI * 2;
	}
	public void draw(Graphics2D g, Camera c) {
		int drawX = getDrawX(c);
		int drawY = getDrawY(c);		
		GraphicsUtils.drawRotatedImg(image, rotation, drawX, drawY, size, size, 0, 0, true, g);
	}
}
