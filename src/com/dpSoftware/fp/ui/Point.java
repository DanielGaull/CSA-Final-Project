package com.dpSoftware.fp.ui;

import org.json.JSONObject;

public class Point {

	private double x;
	private double y;
	public Point() {
		this(0);
	}
	public Point (double value) {
		this(value, value);
	}
	public Point(double x, double y)  {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setX(double value) {
		x = value;
	}
	public void setY(double value) {
		y = value;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	public static Point fromJsonObject(JSONObject obj) {
		double x = obj.getDouble("x");
		double y = obj.getDouble("y");
		return new Point(x, y);
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Point)) return false;
		
		Point p = (Point) o;
		return p.x == x && p.y == y;
	}
	
	public double distanceTo(Point p2) {
		return Math.sqrt(Math.pow(x - p2.x, 2) + Math.pow(y - p2.y, 2));
	}
	
}
