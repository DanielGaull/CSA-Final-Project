package com.dpSoftware.fp.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.dpSoftware.fp.entity.Creature;

public class BossBar {

	private ProgressBar healthBar;
	private Creature trackingEntity;
	
	public BossBar(int x, int y, int width, int height, Color color, Creature trackingEntity, Font font) {
		healthBar = new ProgressBar(x, y, width, height, getBorderColor(color), color, getBgColor(color), 
				trackingEntity.getHealth(), trackingEntity.getMaxHealth(), font, getTextColor(color), 
				trackingEntity.getName(), true);
		this.trackingEntity = trackingEntity;
	}
	
	public void update() {
		healthBar.setMaxValue(trackingEntity.getMaxHealth());
		healthBar.setValue(trackingEntity.getHealth());
	}
	public void draw(Graphics2D g) {
		healthBar.draw(g);
	}
	
	public Creature getTrackingEntity() {
		return trackingEntity;
	}
	
	private Color getBorderColor(Color color) {
		return new Color(Math.min((int) (color.getRed() * 0.55), 255), 
				Math.min((int) (color.getGreen() * 0.55), 255),
				Math.min((int) (color.getBlue() * 0.55), 255));
	}
	private Color getBgColor(Color color) {
		return new Color(Math.min((int) (color.getRed() * 0.75), 255), 
				Math.min((int) (color.getGreen() * 0.75), 255),
				Math.min((int) (color.getBlue() * 0.75), 255));
	}
	private Color getTextColor(Color color) {
		int average = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
		if (average > 127) {
			return new Color(color.getRed() / 3, 
					color.getGreen() / 3,
					color.getBlue() / 3);
		} else {
			return new Color(Math.min((int) (color.getRed() * 3), 255), 
					Math.min((int) (color.getGreen() * 3), 255),
					Math.min((int) (color.getBlue() * 3), 255));
		}
	}
}
