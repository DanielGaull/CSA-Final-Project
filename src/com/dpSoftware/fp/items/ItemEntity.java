package com.dpSoftware.fp.items;

import java.awt.Graphics2D;

import com.dpSoftware.fp.entity.Entity;
import com.dpSoftware.fp.ui.Image;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.ResourceLoader;
import com.dpSoftware.fp.world.World;

public class ItemEntity extends Entity {

	private ItemStack itemStack;
	private Image img;
	
	public ItemEntity(double worldX, double worldY, int size, int tileSize,
			ItemStack itemStack) {
		super(worldX, worldY, size, size, tileSize);
		
		this.itemStack = itemStack;
		// Don't display until there's time to update
		img = new Image(-size, -size, size, size, 
				Images.loadImage(itemStack.getItem().getTextureDirectory()));
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		img.setX(getDrawX(c));
		img.setY(getDrawY(c));
	}
	public void draw(Graphics2D g, Camera c) {
		img.draw(g);
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

}
