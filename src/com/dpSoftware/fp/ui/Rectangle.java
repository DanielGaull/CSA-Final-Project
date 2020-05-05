package com.dpSoftware.fp.ui;

public class Rectangle {
	private double x;
	private double y;
	private double width;
	private double height;
	
	public Rectangle() {
		x = y = width = height = 0;
	}
	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean includes(Point point) {
		return (point.getX() >= x && point.getY() >= y && point.getX() <= x + width && point.getY() <= y + height);
	}
	public boolean includesNotEqual(Point point) {
		return (point.getX() > x && point.getY() > y && point.getX() < x + width && point.getY() < y + height);
	}
	public boolean includes(double x, double y) {
		return includes(new Point(x, y));
	}
	public boolean intersects(Rectangle rect) {
		// In order to intersect, at least one of the corners of either rectangle must intersect the other rectangle
		// First see if any of rect's corners intersect any of this's corners
		if (includesNotEqual(rect.getTopLeft()) || includesNotEqual(rect.getTopRight()) || includesNotEqual(rect.getBottomLeft()) || 
				includesNotEqual(rect.getBottomRight())) {
			return true;
		}
		// Now test if any of "this"'s corners intersect any of "rect"'s corners
		if (rect.includesNotEqual(getTopLeft()) || rect.includesNotEqual(getTopRight()) || rect.includesNotEqual(getBottomLeft()) || 
				rect.includesNotEqual(getBottomRight())) {
			return true;
		}		
		
		return false;
	}
	public Sides getIntSideLeftRight(Rectangle rect) {
		if (intersects(rect)) {
			if (rect.getRight() > getX() && rect.getX() < getX()) {
				return Sides.Left;
			}
			if (rect.getX() < getRight() && rect.getRight() > getRight()) {
				return Sides.Right;
			}
			
		}
		return null;
	}
	public Sides getIntSideTopBottom(Rectangle rect) {
		if (intersects(rect)) {
			if (rect.getBottom() > getY() && rect.getY() < getY()) {
				return Sides.Top;
			}
			if (rect.getY() < getBottom() && rect.getBottom() > getBottom()) {
				return Sides.Bottom;
			}
			
		}
		return null;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getLeft() {
		return x;
	}
	public double getRight() {
		return x + width;
	}
	public double getTop() {
		return y;
	}
	public double getBottom() {
		return y + height;
	}
	public Point getTopLeft() {
		return new Point(getLeft(), getTop());
	}
	public Point getBottomRight() {
		return new Point(getRight(), getBottom());
	}
	public Point getTopRight() {
		return new Point(getRight(), getTop());
	}
	public Point getBottomLeft() {
		return new Point(getLeft(), getBottom());
	}
	public Point center() {
		return new Point(x + width / 2, y + height / 2);
	}
	public void setX(double value) {
		x = value;
	}
	public void setY(double value) {
		y = value;
	}
	public void setWidth(double value) {
		width = value;
	}
	public void setHeight(double value) {
		height = value;
	}
	public void changeX(double amount) {
		x += amount;
	}
	public void changeY(double amount) {
		y += amount;
	}
	
	public String toString() {
		return "Rectangle: (x: " + x + ", y: " + y + ", w: " + width + ", h: " + height + ")";
	}
	
	public enum Sides {
		Top,
		Bottom,
		Left,
		Right;
	}
}
