package com.dpSoftware.fp.util;

import java.awt.Color;

import com.dpSoftware.fp.ui.Point;

public class MathUtils {

	public static double roundTo(double value, int digits) {
		return Math.round(value * Math.pow(10, digits)) / Math.pow(10, digits);
	}
	
	// Linear interpolation (LERP) functions
	public static double lerp(double min, double max, double factor) {
		if (factor < 0) return min;
		else if (factor > 1) return max;
		return min + factor * (max - min);
	}
	public static Color colorLerp(Color c1, Color c2, double factor) {
		return new Color((int) lerp(c1.getRed(), c2.getRed(), factor),
				(int) lerp(c1.getGreen(), c2.getGreen(), factor),
				(int) lerp(c1.getBlue(), c2.getBlue(), factor),
				(int) lerp(c1.getAlpha(), c2.getAlpha(), factor));
	}
	public static Point pointLerp(Point p1, Point p2, double factor) { 
		return new Point(lerp(p1.getX(), p2.getX(), factor), lerp(p1.getY(), p2.getY(), factor));
	}
	
	public static boolean isWholeNumber(double number) {
		return number == Math.floor(number) && !Double.isInfinite(number);
	}
	
	public static double angleBetween(Point p1, Point p2) {
		// Multiply by -1 at the end because for some reason Java flips everything. This calculation 
		// matches the angle up to the unit circle (roughly)
		double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()) * -1;
		// Due to tangent's range, once the angle gets between pi and 2pi, it becomes negative
		// So, if the angle is negative, need to multiply by -1 and add pi
		// to match it up with the unit circle
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
		// All of this seems to finally return an angle that Java can work with properly
		return -angle + Math.PI * 2;
	}
}
