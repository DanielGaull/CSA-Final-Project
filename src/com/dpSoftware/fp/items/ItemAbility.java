package com.dpSoftware.fp.items;

import java.util.ArrayList;

public class ItemAbility {

	private ArrayList<String> descriptionLines;
	
	private double healthHealed;
	private int healthHealUnitTime;
	private double maxHealthAdded;
	
	private double energyHealed;
	private int energyHealUnitTime;
	private double maxEnergyAdded;
	
	private int damageBoostPercent;
	private int damageBoostQuantity;
	private int defenseBoostPercent;
	private int defenseBoostQuantity;
	
	private double luckBoost;
	
	private double breakSpeedBoost;
	
	public ItemAbility() {
		descriptionLines = new ArrayList<>();
	}
	
	public static ItemAbility honeyShield() { 
		ItemAbility honeyShield = new ItemAbility();
		honeyShield.setHealthHealed(1);
		honeyShield.setHealthHealUnitTime(2000);
		honeyShield.setMaxHealthAdded(50);
		honeyShield.generateDescription();
		return honeyShield;
	}
	
	private void generateDescription() {
		if (healthHealed > 0) {
			double healTimeSeconds = (healthHealUnitTime / 1000.0);
			addDescriptionLine("+" + (int) healthHealed + " health every " + 
					(healTimeSeconds > 1 ? ((int) healTimeSeconds + " seconds") : "second"));
		}
		if (maxHealthAdded > 0) {
			addDescriptionLine("+" + (int) maxHealthAdded + " Max Health");
		}
		
		if (energyHealed > 0) {
			double healTimeSeconds = (energyHealUnitTime / 1000.0);
			addDescriptionLine("+" + (int) energyHealed + " energy every " + 
					(healTimeSeconds > 1 ? ((int) healTimeSeconds + " seconds") : "second"));
		}
		if (maxEnergyAdded > 0) {
			addDescriptionLine("+" + (int) maxEnergyAdded + " Max Energy");
		}

		if (damageBoostQuantity > 0) {
			addDescriptionLine("+" + damageBoostQuantity + " Damage");
		}
		if (defenseBoostQuantity > 0) {
			addDescriptionLine("+" + defenseBoostQuantity + " Defense");
		}
		if (damageBoostPercent > 0) {
			addDescriptionLine("+" + damageBoostPercent + "% Damage");
		}
		if (defenseBoostPercent > 0) {
			addDescriptionLine("+" + damageBoostPercent + "% Defense");
		}
		
		if (breakSpeedBoost > 0) {
			addDescriptionLine("+" + breakSpeedBoost + " Tool Speed");
		}
	}
	
	public ArrayList<String> getDescription() {
		return descriptionLines;
	}
	public void addDescriptionLine(String value) {
		descriptionLines.add(value);
	}

	public double getHealthHealed() {
		return healthHealed;
	}

	public int getHealthHealUnitTime() {
		return healthHealUnitTime;
	}

	public double getMaxHealthAdded() {
		return maxHealthAdded;
	}

	public double getEnergyHealed() {
		return energyHealed;
	}

	public int getEnergyHealUnitTime() {
		return energyHealUnitTime;
	}

	public double getMaxEnergyAdded() {
		return maxEnergyAdded;
	}

	public int getDamageBoostPercent() {
		return damageBoostPercent;
	}

	public int getDamageBoostQuantity() {
		return damageBoostQuantity;
	}

	public int getDefenseBoostPercent() {
		return defenseBoostPercent;
	}

	public int getDefenseBoostQuantity() {
		return defenseBoostQuantity;
	}

	public double getLuckBoost() {
		return luckBoost;
	}

	public double getBreakSpeedBoost() {
		return breakSpeedBoost;
	}

	public void setHealthHealed(double healthHealed) {
		this.healthHealed = healthHealed;
	}

	public void setHealthHealUnitTime(int healthHealUnitTime) {
		this.healthHealUnitTime = healthHealUnitTime;
	}

	public void setMaxHealthAdded(double maxHealthAdded) {
		this.maxHealthAdded = maxHealthAdded;
	}

	public void setEnergyHealed(double energyHealed) {
		this.energyHealed = energyHealed;
	}

	public void setEnergyHealUnitTime(int energyHealUnitTime) {
		this.energyHealUnitTime = energyHealUnitTime;
	}

	public void setMaxEnergyAdded(double maxEnergyAdded) {
		this.maxEnergyAdded = maxEnergyAdded;
	}

	public void setDamageBoostPercent(int damageBoostPercent) {
		this.damageBoostPercent = damageBoostPercent;
	}

	public void setDamageBoostQuantity(int damageBoostQuantity) {
		this.damageBoostQuantity = damageBoostQuantity;
	}

	public void setDefenseBoostPercent(int defenseBoostPercent) {
		this.defenseBoostPercent = defenseBoostPercent;
	}

	public void setDefenseBoostQuantity(int defenseBoostQuantity) {
		this.defenseBoostQuantity = defenseBoostQuantity;
	}

	public void setLuckBoost(double luckBoost) {
		this.luckBoost = luckBoost;
	}

	public void setBreakSpeedBoost(double breakSpeedBoost) {
		this.breakSpeedBoost = breakSpeedBoost;
	}
}
