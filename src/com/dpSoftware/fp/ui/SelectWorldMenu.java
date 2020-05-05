package com.dpSoftware.fp.ui;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import com.dpSoftware.fp.util.FileUtils;
import com.dpSoftware.fp.world.WorldSave;

public class SelectWorldMenu extends Menu {

	private ArrayList<WorldSave> worlds;
	private int selectedWorld;
	
	public static final String WORLD_SAVE_LOCATION = "save\\worlds";
	
	private Button nextButton;
	private Button prevButton;
	private static final double BUTTON_SPACING_RATIO = 0.2;
	private final int buttonSpacing;
	
	private Button backButton;
	private Button selectButton;
	
	public static final int BACK = 0;
	public static final int SELECT = 1;
	
	private final int worldBoxX;
	private final int worldBoxY;
	private final int worldBoxWidth;
	private final int worldBoxHeight;
	private static final int WORLDBOX_BORDER = 5;
	private static final double WORLDBOX_HEIGHT_RATIO = 0.5;
	
	private static final double ELEMENT_WIDTH_RATIO = 0.65;
	private final int elementWidth;
	private static final double ELEMENT_HEIGHT_RATIO = 0.07;
	private final int elementHeight;
	private final int buttonSize;
	private static final int SPACING = 10;
	
	public SelectWorldMenu(int winWidth, int winHeight, IMenuListener menuListener) {
		super(winWidth, winHeight, menuListener);
		
		elementWidth = (int) (ELEMENT_WIDTH_RATIO * winWidth);
		elementHeight = (int) (ELEMENT_HEIGHT_RATIO * winHeight);
		buttonSize  = elementHeight;
		
		worldBoxWidth = elementWidth;
		worldBoxHeight = (int) (WORLDBOX_HEIGHT_RATIO * winHeight);
		worldBoxX = winWidth / 2 - worldBoxWidth / 2;
		worldBoxY = winHeight / 2 - worldBoxHeight / 2;
		
		worlds = new ArrayList<>();
		selectedWorld = 0;
		
		ArrayList<File> worldFiles = FileUtils.getAllSubFiles(new File(WORLD_SAVE_LOCATION));
		for (int i = 0; i < worldFiles.size(); i++) {
			worlds.add(WorldSave.fromFile(worldFiles.get(i)));
		}
		
		buttonSpacing = (int) (BUTTON_SPACING_RATIO * winWidth);
		
		prevButton = new Button(SPACING, winHeight - buttonSpacing - buttonSize, buttonSize, buttonSize,
				this, "\u2190", InterfaceTheme.BIG_FONT);
		nextButton = new Button(winWidth - SPACING - buttonSize, winHeight - buttonSpacing - buttonSize, 
				buttonSize, buttonSize, this, "\u2192", InterfaceTheme.BIG_FONT);
		
		backButton = new Button(winWidth / 2 - elementWidth / 2, winHeight - elementHeight - SPACING, elementWidth,
				elementHeight, this, "Back", InterfaceTheme.BIG_FONT);
		selectButton = new Button(winWidth / 2 - elementWidth / 2, backButton.getY() - elementHeight - SPACING, elementWidth,
				elementHeight, this, "Select", InterfaceTheme.BIG_FONT);
		
		addButton(prevButton);
		addButton(nextButton);
		addButton(backButton);
		addButton(selectButton);
	}

	public void update(long passedTime) {
		super.update(passedTime);
		
		if (selectedWorld + 1 >= worlds.size()) {
			nextButton.disable();
		} else {
			nextButton.enable();
		}
		if (selectedWorld - 1 < 0) {
			prevButton.disable();
		} else {
			prevButton.enable();
		}
		
		if (worlds.size() <= 0) {
			// No worlds, so we can't select any of them
			selectButton.disable();
		} else{
			selectButton.enable();
		}
	}
	public void draw(Graphics2D g) {
		super.draw(g);
		
		g.setColor(InterfaceTheme.INTERFACE_BG);
		g.fillRect(worldBoxX, worldBoxY, worldBoxWidth, worldBoxHeight);
		g.setStroke(new BasicStroke(WORLDBOX_BORDER));
		g.setColor(InterfaceTheme.INTERFACE_BORDER);
		g.drawRect(worldBoxX, worldBoxY, worldBoxWidth, worldBoxHeight);
		g.setFont(InterfaceTheme.BIG_FONT);
		g.setColor(InterfaceTheme.INTERFACE_TEXT);
		FontMetrics metrics = g.getFontMetrics();
		String worldNameString = "";
		if (worlds.size() > 0) {
			worldNameString = getSelectedWorld().getName();
		} else {
			worldNameString = "[NO WORLD SAVES]";
		}
		g.drawString(worldNameString, worldBoxX + worldBoxWidth / 2 - metrics.stringWidth(worldNameString) / 2, 
				worldBoxY + worldBoxHeight / 2 - metrics.getHeight() / 2);
	}
	
	public WorldSave getSelectedWorld() {
		return worlds.get(selectedWorld);
	}
	
	public void addWorld(WorldSave world) {
		worlds.add(world);
	}
	public void setWorld(String name, WorldSave value) {
		for (int i = 0; i < worlds.size(); i++) {
			if (worlds.get(i).getName().equals(name)) {
				worlds.set(i, value);
				return;
			}
		}
	}
	
	@Override
	public void onClick(MenuElement element) {
		if (element == nextButton) {
			if (selectedWorld + 1 < worlds.size()) {
				selectedWorld++;
			}
		} else if (element == prevButton) {
			if (selectedWorld - 1 >= 0) {
				selectedWorld--;
			}			
		} else if (element == backButton) {
			menuListener.onButtonClick(this, BACK);
		} else if (element == selectButton) {
			menuListener.onButtonClick(this, SELECT);
		}
	}
}
