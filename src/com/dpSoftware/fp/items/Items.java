package com.dpSoftware.fp.items;

import com.dpSoftware.fp.entity.HoldOrientations;
import com.dpSoftware.fp.entity.SpawnableEntities;
import com.dpSoftware.fp.ui.Point;

public enum Items {

	Wood(ItemCategories.None, 75, "wood", "Wood", HoldOrientations.Normal, ItemRarities.Common),
	Leaves(ItemCategories.None, 100, "leaves", "Leaves", HoldOrientations.Normal, ItemRarities.Common),
	Cactus(ItemCategories.Food, 75, "cactus", "Cactus", HoldOrientations.Normal, ItemRarities.Common, 10),
	Sand(ItemCategories.None, 100, "sand", "Sand", HoldOrientations.Normal, ItemRarities.Common),
	Metal(ItemCategories.None, 75, "metal", "Metal", HoldOrientations.Normal, ItemRarities.Common),
	Axe(ItemCategories.Tool, 1, "axe", "Axe", HoldOrientations.DoubleHandedClose, ItemRarities.Common, "Chopping"),
	Pickaxe(ItemCategories.Tool, 1, "pickaxe", "Pickaxe", HoldOrientations.DoubleHandedClose, ItemRarities.Common, "Mining"),
	Shovel(ItemCategories.Tool, 1, "shovel", "Shovel", HoldOrientations.DoubleHandedClose, ItemRarities.Common, "Digging"),
	Flower(ItemCategories.None, 150, "flower", "Flower", HoldOrientations.Normal, ItemRarities.Common),
	Stone(ItemCategories.None, 75, "stone", "Stone", HoldOrientations.Normal, ItemRarities.Common),
	Clippers(ItemCategories.Tool, 1, "clippers", "Clippers", HoldOrientations.DoubleHandedClose, ItemRarities.Common, "Clipping"),
	LeafSword(ItemCategories.Sword, "leafsword", "Leaf Sword", HoldOrientations.OneHandedRight, ItemRarities.Common, 40, 20),
	DesertSword(ItemCategories.Sword, "desertsword", "Desert Sword", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 125, 40),
	Gem(ItemCategories.None, 50, "gem", "Gem", HoldOrientations.Normal, ItemRarities.Uncommon),
	Cobalt(ItemCategories.None, 75, "cobalt", "Cobalt", HoldOrientations.Normal, ItemRarities.Uncommon),
	CobaltSword(ItemCategories.Sword, "cobaltsword", "Cobalt Sword", HoldOrientations.OneHandedRight, ItemRarities.Rare, 100, 30),
	Web(ItemCategories.None, 100, "web", "Web", HoldOrientations.Normal, ItemRarities.Common),
	MoltenRock(ItemCategories.None, 75, "moltenrock", "Molten Rock", HoldOrientations.Normal, ItemRarities.Uncommon),
	Scroll(ItemCategories.None, 100, "scroll", "Scroll", HoldOrientations.Normal, ItemRarities.Common),
	Banana(ItemCategories.Food, 75, "banana", "Banana", HoldOrientations.Normal, ItemRarities.Common, 13),
	Apple(ItemCategories.Food, 75, "apple", "Apple", HoldOrientations.Normal, ItemRarities.Common, 15),
	Blueberries(ItemCategories.Food, 75, "blueberries", "Blueberries", HoldOrientations.Normal, ItemRarities.Common, 10),
	FruitSalad(ItemCategories.Food, 75, "fruitsalad", "Fruit Salad", HoldOrientations.Normal, ItemRarities.Common, 35),
	Poison(ItemCategories.None, 50, "poison", "Poison", HoldOrientations.Normal, ItemRarities.Common),
	TarantulaEgg(ItemCategories.Summon, 1, "tarantulaegg", "Tarantula Egg", HoldOrientations.Normal, ItemRarities.Rare, SpawnableEntities.BloodTarantulaBoss),
	BloodSilk(ItemCategories.None, 100, "bloodsilk", "Blood Silk", HoldOrientations.Normal, ItemRarities.Rare),
	Snow(ItemCategories.None, 100, "snow", "Snow", HoldOrientations.Normal, ItemRarities.Common),
	Ice(ItemCategories.None, 75, "ice", "Ice", HoldOrientations.Normal, ItemRarities.Common),
	Cranberries(ItemCategories.Food, 75, "cranberries", "Cranberries", HoldOrientations.Normal, ItemRarities.Common, 10),
	GlacierSword(ItemCategories.Sword, "glaciersword", "Glacier Sword", HoldOrientations.OneHandedRight, ItemRarities.Rare, 25, 10),
	ArcticSword(ItemCategories.Sword, "arcticsword", "Arctic Sword", HoldOrientations.OneHandedRight, ItemRarities.Epic, 205, 60),
	TarantulaSword(ItemCategories.Sword, "tarantulasword", "Tarantula Sword", HoldOrientations.OneHandedRight, ItemRarities.Epic, 220, 40),
	Honey(ItemCategories.Food, 50, "honey", "Honey", HoldOrientations.Normal, ItemRarities.Common, 15),
	BasicSword(ItemCategories.Sword, "metalsword", "Basic Sword", HoldOrientations.OneHandedRight, ItemRarities.Common, 10, 0),
	YetiIcicle(ItemCategories.None, 1, "yetiicicle", "Yeti's Icicle", HoldOrientations.Normal, ItemRarities.Epic),
	FrostTablet(ItemCategories.Summon, 1, "frosttablet", "Frost Tablet", HoldOrientations.Normal, ItemRarities.Rare, SpawnableEntities.YetiBoss),
	VolcanoSword(ItemCategories.Sword, "volcanosword", "Volcano Sword", HoldOrientations.OneHandedRight, ItemRarities.Epic, 275, 25),
	ForestShield(ItemCategories.Shield, "forestshield", "Forest Shield", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 20, 75),
	AutumnShield(ItemCategories.Shield, "autumnshield", "Autumn Shield", HoldOrientations.OneHandedRight, ItemRarities.Rare, 85, 170),
	IceShield(ItemCategories.Shield, "iceshield", "Ice Shield", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 30, 80),
	WinterShield(ItemCategories.Shield, "wintershield", "Winter Shield", HoldOrientations.OneHandedRight, ItemRarities.Rare, 110, 160),
	BlossomShield(ItemCategories.Shield, "blossomshield", "Blossom Shield", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 20, 75),
	SpringShield(ItemCategories.Shield, "springshield", "Spring Shield", HoldOrientations.OneHandedRight, ItemRarities.Rare, 95, 165),
	SandShield(ItemCategories.Shield, "sandshield", "Sand Shield", HoldOrientations.OneHandedRight,  ItemRarities.Uncommon, 30, 65),
	SummerShield(ItemCategories.Shield, "summershield", "Summer Shield", HoldOrientations.OneHandedRight, ItemRarities.Rare, 120, 155),
	EarthShield(ItemCategories.Shield, "earthshield", "Earth Shield", HoldOrientations.OneHandedRight, ItemRarities.Legendary, 180, 250),
	Stinger(ItemCategories.None, 50, "stinger", "Stinger", HoldOrientations.Normal, ItemRarities.Common),
	Ectoplasm(ItemCategories.None, 75, "ectoplasm", "Ectoplasm", HoldOrientations.Normal, ItemRarities.Uncommon),
	Bone(ItemCategories.None, 100, "bone", "Bone", HoldOrientations.Normal, ItemRarities.Common),
	Skull(ItemCategories.None, 25, "skull", "Skull", HoldOrientations.Normal, ItemRarities.Uncommon),
	HoneyShield(ItemCategories.Shield, "honeyshield", "Honey Shield", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 50, 95, ItemAbility.honeyShield()),
	BoneSword(ItemCategories.Sword, "bonesword", "Bone Sword", HoldOrientations.OneHandedRight, ItemRarities.Common, 50, 25),
	SkullSword(ItemCategories.Sword, "skullsword", "Skull Sword", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 90, 45),
	ReaperBlade(ItemCategories.Sword, "reapersword", "Reaper Blade", HoldOrientations.OneHandedRight, ItemRarities.Epic, 175, 55),
	SpiritBrand(ItemCategories.Sword, "spiritbrand", "Spirit Brand", HoldOrientations.OneHandedRight, ItemRarities.Rare, 100, 50),
	MinerShield(ItemCategories.Shield, "minershield", "Miner Shield", HoldOrientations.OneHandedRight, ItemRarities.Common, 35, 100),
	GemShield(ItemCategories.Shield, "gemshield", "Gem Shield", HoldOrientations.OneHandedRight, ItemRarities.Rare, 135, 315),
	MountainShield(ItemCategories.Shield, "mountainshield", "Mountain Shield", HoldOrientations.OneHandedRight, ItemRarities.Legendary, 150, 450),
	WaspBlade(ItemCategories.Sword, "waspblade", "Wasp Blade", HoldOrientations.OneHandedRight, ItemRarities.Rare, 100, 50),
	Seaweed(ItemCategories.Food, 100, "seaweed", "Seaweed", HoldOrientations.Normal, ItemRarities.Common, 7),
	Carp(ItemCategories.Food, 50, "carp", "Carp", HoldOrientations.Normal, ItemRarities.Common, 10),
	Salmon(ItemCategories.Food, 50, "salmon", "Salmon", HoldOrientations.Normal, ItemRarities.Common, 15),
	Seashell(ItemCategories.None, 75, "seashell", "Seashell", HoldOrientations.Normal, ItemRarities.Common),
	AncientSeaSword(ItemCategories.Sword, "ancientseasword", "Ancient Sea Sword", HoldOrientations.OneHandedRight, ItemRarities.Uncommon, 40, 20),
	SalmonBlade(ItemCategories.Sword, "salmonblade", "Salmon Blade", HoldOrientations.OneHandedRight, ItemRarities.Rare, 55, 15),
	TsunamiSword(ItemCategories.Sword, "tsunamisword", "Tsunami Sword", HoldOrientations.OneHandedRight, ItemRarities.Epic, 250, 100),
	GlennSword(ItemCategories.Sword, "glennsword", "Glenn Sword", HoldOrientations.OneHandedRight, ItemRarities.Legendary, 500, 150),
	HoneyCrystal(ItemCategories.Food, 10, "honeycrystal", "Honey Crystal", HoldOrientations.Normal, ItemRarities.Uncommon, 0, 25, "Eating"),
	;
	
	private final ItemCategories category;
	private final int maxStackSize;
	private final String identifier;
	private final String name;
	private final int damage;
	private final int defense;
	private final int energyHealed;
	private final int healthHealed;
	private final String actionText;
	private final HoldOrientations holdOrientation;
	private final SpawnableEntities entitySpawned;
	private final ItemRarities rarity;
	private final ItemAbility ability;
	Items(ItemCategories category, int maxStackSize, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity) {
		this(category, maxStackSize, identifier, name, 0, 0, holdOrientation, rarity, 0, null, 0, null, new ItemAbility());
	}
	Items(ItemCategories category, int maxStackSize, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, String actionText) {
		this(category, maxStackSize, identifier, name, 0, 0, holdOrientation, rarity, 0, null, 0, actionText, new ItemAbility());
	}
	Items(ItemCategories category, int maxStackSize, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, int energyHealed) {
		this(category, maxStackSize, identifier, name, 0, 0, holdOrientation, rarity, energyHealed, null, 0, "Eating", new ItemAbility());
	}
	Items(ItemCategories category, int maxStackSize, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, int energyHealed, int healthHealed, String actionText) {
		this(category, maxStackSize, identifier, name, 0, 0, holdOrientation, rarity, energyHealed, null, healthHealed, actionText, new ItemAbility());
	}
	Items(ItemCategories category, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, int damage, int defense) {
		this(category, 1, identifier, name, damage, defense, holdOrientation, rarity, 0, null, 0, null, new ItemAbility());
	}
	Items(ItemCategories category, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, int damage, int defense, ItemAbility ability) {
		this(category, 1, identifier, name, damage, defense, holdOrientation, rarity, 0, null, 0, null, ability);
	}
	Items(ItemCategories category, int maxStackSize, String identifier, String name, HoldOrientations holdOrientation, 
			ItemRarities rarity, SpawnableEntities entitySpawned) {
		this(category, maxStackSize, identifier, name, 0, 0, holdOrientation, rarity, 0, entitySpawned, 0, null, new ItemAbility());
	}
	Items(ItemCategories category, int maxStackSize, String identifier, String name, int damage, int defense, 
			HoldOrientations holdOrientation, ItemRarities rarity, int energyHealed, SpawnableEntities entitySpawned, 
			int healthHealed, String actionText, ItemAbility ability) {
		this.category = category;
		this.maxStackSize = maxStackSize;
		this.identifier = identifier;
		this.name = name;
		this.damage = damage;
		this.defense = defense;
		this.holdOrientation = holdOrientation;
		this.energyHealed = energyHealed;
		this.entitySpawned = entitySpawned;
		this.healthHealed = healthHealed;
		this.actionText = actionText;
		this.rarity = rarity;
		this.ability = ability;
	}
	public String getTextureDirectory() {
		switch (category) {
			case Sword:
				return "swords\\" + identifier;
			case Shield:
				return "shields\\" + identifier;
			default:
				return "items\\" + identifier;					
		}
	}
	public String getRecipeDirectory() {
		switch (category) {
			case Sword:
				return "swords\\" + identifier;
			case Shield:
				return "shields\\" + identifier;
			default:
				return "items\\" + identifier;
		}
	}
	public ItemCategories getCategory() {
		return category;
	}
	public ItemRarities getRarity() {
		return rarity;
	}
	public int getMaxStackSize() {
		return maxStackSize;
	}
	public String getId() {
		return identifier;
	}
	public String getName() {
		return name;
	}
	public boolean isStackable() {
		return maxStackSize > 1;
	}
	public int getBaseDamage() {
		return damage;
	}
	public int getBaseDefense() {
		return defense;
	}
	public HoldOrientations getHoldOrientation() {
		return holdOrientation;
	}
	public int getEnergyHealed() {
		return energyHealed;
	}
	public SpawnableEntities getEntitySpawned() {
		return entitySpawned;
	}
	public int getHealthHealed() {
		return healthHealed;
	}
	public String getActionText() {
		return actionText;
	}
	public ItemAbility getAbility() {
		return ability;
	}
	
	public static Items itemFrom(String id) {
		Items[] possibleValues = Items.class.getEnumConstants();
		for (int i = 0; i < possibleValues.length; i++) {
			if (possibleValues[i].identifier.equals(id)) {
				return possibleValues[i];
			}
		}
		return Items.Wood;
	}
	
}
