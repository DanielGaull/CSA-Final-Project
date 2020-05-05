package com.dpSoftware.fp.crafting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.dpSoftware.fp.items.ItemDisplay;

public class CraftingRecipeDisplay {

	private int x;
	private int y;
	public static final int  WIDTH = 500;
	public  static final int HEIGHT = 100;
	private Color border;
	private Color fill;
	private static final int BORDER_SIZE = 5;
	
	private Font font;
	private String name;
	private Color textColor;
	private static final int TEXT_Y_SPACING = 3;
	
	private CraftingRecipe recipe;
	private ItemDisplay[] ingredientDisplays;
	private ItemDisplay resultDisplay;
	private static final int INGREDIENT_SIZE = 48;
	private static final int RESULT_SIZE = 64;
	private static final int SPACING = 10;
	
	public CraftingRecipeDisplay(int x, int y, Color border, Color fill, CraftingRecipe recipe, Font font, Color textColor,
			int winWidth, int winHeight) {
		this.x = x;
		this.y = y;
		this.border = border;
		this.fill = fill;
		this.recipe = recipe;
		this.font = font;
		this.textColor = textColor;
		
		resultDisplay = new ItemDisplay(x + SPACING, y + HEIGHT / 2 - RESULT_SIZE / 2, RESULT_SIZE, RESULT_SIZE, recipe.getResult(), true, 
				winWidth, winHeight);
		
		ingredientDisplays = new ItemDisplay[recipe.getIngredients().size()];
		int ingredStartX = resultDisplay.getX() + resultDisplay.getWidth() + SPACING;
		int ingredY = y + HEIGHT / 2 - INGREDIENT_SIZE / 2;
		for (int i = 0; i < ingredientDisplays.length; i++) {
			ingredientDisplays[i] = new ItemDisplay(ingredStartX + (INGREDIENT_SIZE + SPACING) * i, ingredY, INGREDIENT_SIZE, INGREDIENT_SIZE,
					recipe.getIngredients().get(i), true, winWidth, winHeight);
		}
		
		name = recipe.getResult().getItem().getName();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(fill);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(border);
		g.setStroke(new BasicStroke(BORDER_SIZE));
		g.drawRect(x, y, WIDTH, HEIGHT);
		
		for (int i = 0; i < ingredientDisplays.length; i++) {
			ingredientDisplays[i].draw(g);
		}
		for (int i = 0; i < ingredientDisplays.length; i++) {
			ingredientDisplays[i].drawDesc(g);
		}
		resultDisplay.draw(g);
		resultDisplay.drawDesc(g);
		
		g.setFont(font);
		g.setColor(textColor);
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(name, x + BORDER_SIZE, y + metrics.getHeight() - TEXT_Y_SPACING);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return WIDTH;
	}
	public int getHeight() {
		return HEIGHT;
	}
	public CraftingRecipe getRecipe() {
		return recipe;
	}
	
}