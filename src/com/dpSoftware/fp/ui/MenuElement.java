package com.dpSoftware.fp.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.dpSoftware.fp.util.Input;

public class MenuElement {
	
	protected Rectangle rect;
	protected Color color;
	protected Color borderColor;
	protected boolean hasBorder;
	protected int tag;
	protected static final float BORDER_SIZE = 5f;
	protected static final int SPACING = 5;

	protected boolean mousePressed;
	protected boolean mouseReleased;
	protected boolean intersected;
	
	protected boolean enabled;
	
	protected static boolean mouseCaptured;
	
	public MenuElement(int x, int y, int width, int height, Color color) {
		this(x, y, width, height, color, Color.WHITE);
		hasBorder = false;
	}
	public MenuElement(int x, int y, int width, int height, Color color, Color borderColor) {
		rect = new Rectangle(x, y, width, height);
		this.color = color;
		this.borderColor = borderColor;
		hasBorder = true;
		enabled = true;
	}

	public void update(long passedTime) {
		intersected = rect.includes(Input.getMouseLoc());
		if (intersected && Input.isLeftDown()) {
			if (!mouseCaptured) {
				mousePressed = true;
				mouseReleased = false;
				mouseCaptured = true;
			}
		} else if (intersected) {
			mouseReleased = true;
		} else if (!Input.isLeftDown()) {
			mousePressed = false;
			mouseCaptured = false;
		}
		if (mousePressed && mouseReleased) {
			mousePressed = false;
			mouseCaptured = false;
			onClick();
		}
		if (Input.isLeftDown() && !intersected) {
			onClickOutside();
		}
	}
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
		if (hasBorder) {
			g.setColor(borderColor);
			g.setStroke(new BasicStroke(BORDER_SIZE));
			g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
		}
	}

	protected void onClick() {
		
	}
	protected void onHover() {
		
	}
	protected void onClickOutside() {
		
	}
	
	public int getX() {
		return (int) rect.getX();
	}
	public int getY() {
		return (int) rect.getY();
	}
	public int getWidth() {
		return (int) rect.getWidth();
	}
	public int getHeight() {
		return (int) rect.getHeight();
	}
	public Color getColor() {
		return color;
	}
	
	public void setX(int value) {
		rect.setX(value);
	}
	public void setY(int value) {
		rect.setY(value);
	}
	public void setWidth(int value) {
		rect.setWidth(value);
	}
	public void setHeight(int value) {
		rect.setHeight(value);
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void changeX(int amount) {
		rect.changeX(amount);
	}
	public void changeY(int amount) {
		rect.changeY(amount);
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public int getTag() {
		return tag;
	}
	public void setTag(int value) {
		tag = value;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void enable() {
		enabled = true;
	}
	public void disable() {
		enabled = false;
	}
}
