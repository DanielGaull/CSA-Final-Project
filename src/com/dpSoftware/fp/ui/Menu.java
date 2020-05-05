package com.dpSoftware.fp.ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Menu implements IClickListener {

	private ArrayList<MenuElement> elements;
	protected IMenuListener menuListener;
	
	public Menu(int winWidth, int winHeight, IMenuListener menuListener) {
		this.menuListener = menuListener;
		this.elements = new ArrayList<MenuElement>();
	}
	
	public void update(long passedTime) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).update(passedTime);
		}
	}
	public void draw(Graphics2D g) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).draw(g);
		}
	}
	
	public void addButton(Button button) {
		button.setClickListener(this);
		addElement(button);
	}
	public void addElement(MenuElement element) {
		elements.add(element);
	}

	public void onClick(MenuElement clicked) {}
	
}
