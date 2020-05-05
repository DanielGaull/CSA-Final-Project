package com.dpSoftware.fp.crafting;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import com.dpSoftware.fp.items.Inventory;
import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.ui.Button;
import com.dpSoftware.fp.ui.IClickListener;
import com.dpSoftware.fp.ui.ITextChangeListener;
import com.dpSoftware.fp.ui.InterfaceTheme;
import com.dpSoftware.fp.ui.MenuElement;
import com.dpSoftware.fp.ui.Textbox;

public class CraftingInterface implements IClickListener, ITextChangeListener {

	private ArrayList<CraftingRecipeDisplay> recipeDisplays;
	private int currentRecipeIndex;
	
	private CraftListener craftListener;
	
	private Inventory inventory;
	
	private Button craftButton;
	private Button upButton;
	private Button downButton;
	private Textbox searchBox;
	private boolean showUpButton;
	private boolean showDownButton;
	private static final int CRAFT_BUTTON_WIDTH = 55;
	private static final int BUTTON_SIZE = 35;
	private static final int BUTTON_SPACING_X = 10;
	private static final int BUTTON_SPACING_Y = 5;
	private static final int SEARCHBOX_WIDTH = 275;
	private static final int SEARCHBOX_HEIGHT = 25;
	private static final int SEARCHBOX_MAX_TEXT_LENGTH = SEARCHBOX_WIDTH / 10;
	private static final int SEARCHBOX_SPACING = 7;
	
	public CraftingInterface(ArrayList<CraftingRecipe> recipes, int x, int y, CraftListener craftListener,
			int winWidth, int winHeight) {
		this.craftListener = craftListener;
		
		recipeDisplays = new ArrayList<>();
		for (int i = 0; i < recipes.size(); i++) {
			recipeDisplays.add(new CraftingRecipeDisplay(x, y, InterfaceTheme.INTERFACE_BORDER, InterfaceTheme.INTERFACE_BG, recipes.get(i), InterfaceTheme.MED_FONT,
					InterfaceTheme.INTERFACE_TEXT, winWidth, winHeight));
		}
		
		currentRecipeIndex = 0;
		
		craftButton = new Button(x + CraftingRecipeDisplay.WIDTH + BUTTON_SPACING_X, y + CraftingRecipeDisplay.HEIGHT / 2 - BUTTON_SIZE / 2, 
				CRAFT_BUTTON_WIDTH, BUTTON_SIZE, this, "Craft", InterfaceTheme.MED_FONT);
		upButton = new Button(x - BUTTON_SPACING_X - BUTTON_SIZE, y + BUTTON_SPACING_Y, BUTTON_SIZE, BUTTON_SIZE, this, 
				"\uD83E\uDC45", InterfaceTheme.MED_FONT);
		downButton = new Button(upButton.getX(), y + CraftingRecipeDisplay.HEIGHT - BUTTON_SIZE - BUTTON_SPACING_Y, BUTTON_SIZE, BUTTON_SIZE, this, 
				"\uD83E\uDC47", InterfaceTheme.MED_FONT);
		
		searchBox = new Textbox(x + CraftingRecipeDisplay.WIDTH - SEARCHBOX_WIDTH, y - SEARCHBOX_HEIGHT - SEARCHBOX_SPACING,
				SEARCHBOX_WIDTH, SEARCHBOX_HEIGHT, SEARCHBOX_MAX_TEXT_LENGTH, InterfaceTheme.MED_FONT);
		searchBox.setHint("Search");
		searchBox.setTextChangeListener(this);
	}
	
	public void update(long passedTime) {
		if (currentRecipeIndex <= 0) {
			showUpButton = false;
		} else {
			showUpButton = true;
		}
		if (currentRecipeIndex >= recipeDisplays.size() - 1) {
			showDownButton = false;
		} else {
			showDownButton = true;
		}
		if (craftListener.canCraft(recipeDisplays.get(currentRecipeIndex).getRecipe())) {
			craftButton.enable();
		} else {
			craftButton.disable();
		}
		
		searchBox.update(passedTime);
		
		craftButton.update(passedTime);
		if (showUpButton) {
			upButton.update(passedTime);
		}
		if (showDownButton) {
			downButton.update(passedTime);
		}
	}
	public void draw(Graphics2D g) {
		craftButton.draw(g);
		if (showUpButton) {
			upButton.draw(g);
		}
		if (showDownButton) {
			downButton.draw(g);			
		}
		searchBox.draw(g);
		
		recipeDisplays.get(currentRecipeIndex).draw(g);
	}
	
	public void reset() {
		searchBox.resetText();
	}
	
	// Sorts the recipes based on what items are available in the specified inventory
	public void sort(Inventory inventory) {
		this.inventory = inventory;
		
		sortDefault();
		
		ArrayList<CraftingRecipeDisplay> canCraftList = new ArrayList<>();
		ArrayList<CraftingRecipeDisplay> hasMatsList = new ArrayList<>();
		ArrayList<CraftingRecipeDisplay> bottomList = new ArrayList<>();
		for (int i = 0; i < recipeDisplays.size(); i++) {
			if (inventory.canCraft(recipeDisplays.get(i).getRecipe())) {
				canCraftList.add(recipeDisplays.get(i));
			} else if (inventory.hasAnyItems(recipeDisplays.get(i).getRecipe())) {
				hasMatsList.add(recipeDisplays.get(i));
			} else {
				bottomList.add(recipeDisplays.get(i));
			}
		}
		
		concatRecipeDisplayLists(canCraftList, hasMatsList, bottomList);
		currentRecipeIndex = 0;
	}
	public void search(String searchTerm) {
		sort(inventory);
		searchTerm = searchTerm.toLowerCase();
		
		ArrayList<CraftingRecipeDisplay> nameFoundList = new ArrayList<>();
		ArrayList<CraftingRecipeDisplay> ingredientFoundList = new ArrayList<>();
		ArrayList<CraftingRecipeDisplay> bottomList = new ArrayList<>();
		for (int i = 0; i < recipeDisplays.size(); i++) {
			if (recipeDisplays.get(i).getRecipe().getResult().getItem().getName().toLowerCase().indexOf(searchTerm) >= 0) {
				// The search term is in the name of the result of this recipe
				nameFoundList.add(recipeDisplays.get(i));
			} else {
				boolean ingredientFound = false;
				ArrayList<ItemStack> ingredients = recipeDisplays.get(i).getRecipe().getIngredients();
				for (int j = 0; j < ingredients.size(); j++) {
					if (ingredients.get(j).getItem().getName().toLowerCase().indexOf(searchTerm) >= 0) {
						// The search term is in the name of one of the ingredients of this recipe
						ingredientFoundList.add(recipeDisplays.get(i));
						ingredientFound = true;
						break;
					}
				}
				if (!ingredientFound) {
					bottomList.add(recipeDisplays.get(i));
				}
			}
		}
		
		concatRecipeDisplayLists(nameFoundList, ingredientFoundList, bottomList);
		currentRecipeIndex = 0;
	}
	public void sortDefault() {
		Collections.sort(recipeDisplays, (r1, r2) -> r1.getRecipe().getResult().getItem().getName().compareTo(r2.getRecipe().getResult().getItem().getName()));
	}
	
	private void concatRecipeDisplayLists(ArrayList<CraftingRecipeDisplay>... lists) {
		recipeDisplays.clear();
		for (int i = 0; i < lists.length; i++) {
			recipeDisplays.addAll(lists[i]);
		}
	}
	
	public boolean isTyping() {
		return searchBox.isSelected();
	}

	public void onClick(MenuElement clicked) {
		if (clicked == upButton) {
			if (currentRecipeIndex > 0) {
				currentRecipeIndex--;
			}
		} else if (clicked == downButton) {
			if (currentRecipeIndex < recipeDisplays.size() - 1) {
				currentRecipeIndex++;
			}
		} else if (clicked == craftButton) {
			craftListener.craft(recipeDisplays.get(currentRecipeIndex).getRecipe());
		}
	}
	public void onTextChanged(MenuElement element, String text) {
		if (element == searchBox) {
			if (text.length() > 0) {
				// Need to put recipes for items with this text on top
				// Items with this in the name go first, and items with it 
				// in an ingredient's name go later
				search(text);
			} else {
				// Reset everything
				sort(inventory);
			}
		}
	}
	
}
