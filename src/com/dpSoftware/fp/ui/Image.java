package com.dpSoftware.fp.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Image {

	private int x;
	private int y;
	private int width;
	private int height;
	private BufferedImage bufferedImg;
	
	public Image(int x, int y, int width, int height, BufferedImage bufferedImg) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bufferedImg = bufferedImg;
	}
	
	public void draw(Graphics2D g) {
		drawAt(g, x, y);
	}
	public void drawAt(Graphics2D g, int x, int y) {
		g.drawImage(bufferedImg, x, y, width, height, null);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public BufferedImage getBufferedImg() {
		return bufferedImg;
	}
	public void setBufferedImg(BufferedImage bi) {
		bufferedImg = bi;
	}
	
}
