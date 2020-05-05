package com.dpSoftware.fp.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class PauseMenu extends Menu {

	private final int winWidth;
	private final int winHeight;
	
	private static final Color OVERLAY_COLOR = new Color(127, 127, 127, 127);
	
	private Button resumeButton;
	private Button mainMenuButton;
	
	public static final int RESUME = 0;
	public static final int MAIN_MENU = 1;
	
	private static final String HEADER = "Paused";
	private static final int HEADER_SPACING = 100;
	
	private static final double ELEMENT_WIDTH_RATIO = 0.65;
	private final int elementWidth;
	private static final double ELEMENT_HEIGHT_RATIO = 0.07;
	private final int elementHeight;
	private static final int SPACING = 10;
	
	public PauseMenu(int winWidth, int winHeight, IMenuListener menuListener) {
		super(winWidth, winHeight, menuListener);
		
		this.winWidth = winWidth;
		this.winHeight = winHeight;
		
		elementWidth = (int) (ELEMENT_WIDTH_RATIO * winWidth);
		elementHeight = (int) (ELEMENT_HEIGHT_RATIO * winHeight);
		
		resumeButton = new Button(winWidth / 2 - elementWidth / 2, winHeight / 2 - SPACING / 2 - elementHeight,
				elementWidth, elementHeight, this, "Resume", InterfaceTheme.BIG_FONT);
		mainMenuButton = new Button(resumeButton.getX(), resumeButton.getY() + resumeButton.getHeight() + SPACING,
				elementWidth, elementHeight, this, "Main Menu", InterfaceTheme.BIG_FONT);
		addButton(resumeButton);
		addButton(mainMenuButton);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(OVERLAY_COLOR);
		g.fillRect(0, 0, winWidth, winHeight);
		
		g.setColor(Color.white);
		g.setFont(InterfaceTheme.TITLE_FONT);
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(HEADER, winWidth / 2 - metrics.stringWidth(HEADER) / 2, resumeButton.getY() - metrics.getHeight() - HEADER_SPACING);
		
		super.draw(g);
	}
	
	public void onClick(MenuElement clicked) {
		if (clicked == resumeButton) {
			menuListener.onButtonClick(this, RESUME);
		} else if (clicked == mainMenuButton) {
			menuListener.onButtonClick(this, MAIN_MENU);
		}
	}

}
