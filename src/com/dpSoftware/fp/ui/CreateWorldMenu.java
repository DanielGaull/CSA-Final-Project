package com.dpSoftware.fp.ui;

import java.awt.Font;
import java.util.Random;

public class CreateWorldMenu extends Menu implements ITextChangeListener {

	private Button createButton;
	private Button backButton;
	private Textbox nameBox;
	private Textbox seedBox;
	
	private Random random;
	
	public static final int CREATE_WORLD = 0;
	public static final int BACK = 1;
	
	private static final int MAX_NAME_LENGTH = 36;
	
	private static final double ELEMENT_WIDTH_RATIO = 0.65;
	private final int elementWidth;
	private static final double ELEMENT_HEIGHT_RATIO = 0.07;
	private final int elementHeight;
	private static final int TEXTBOX_SPACING = 40;
	private static final int BUTTON_SPACING = 10;
	
	public CreateWorldMenu(int winWidth, int winHeight, IMenuListener menuListener) {
		super(winWidth, winHeight, menuListener);
		
		elementWidth = (int) (ELEMENT_WIDTH_RATIO * winWidth);
		elementHeight = (int) (ELEMENT_HEIGHT_RATIO * winHeight);
		
		random = new Random();
		
		int centerX = winWidth / 2 - elementWidth / 2;
		backButton = new Button(centerX, winHeight - elementHeight - BUTTON_SPACING, elementWidth, elementHeight,
				this, "Back", InterfaceTheme.BIG_FONT);
		createButton = new Button(centerX, backButton.getY() - elementHeight - BUTTON_SPACING, elementWidth, elementHeight, 
				this, "Create World", InterfaceTheme.BIG_FONT);
		
		nameBox = new Textbox(centerX, winHeight / 2 - elementHeight, elementWidth, elementHeight, MAX_NAME_LENGTH);
		nameBox.setHint("World Name");
		nameBox.addBlockedCharacter('\\');
		nameBox.addBlockedCharacter('/');
		nameBox.addBlockedCharacter(':');
		nameBox.addBlockedCharacter('*');
		nameBox.addBlockedCharacter('?');
		nameBox.addBlockedCharacter('"');
		nameBox.addBlockedCharacter('\'');
		nameBox.addBlockedCharacter('<');
		nameBox.addBlockedCharacter('>');
		nameBox.addBlockedCharacter('|');
		nameBox.addBlockedCharacter('.');
		nameBox.setTextChangeListener(this);
		
		seedBox = new Textbox(centerX, nameBox.getY() + nameBox.getHeight() + TEXTBOX_SPACING, elementWidth, elementHeight, MAX_NAME_LENGTH);
		seedBox.setHint("Seed");
		
		addButton(createButton);
		addButton(backButton);
		addElement(nameBox);
		addElement(seedBox);
	}
	
	public String getName() {
		return nameBox.getText();
	}
	public long getSeed() {
		if (seedBox.getText().length() > 0) {
			try {
				return Long.parseLong(seedBox.getText());
			} catch (NumberFormatException ex) {
				return seedBox.getText().hashCode();
			}
		} else {
			return random.nextLong();
		}
	}
	
	public void onClick(MenuElement clicked) {
		if (clicked == createButton) {
			if (getName().length() > 0) {
				menuListener.onButtonClick(this, CREATE_WORLD);
			} else {
				nameBox.startError("Please enter a world name.");
			}
		} else if (clicked == backButton) {
			menuListener.onButtonClick(this, BACK);
		}
	}
	
	public void nameboxError(String error) {
		nameBox.startError(error);
	}

	public void onTextChanged(MenuElement element, String text) {
		if (element == nameBox && nameBox.hasError()) {
			nameBox.endError();
		}
	}

}
