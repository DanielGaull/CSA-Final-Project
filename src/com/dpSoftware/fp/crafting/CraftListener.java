package com.dpSoftware.fp.crafting;

public interface CraftListener {

	public void craft(CraftingRecipe recipe);
	public boolean canCraft(CraftingRecipe recipe);
	
}
