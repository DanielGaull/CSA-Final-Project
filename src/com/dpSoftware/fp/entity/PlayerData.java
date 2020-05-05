package com.dpSoftware.fp.entity;

import org.json.JSONObject;

import com.dpSoftware.fp.items.Inventory;

public class PlayerData {

	private double healthPercent;
	private double energyPercent;
	
	private double x;
	private double y;
	
	private Inventory inventory;
	
	private int level;
	private int xp;
	private int coins;
	
	public PlayerData(double healthPercent, double energyPercent, double x, double y, Inventory inventory, int level, int xp, int coins) {
		this.healthPercent = healthPercent;
		this.energyPercent = energyPercent;
		this.x = x;
		this.y = y;
		this.inventory = inventory;
		this.level = level;
		this.xp = xp;
		this.coins = coins;
	}

	public double getHealthPercent() {
		return healthPercent;
	}

	public void setHealthPercent(double health) {
		this.healthPercent = health;
	}

	public double getEnergyPercent() {
		return energyPercent;
	}

	public void setEnergyPercent(double energy) {
		this.energyPercent = energy;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public static PlayerData createDefault() {
		return new PlayerData(PlayerEntity.DEFAULT_HEALTH, PlayerEntity.DEFAULT_ENERGY, 0, 0, new Inventory(),
				1, 0, 0);
	}
	
	public static PlayerData fromJsonObject(JSONObject jsonObj) {
		double healthPercent = 100;
		double energyPercent = 100;
		if (jsonObj.has("healthPercent")) {
			healthPercent = jsonObj.getDouble("healthPercent");
		} else {
			healthPercent = jsonObj.getDouble("health");
		}
		if (jsonObj.has("energyPercent")) {
			energyPercent = jsonObj.getDouble("energyPercent");
		} else {
			energyPercent = jsonObj.getDouble("energy");
		}
		
		double x = jsonObj.getDouble("x");
		double y = jsonObj.getDouble("y");
		
		Inventory inventory = Inventory.fromJsonObject(jsonObj.getJSONObject("inventory"));
		
		int coins = jsonObj.getInt("coins");
		int xp = jsonObj.getInt("xp");
		int level = jsonObj.getInt("level");
		
		return new PlayerData(healthPercent, energyPercent, x, y, inventory, level, xp, coins);
	}
}
