package com.dpSoftware.fp.util;

public class Vector {

	private double x;
	private double y;
	
	public Vector() {
		this(0, 0);
	}
	// Constructs a unit vector of the specified angle
	public Vector(double angle) {
		x = Math.cos(angle);
		y = Math.sin(angle);
	}
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public boolean isUnitVector() {
		return (getMagnitude() == 1);
	}
	public double getMagnitude() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}
	
	public void setX(double value) {
		x = value;
	}
	public void setY(double value) {
		y = value;
	}

	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	public static Vector subtract(Vector v1, Vector v2) {
		// Multiply the second vector by -1 and then add them together
		return add(v1, multiply(v2, -1));
	}
	public static Vector multiply(Vector v, double scalar) {
		return new Vector(v.x * scalar, v.y * scalar);
	}
}
