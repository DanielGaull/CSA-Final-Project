package com.dpSoftware.fp.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.dpSoftware.fp.ui.Point;

public class BufferedImageUtils {
	
	public static BufferedImage getUpsideDownImage(BufferedImage bi) {
	    BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(),
	        bi.getType());
	    AffineTransform tran = AffineTransform.getTranslateInstance(0,
	        bi.getHeight());
	    AffineTransform flip = AffineTransform.getScaleInstance(1d, -1d);
	    tran.concatenate(flip);

	    Graphics2D g = flipped.createGraphics();
	    g.setTransform(tran);
	    g.drawImage(bi, 0, 0, null);
	    g.dispose();

	    return flipped;
	}
	public static BufferedImage getSideFlippedImage(BufferedImage bi) {
	    BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
	    AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
	    AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
	    tran.concatenate(flip);

	    Graphics2D g = flipped.createGraphics();
	    g.setTransform(tran);
	    g.drawImage(bi, 0, 0, null);
	    g.dispose();

	    return flipped;
	}
	
	// From https://stackoverflow.com/questions/4248104/applying-a-tint-to-an-image-in-java/4248459#4248459
	public static BufferedImage getColoredImage(BufferedImage baseImg, Color color) {
	    BufferedImage newImg = new BufferedImage(baseImg.getWidth(), baseImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    for (int i = 0; i < baseImg.getWidth(); i++) {
	    	for (int j = 0; j < baseImg.getHeight(); j++) {
	    		Color pixel = new Color(baseImg.getRGB(i, j), true);
	    		double lerpFactor = color.getAlpha() / 255.0;
	    		int r = (int) MathUtils.lerp(pixel.getRed(), color.getRed(), lerpFactor);
	    		int g = (int) MathUtils.lerp(pixel.getGreen(), color.getGreen(), lerpFactor);
	    		int b = (int) MathUtils.lerp(pixel.getBlue(), color.getBlue(), lerpFactor);
	    		int a = pixel.getAlpha();
	    		int rgba = (a << 24) | (r << 16) | (g << 8) | b;
	    		newImg.setRGB(i, j, rgba);
	    	}
	    }
	    return newImg;
	}
}
