package com.dpSoftware.fp.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.dpSoftware.fp.items.ItemStack;

public class LootTable {
	
	private ArrayList<LootEntry> entries;
	
	public LootTable(LootEntry... entries) {
		this.entries = new ArrayList<>(Arrays.asList(entries));
	}
	
	public void addEntry(LootEntry entry) {
		entries.add(entry);
	}
	public void clear() {
		entries.clear();
	}

	public ArrayList<ItemStack> runLootTable(Random rand) {
		ArrayList<ItemStack> result = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			int amount = entries.get(i).runAndGetAmount(rand);
			if (amount > 0) {
				result.add(new ItemStack(entries.get(i).getItem(), amount));
			}
		}
		return result;
	}
	
}
