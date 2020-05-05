package com.dpSoftware.fp.ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.util.Vector;
import com.dpSoftware.fp.world.World;

public class MainMenu extends Menu {

	private final int winWidth;
	private final int winHeight;
	
	// Buttons
	private Button loadWorldButton;
	private Button newWorldButton;
	private Button exitButton;
	
	// Button "codes"
	public static final int LOAD_WORLD = 0;
	public static final int NEW_WORLD = 1;
	public static final int EXIT = 2;
	
	private static final double BUTTON_WIDTH_RATIO = 0.65;
	private final int buttonWidth;
	private static final double BUTTON_HEIGHT_RATIO = 0.07;
	private final int buttonHeight;
	private static final double BUTTON_START_Y_RATIO = 0.35;
	private final int buttonStartY;
	private static final int BUTTON_SPACING = 10;
	
	public MainMenu(int winWidth, int winHeight, IMenuListener menuListener) {
		super(winWidth, winHeight, menuListener);
		
		this.winWidth = winWidth;
		this.winHeight = winHeight;
		
		buttonWidth = (int) (BUTTON_WIDTH_RATIO * winWidth);
		buttonHeight = (int) (BUTTON_HEIGHT_RATIO * winHeight);
		buttonStartY = (int) (BUTTON_START_Y_RATIO * winHeight);
		
		newWorldButton = new Button(winWidth / 2 - buttonWidth / 2, buttonStartY, buttonWidth, buttonHeight,
				this, "New World", InterfaceTheme.BIG_FONT);
		loadWorldButton = new Button(winWidth / 2 - buttonWidth / 2, newWorldButton.getY() + buttonHeight + BUTTON_SPACING, buttonWidth, buttonHeight,
				this, "Load World", InterfaceTheme.BIG_FONT);
		exitButton = new Button(winWidth / 2 - buttonWidth / 2, loadWorldButton.getY() + buttonHeight + BUTTON_SPACING, buttonWidth, buttonHeight,
				this, "Exit", InterfaceTheme.BIG_FONT);
		addButton(loadWorldButton);
		addButton(newWorldButton);
		addButton(exitButton);
	}

	public void onClick(MenuElement clicked) {
		if (clicked == loadWorldButton) {
			menuListener.onButtonClick(this, LOAD_WORLD);
		} else if (clicked == newWorldButton) {
			menuListener.onButtonClick(this, NEW_WORLD);
		} else if (clicked == exitButton) {
			menuListener.onButtonClick(this, EXIT);
		}
	}
	
}
