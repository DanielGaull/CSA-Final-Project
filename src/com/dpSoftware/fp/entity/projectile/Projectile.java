package com.dpSoftware.fp.entity.projectile;

import java.awt.Graphics2D;

import com.dpSoftware.fp.entity.Creature;
import com.dpSoftware.fp.entity.Entity;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.util.Vector;

public abstract class Projectile extends Entity {

	private Point target;
	private Point start;
	private Vector direction;
	private Point location;
	private Timer lifeTimer;
	private boolean isFlying;
	private boolean isLanded;
	private double speed;
	private Projectiles projectileType;
	private Creature launcher;
	
	private static final double DESPAWN_DISTANCE = 50; // Despawns when 50 tiles way from player
	
	private double damage;
	
	public Projectile(double worldX, double worldY, int width, int height, int tileSize, double speed, double damage,
			Projectiles projectileType, long lifeTime) {
		super(worldX - width * 1.0 / tileSize / 2, worldY - height * 1.0 / tileSize / 2, width, height, tileSize);
		this.speed = speed;
		this.damage = damage;
		this.projectileType = projectileType;
		lifeTimer = new Timer(lifeTime);
		setDespawnDistance(DESPAWN_DISTANCE);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		double secondsPassed = passedTime / 1000.0;
		if (isFlying) {
			location.setX(location.getX() + speed * secondsPassed * direction.getX());
			location.setY(location.getY() + speed * secondsPassed * direction.getY());
			setWorldX(location.getX());
			setWorldY(location.getY());
			lifeTimer.update(passedTime);
			if (lifeTimer.query()) {
				impact();
			}
		}
	}
	public void launch(Point target, Creature launcher) {
		this.launcher = launcher;
		// Make sure the projectile is centered on the target
		target.setX(target.getX() - width * 1.0 / tileSize / 2);
		target.setY(target.getY() - height * 1.0 / tileSize / 2);
		this.target = target;
		start = new Point(getWorldX(), getWorldY());
		isFlying = true;
		isLanded = false;
		location = new Point(getWorldX(), getWorldY());
		double angle = MathUtils.angleBetween(start, target);
		direction = new Vector(angle);
		onLaunch();
	}
	// Can be overridden by children of this class to add special behavior
	// Most projectiles disappear once they've landed, however
	protected void onLand() {
		dispose();
	}
	protected void onLaunch() {
		
	}
	
	public boolean isFlying() {
		return isFlying;
	}
	public boolean isLanded() {
		return isLanded;
	}
	
	public double getDamage() {
		return damage;
	}
	public Creature getLauncher() {
		return launcher;
	}
	
	public void impact() {
		isFlying = false;
		isLanded = true;
		onLand();
	}
	
	public Projectiles getProjectileType() {
		return projectileType;
	}
}
