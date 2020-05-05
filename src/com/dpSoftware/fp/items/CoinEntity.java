package com.dpSoftware.fp.items;

import java.awt.Graphics2D;
import com.dpSoftware.fp.entity.Entity;
import com.dpSoftware.fp.ui.Image;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Images;

public class CoinEntity extends Entity {

	private Image img;
	private static final String TEXTURE_NAME = "misc\\coin";
	private int amount;
	
	public CoinEntity(double x, double y, int size, int tileSize, int amount) {
		super(x, y, size, size, tileSize);
		this.amount = amount;
		img = new Image(-size, -size, size, size, Images.loadImage(TEXTURE_NAME));
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		img.setX(getDrawX(c));
		img.setY(getDrawY(c));
	}

	public void draw(Graphics2D g, Camera c) {
		img.draw(g);
	}
	
	public int getAmount() {
		return amount;
	}
	
}
