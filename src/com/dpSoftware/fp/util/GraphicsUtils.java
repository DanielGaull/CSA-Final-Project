package com.dpSoftware.fp.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.dpSoftware.fp.ui.Point;

public class GraphicsUtils {

	public static void drawRotatedImg(BufferedImage img, double radians, int x, int y, int width, int height, 
			int offsetX, int offsetY, boolean center, Graphics2D g) {
		if (center) {
			drawRotatedImg(img, radians, x, y, width, height, offsetX, offsetY, new Point(width / 2, height / 2), g);
		} else {
			drawRotatedImg(img, radians, x, y, width, height, offsetX, offsetY, new Point(0, 0), g);
		}
	}
	public static void drawRotatedImg(BufferedImage img, double radians, int x, int y, int width, int height, 
			int offsetX, int offsetY, Point centerPoint, Graphics2D g) {
		AffineTransform prevTransform = g.getTransform();
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		at.rotate(radians, centerPoint.getX(), centerPoint.getY());
		g.setTransform(at);
		g.drawImage(img, offsetX, offsetY, width, height, null);
		g.setTransform(prevTransform);
	}
	
}
