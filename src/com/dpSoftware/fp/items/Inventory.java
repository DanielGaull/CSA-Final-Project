package com.dpSoftware.fp.items;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dpSoftware.fp.crafting.CraftingRecipe;

public class Inventory {

	public static final int INV_ROWS = 5; // The first row is the hotbar
	public static final int INV_COLS = 10;
	public static final int HOTBAR_SLOTS = INV_COLS;
	public static final int HOTBAR_ROW = 0;
	private ItemStack[][] invSlots;
	private ItemStack shieldSlot;
	private int selectedSlot; // The selected hotbar slot

	public Inventory() {
		invSlots = new ItemStack[INV_ROWS][INV_COLS];
		for (int i = 0; i < INV_ROWS; i++) {
			for (int j = 0; j < INV_COLS; j++) {
				invSlots[i][j] = ItemStack.empty();
			}
		}
		shieldSlot = ItemStack.empty();
		selectedSlot = 0;
	}
	public Inventory(ItemStack[][] slots, ItemStack shieldSlot, int selectedSlot) {
		this.invSlots = slots;
		this.shieldSlot = shieldSlot;
		this.selectedSlot = selectedSlot;
	}

	public ItemStack getInvItem(int row, int col) {
		return invSlots[row][col];
	}

	public void setSlot(int row, int col, ItemStack itemStack) {
		invSlots[row][col] = itemStack;
	}

	public void swapSlots(int row1, int col1, int row2, int col2) {
		ItemStack temp = invSlots[row1][col1];
		invSlots[row1][col1] = invSlots[row2][col2];
		invSlots[row2][col2] = temp;
	}

	// Returns the amount of items that were UNSUCCESSFULLY added
	public int addItem(Items item, int amount) {
		// Used to store the location of the first empty inventory slot, in case none
		// of the existing stacks contain the right item or are full
		int firstEmptyRow = -1;
		int firstEmptyCol = -1;
		for (int i = 0; i < INV_ROWS; i++) {
			for (int j = 0; j < INV_COLS; j++) {
				if (invSlots[i][j].checkEmpty()) {
					if (firstEmptyRow < 0 || firstEmptyCol < 0) {
						firstEmptyRow = i;
						firstEmptyCol = j;
					}
				} else {
					// Ignore if this stack is its maximum size
					if (invSlots[i][j].getItem() == item
							&& invSlots[i][j].getAmount() < item.getMaxStackSize()) {
						if (invSlots[i][j].getAmount() + amount > item.getMaxStackSize()) {
							// Adding to this stack will make it too large - 
							// we add enough to fill the stack, then recursively call
							// this addItem method to get the rest of the items
							// added in
							int amountRemaining = invSlots[i][j].getAmount() + amount - item.getMaxStackSize();
							invSlots[i][j].setAmount(item.getMaxStackSize());
							return addItem(item, amountRemaining);
						} else {
							// This stack will not overflow when "amount" is added to it
							invSlots[i][j].changeAmount(amount);
							return 0;
						}
					}
				}
			}
		}

		// Didn't find an existing item stack, so we add a new one (if possible)
		if (firstEmptyRow >= 0 && firstEmptyCol >= 0) {
			if (amount <= item.getMaxStackSize()) {
				invSlots[firstEmptyRow][firstEmptyCol] = new ItemStack(item, amount);
				return 0;
			} else {
				int amountRemaining = amount - item.getMaxStackSize();
				invSlots[firstEmptyRow][firstEmptyCol] = new ItemStack(item, item.getMaxStackSize());
				return addItem(item, amountRemaining);
			}
		} else {
			return amount;
		}
	}
	public int addItem(ItemStack itemStack) {
		return addItem(itemStack.getItem(), itemStack.getAmount());
	}
	
	// Returns the amount of items unsuccessfully removed
	public int remove(ItemStack itemStack) {
		int amountToRemove = itemStack.getAmount();
		for (int i = 0; i < INV_ROWS; i++) {
			for (int j = 0; j < INV_COLS; j++) {
				if (invSlots[i][j].getItem() == itemStack.getItem()) {
					if (invSlots[i][j].getAmount() > amountToRemove) {
						// Don't need to take away this full stack in order to finish removing all the necessary items
						// Return 0 after reducing the stack's amount
						invSlots[i][j].setAmount(invSlots[i][j].getAmount() - amountToRemove);
						return 0;
					} else if (invSlots[i][j].getAmount() == amountToRemove) {
						// We can still return 0 as this is the last stack we must modify,
						// but need to set this stack to be blank so that other items can be put here
						invSlots[i][j] = ItemStack.empty();
						return 0;
					} else {
						// Need to remove this entire stack, but there are still more items to remove
						amountToRemove -= invSlots[i][j].getAmount();
						invSlots[i][j] = ItemStack.empty();
					}
				}
			}
		}
		// If we got here, then we haven't been able to remove all the items, so we return the amount that we had left to remove
		return amountToRemove;
	}
	public int removeFromSelectedSlot(int amount) {
		return removeFromSlot(HOTBAR_ROW, selectedSlot, amount);
	}
	public int removeFromSlot(int row, int col, int amount) {
		int amountRemaining;
		if (invSlots[row][col].getAmount() >= amount) {
			invSlots[row][col].changeAmount(-amount);
			amountRemaining = 0;
		} else {
			amountRemaining = amount - invSlots[row][col].getAmount();
			invSlots[row][col].setAmount(0);
		}
		if (invSlots[row][col].getAmount() <= 0) {
			invSlots[row][col] = ItemStack.empty();
		}
		return amountRemaining;
	}

	public ItemStack getShield() {
		return shieldSlot;
	}
	public void setShield(ItemStack stack) {
		shieldSlot = stack;
	}
	
	public ItemStack getHotbarItem(int index) {
		return invSlots[HOTBAR_ROW][index];
	}
	public ItemStack getSelectedItem() {
		return getHotbarItem(selectedSlot);
	}
	
	public int getSelectedSlot() {
		return selectedSlot;
	}
	public void setSelectedSlot(int value) {
		selectedSlot = value;
	}
	
	public ItemStack[][] getInvSlots() {
		return invSlots;
	}
	
	public int countOf(Items item) {
		int count = 0;
		for (int i = 0; i < INV_ROWS; i++) {
			for (int j = 0; j < INV_COLS; j++) {
				if (invSlots[i][j].getItem() == item) {
					count += invSlots[i][j].getAmount();
				}
			}
		}
		return count;
	}
	
	public boolean canCraft(CraftingRecipe recipe) {
		ArrayList<ItemStack> ingredients = recipe.getIngredients();
		for (int i = 0; i < ingredients.size(); i++) {
			if (countOf(ingredients.get(i).getItem()) < ingredients.get(i).getAmount()) {
				// Found an ingredient that the inventory does not have enough
				// of to satisfy the recipe's
				// requirements. Return false as this recipe is uncraftable
				return false;
			}
		}
		return true;
	}
	public boolean hasAnyItems(CraftingRecipe recipe) {
		// Returns true if this inventory has any of the items required in this recipe
		ArrayList<ItemStack> ingredients = recipe.getIngredients();
		for (int i = 0; i < ingredients.size(); i++) {
			if (countOf(ingredients.get(i).getItem()) > 0) {
				// Have at least one of the ingredients required
				return true;
			}
		}
		return false;
	}
	
	public static Inventory fromJsonObject(JSONObject jsonObj) {
		int selectedSlot = jsonObj.getInt("selectedSlot");
		ItemStack shieldSlot = ItemStack.fromJsonObj(jsonObj.getJSONObject("shield"));
		ItemStack[][] slots = new ItemStack[INV_ROWS][INV_COLS];
		JSONArray slotRows = jsonObj.getJSONArray("invSlots");
		for (int i = 0; i < slotRows.length(); i++) {
			JSONArray slotCols = slotRows.getJSONArray(i);
			for (int j = 0; j < slotCols.length(); j++) {
				slots[i][j] = ItemStack.fromJsonObj(slotCols.getJSONObject(j));
			}
		}
		return new Inventory(slots, shieldSlot, selectedSlot);
	}
}
