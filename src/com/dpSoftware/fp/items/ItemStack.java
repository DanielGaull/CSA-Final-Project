package com.dpSoftware.fp.items;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemStack {

	private Items item;
	private int amount;

	public ItemStack(Items item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	public Items getItem() {
		return item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int value) {
		amount = value;
	}

	public void changeAmount(int value) {
		amount += value;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ItemStack) {
			ItemStack otherStack = (ItemStack) obj;
			return otherStack.item == item && otherStack.amount == amount;
		}
		return false;
	}

	public ItemStack clone() {
		return new ItemStack(item, amount);
	}

	public String toString() {
		return new JSONObject(this).toString();
	}

	public static ItemStack fromJsonObj(JSONObject obj) {
		Items item = null;
		try {
			item = Enum.valueOf(Items.class, obj.getString("item"));
		} catch (JSONException jsonEx) {
			return empty();
		}
		int amount = obj.getInt("amount");
		return new ItemStack(item, amount);
	}

	public static ItemStack empty() {
		return new ItemStack(null, 0);
	}

	public boolean checkEmpty() {
		return item == null;
	}
}
