package com.dpSoftware.fp.crafting;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONWriter;

import com.dpSoftware.fp.items.ItemStack;

public class CraftingRecipe {

	private ArrayList<ItemStack> ingredients;
	private ItemStack result;
	
	public CraftingRecipe(ItemStack result, ItemStack... ingredients) {
		this(result, new ArrayList<ItemStack>(Arrays.asList(ingredients)));
	}
	public CraftingRecipe(ItemStack result, ArrayList<ItemStack> ingredients) {
		this.result = result;
		this.ingredients = ingredients;
	}
	
	public ItemStack getResult() {
		return result;
	}
	public ArrayList<ItemStack> getIngredients() {
		return ingredients;
	}
	
	public String toJSON() { 
		return new JSONObject(this).toString();
	}
	
	public static CraftingRecipe fromJSON(String json) {
		JSONTokener tokener = new JSONTokener(json);
		JSONObject recipeObj = new JSONObject(tokener);
		ItemStack result = ItemStack.fromJsonObj(recipeObj.getJSONObject("result"));
		JSONArray ingredientArray = recipeObj.getJSONArray("ingredients");
		ArrayList<ItemStack> ingredients = new ArrayList<>();
		for (int i = 0; i < ingredientArray.length(); i++) {
			JSONObject ingredientObj = ingredientArray.getJSONObject(i);
			ingredients.add(ItemStack.fromJsonObj(ingredientObj));
		}
		return new CraftingRecipe(result, ingredients);
	}
}
