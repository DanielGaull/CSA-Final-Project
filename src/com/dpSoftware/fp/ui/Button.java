package com.dpSoftware.fp.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;

public class Button extends MenuElement {

	private Color normalColor;
	private Color hoverColor;
	private Color clickColor;
	private Color borderColor;
	private Color disabledBorderColor;
	private Color disabledFillColor;

	private IClickListener listener;

	private String text;
	private Color textColor;
	private Color disabledTextColor;
	private Font font;

	private static final int SPACING = 3;

	public Button(int x, int y, int width, int height, IClickListener listener, String text, Font font) {
		this(x, y, width, height, InterfaceTheme.BUTTON_FILL, InterfaceTheme.BUTTON_HOVER, InterfaceTheme.BUTTON_CLICK,
				InterfaceTheme.BUTTON_BORDER, listener, text, InterfaceTheme.BUTTON_TEXT, font,
				InterfaceTheme.BUTTON_D_BORDER, InterfaceTheme.BUTTON_D_FILL, InterfaceTheme.BUTTON_D_TEXT);
	}

	public Button(int x, int y, int width, int height, Color normalColor, Color hoverColor, Color clickColor,
			Color borderColor, IClickListener listener, String text, Color textColor, Font font,
			Color disabledBorderColor, Color disabledFillColor, Color disabledTextColor) {
		super(x, y, width, height, normalColor, borderColor);

		this.listener = listener;
		this.normalColor = normalColor;
		this.hoverColor = hoverColor;
		this.clickColor = clickColor;
		this.borderColor = borderColor;

		this.disabledBorderColor = disabledBorderColor;
		this.disabledFillColor = disabledFillColor;
		this.disabledTextColor = disabledTextColor;

		this.text = text;
		this.textColor = textColor;
		this.font = font;
	}

	public void setClickListener(IClickListener listener) {
		this.listener = listener;
	}

	public void update(long passedTime) {
		if (enabled) {
			super.update(passedTime);
			super.borderColor = this.borderColor;
			if (mousePressed) {
				color = clickColor;
			} else if (intersected && !mouseCaptured) {
				color = hoverColor;
			} else {
				color = normalColor;
			}
		} else {
			color = disabledFillColor;
			super.borderColor = disabledBorderColor;
		}
	}

	public void onClick() {
		listener.onClick(this);
	}

	public void draw(Graphics2D g) {
		super.draw(g);

		if (enabled) {
			g.setColor(textColor);
		} else {
			g.setColor(disabledTextColor);
		}
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(text, getX() + getWidth() / 2 - metrics.stringWidth(text) / 2,
				getY() + getHeight() / 2 + metrics.getHeight() / 2 - SPACING);
	}

	public void setX(int value) {
		super.setX(value);
	}

	public void setY(int value) {
		super.setY(value);
	}

}
