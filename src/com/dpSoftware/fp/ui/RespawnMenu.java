package com.dpSoftware.fp.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class RespawnMenu extends Menu {

	private final int winWidth;
	private final int winHeight;
	
	private static final Color OVERLAY_COLOR = new Color(127, 30, 30, 127);
	
	private static final String HEADER = "You Have Died";
	private static final int HEADER_SPACING = 100;
	private String deathMessage;
	private static final Color TEXT_COLOR = new Color(237, 135, 123);
	
	private static final double ELEMENT_WIDTH_RATIO = 0.65;
	private final int elementWidth;
	private static final double ELEMENT_HEIGHT_RATIO = 0.07;
	private final int elementHeight;
	private static final int SPACING = 10;
	
	public static final int RESPAWN = 0;
	
	private Button respawnButton;
	
	public RespawnMenu(int winWidth, int winHeight, IMenuListener menuListener) {
		super(winWidth, winHeight, menuListener);
		
		this.winWidth = winWidth;
		this.winHeight = winHeight;
		
		elementWidth = (int) (ELEMENT_WIDTH_RATIO * winWidth);
		elementHeight = (int) (ELEMENT_HEIGHT_RATIO * winHeight);
		
		deathMessage = "";
		
		respawnButton = new Button(winWidth / 2 - elementWidth / 2, winHeight / 2 - elementHeight / 2, 
				elementWidth, elementHeight, this, "Respawn", InterfaceTheme.BIG_FONT);
		addButton(respawnButton);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(OVERLAY_COLOR);
		g.fillRect(0, 0, winWidth, winHeight);
		
		g.setColor(TEXT_COLOR);
		g.setFont(InterfaceTheme.TITLE_FONT);
		FontMetrics metrics = g.getFontMetrics();
		int headerHeight = metrics.getHeight();
		int headerY = respawnButton.getY() - headerHeight - HEADER_SPACING;
		g.drawString(HEADER, winWidth / 2 - metrics.stringWidth(HEADER) / 2, headerY);
		g.setFont(InterfaceTheme.MED_FONT);
		metrics = g.getFontMetrics();
		g.drawString(deathMessage, winWidth / 2 - metrics.stringWidth(deathMessage) / 2, headerY + headerHeight + SPACING);
		
		super.draw(g);
	}
	
	public void onClick(MenuElement clicked) {
		if (clicked == respawnButton) {
			menuListener.onButtonClick(this, RESPAWN);
		}
	}
	
	public void setDeathMessage(String value) {
		this.deathMessage = value;
	}

}
