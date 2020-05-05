package com.dpSoftware.fp.world;

import java.awt.Graphics2D;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Input;

public abstract class WorldObject {

	protected double worldX;
	protected double worldY;
	protected int width;
	protected int height;
	protected final int tileSize;
	
	public WorldObject(double worldX, double worldY, int width, int height, int tileSize) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.width = width;
		this.height = height;
		this.tileSize = tileSize;
	}
	
	public abstract void draw(Graphics2D g, Camera c);
	
	public double getX() {
		return tileSize * worldX;
	}
	public double getY() {
		return tileSize * worldY;
	}
	public double getWorldX() {
		return worldX;
	}
	public double getWorldY() {
		return worldY;
	}
	public WorldNode getWorldNode() {
		return new WorldNode((int) Math.floor(getCenterX()), (int) Math.floor(getCenterY()));
	}
	public Point getPoint() {
		return new Point(worldX, worldY);
	}
	public Point getCenter() {
		return new Point(getCenterX(), getCenterY());
	}
	public int getDrawX(Camera c) {
		return (int) Math.round(getX() - c.getX() * tileSize);
	}
	public int getDrawY(Camera c) {
		return (int) Math.round(getY() - c.getY() * tileSize);
	}
	public int getDrawX(double x, Camera c) {
		return (int) Math.round(x * tileSize - c.getX() * tileSize);
	}
	public int getDrawY(double y, Camera c) {
		return (int) Math.round(y * tileSize - c.getY() * tileSize);
	}
	public Point getDrawPoint(Camera c) {
		return new Point(getDrawX(c), getDrawY(c));
	}
	public Point getDrawCenter(Camera c) {
		return new Point(getDrawX(c) + width, getDrawY(c) + height);
	}
	public Rectangle getRect() {
		return new Rectangle(worldX, worldY, width * 1.0 / tileSize, height * 1.0 / tileSize);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public double getWorldWidth() {
		return width * 1.0 / tileSize;
	}
	public double getWorldHeight() {
		return height * 1.0 / tileSize;
	}
	public double getCenterX() {
		return worldX + (width * 1.0 / tileSize) / 2;
	}
	public double getCenterY() {
		return worldY + (height * 1.0 / tileSize) / 2;
	}
	
	public void setWorldX(double value) {
		worldX = value;
	}
	public void setWorldY(double value) {
		worldY = value;
	}
	public void setWidth(int value) {
		width = value;
	}
	public void setHeight(int value) {
		height = value;
	}
	
	public boolean intersects(double ix, double iy) {
		double x = worldX;
		double y = worldY;
		double right = x + this.width * 1.0 / tileSize;
		double bottom = y + this.height * 1.0 / tileSize;
		return (ix >= x && iy >= y && ix <= right && iy <= bottom);
	}
	public boolean intersects(Rectangle rect) {
		return getRect().intersects(rect);
	}
	public boolean intersects(WorldObject wObj) {
		return intersects(wObj.getRect());
	}
	
}
