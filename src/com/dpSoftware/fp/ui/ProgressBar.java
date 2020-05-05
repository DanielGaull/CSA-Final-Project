package com.dpSoftware.fp.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.dpSoftware.fp.util.MathUtils;

public class ProgressBar {

	private double value;
	private double maxValue;
	private int x;
	private int y;
	private int width;
	private int height;
	private final Color borderColor;
	private final Color fillColor;
	private final Color bgFillColor;
	private Font font;
	private final Color textColor;
	private String valueName;
	private final boolean parseAsIntInDisplay;

	private static final int BORDER_SIZE = 3;

	public ProgressBar(int x, int y, int width, int height, Color border, Color fill, Color bgFill, double value, double maxValue) {
		this(x, y, width, height, border, fill, bgFill, value, maxValue, null, null, null, false);
	}
	public ProgressBar(int x, int y, int width, int height, Color border, Color fill, Color bgFill,  double value, double maxValue, Font font, 
			Color textColor, String valueName, boolean parseAsIntInDisplay) {
		this.value = value;
		this.maxValue = maxValue;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.borderColor = border;
		this.fillColor = fill;
		this.bgFillColor = bgFill;
		this.font = font;
		this.textColor = textColor;
		this.valueName = valueName;
		this.parseAsIntInDisplay = parseAsIntInDisplay;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(bgFillColor);
		g.fillRect(x, y, width, height);
		g.setColor(fillColor);
		g.fillRect(x, y, getFillBarWidth(), height);
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(BORDER_SIZE));
		g.drawRect(x, y, width, height);
		// Some Progress Bars don't draw the progress as a string
		if (font != null) {
			String text = getProgressString();
			g.setColor(textColor);
			g.setFont(font);
			g.drawString(text, x + BORDER_SIZE, y + height - BORDER_SIZE * 2);
		}
	}
	
	private String getProgressString() {
		String text = "";
		if (valueName != null && valueName.length() > 0) {
			text = valueName + ": ";
		}
		if (parseAsIntInDisplay) {
			// Parse value and maxValue as integers so that they don't have the trailing ".0"
			text += ((int) value) + "/" + ((int) maxValue);
		} else {
			text += value + "/" + maxValue;
		}
		return text;
	}
	private int getFillBarWidth() {
		return (int) (value / maxValue * width);
	}

	public double getValue() {
		return value;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String value) {
		valueName = value;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setX(int value) {
		x = value;
	}
	public void setY(int value) {
		y = value;
	}
}
