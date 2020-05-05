package com.dpSoftware.fp.entity;

import java.awt.Graphics2D;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.world.World;
import com.dpSoftware.fp.world.WorldObject;

public abstract class Entity extends WorldObject {

	private double speed;
	private boolean disposed;
	private double despawnDistance;
	private boolean isPersistent;
	protected double facingAngle;
	private static final String DIRECTORY = "entity";
	
	private MoveListener moveListener;
	
	public Entity(double worldX, double worldY, int width, int height,
			int tileSize) {
		super(worldX, worldY, width, height, tileSize);
		// Default speed to 5
		speed = 5;
		// Default despawn distance to 100
		despawnDistance = 100;
	}
	
	public abstract void update(long passedTime, Camera c, Point updateCenter);

	public void drawGui(Graphics2D g, Camera c) {}
	
	public void setMoveListener(MoveListener moveListener) {
		this.moveListener = moveListener;
	}
	
	protected MoveListener getMoveListener() {
		return moveListener;
	}
	
	public void changeX(double amount) {
		setWorldX(getWorldX() + amount);
		if (moveListener != null) {
			moveListener.onMoveX(this, getWorldX());
		}
	}
	public void changeY(double amount) {
		setWorldY(getWorldY() + amount);
		if (moveListener != null) {
			moveListener.onMoveY(this, getWorldY());
		}
	}
	public void increaseX(long passedTime) {
		changeX(speed * (passedTime / 1000.0));
	}
	public void increaseY(long passedTime) {
		changeY(speed * (passedTime / 1000.0));
	}
	public void decreaseX(long passedTime) {
		changeX(-speed * (passedTime / 1000.0));
	}
	public void decreaseY(long passedTime) {
		changeY(-speed * (passedTime / 1000.0));
	}
	
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double value) {
		this.speed = value;
	}

	public double distanceTo(Point p) {
		return distanceTo(p.getX(), p.getY());
	}
	public double distanceTo(double x, double y) {
		return Math.sqrt(Math.pow(x - getCenterX(), 2) + 
				Math.pow(y - getCenterY(), 2));
	}
	
	protected static String getDirectory() {
		return DIRECTORY;
	}
	
	public void dispose() {
		disposed = true;
	}
	public boolean isDisposed() {
		return disposed;
	}
	
	public double getDespawnDistance() {
		return despawnDistance;
	}
	public void setDespawnDistance(double value) {
		despawnDistance = value;
	}
	public boolean isPersistent() {
		return isPersistent;
	}
	public void setPersistent(boolean value) {
		isPersistent = value;
	}
	
	public double getFaceAngleTo(Point point, Camera c) {
		return MathUtils.angleBetween(new Point(getDrawX(c) + getWidth() / 2, getDrawY(c) + getHeight() / 2), point);
	}
	public void face(double angle) {
		facingAngle = angle;
	}
	public double getOrientation() {
		return facingAngle;
	}
}
