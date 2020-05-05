package com.dpSoftware.fp.world;

import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.RandomUtils;

public class LootEntry {

	private Items item;
	private int amountMin;
	private int amountMax;
	private double chance;
	
	public LootEntry(Items item, int amountMin, int amountMax, double chance) {
		super();
		this.item = item;
		this.amountMin = amountMin;
		this.amountMax = amountMax;
		this.chance = chance;
	}
	
	public int runAndGetAmount(Random rand) {
		if (RandomUtils.doesChanceSucceed(rand, chance)) {
			if (amountMin == amountMax) return amountMin;
			return RandomUtils.randomIntBetween(rand, amountMin, amountMax);
		}
		return 0;
	}
	
	public Items getItem() {
		return item;
	}
	public int getAmountMin() {
		return amountMin;
	}
	public int getAmountMax() {
		return amountMax;
	}
	public double getChance() {
		return chance;
	}
}
