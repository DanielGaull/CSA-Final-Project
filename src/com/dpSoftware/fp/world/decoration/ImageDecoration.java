package com.dpSoftware.fp.world.decoration;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.ResourceLoader;

public abstract class ImageDecoration extends Decoration {

	private BufferedImage img;
	
	public ImageDecoration(int tileSize, int worldX, int worldY, String textureName, Items tool, long breakTime,
			boolean canCollide, boolean canSlow) {
		super(tileSize, worldX, worldY, tool, breakTime, canCollide, canSlow);
		img = loadDecorationImg(textureName);
	}

	public void draw(Graphics2D g, Camera c) {
		int functionalX = getDrawX(c);
		int functionalY = getDrawY(c);
		
		g.drawImage(img, functionalX, functionalY, getWidth(), getHeight(), null);
	}
	
	protected BufferedImage loadDecorationImg(String name) {
		return Images.loadImage("decorations\\" + name);
	}

}
