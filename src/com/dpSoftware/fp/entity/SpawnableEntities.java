package com.dpSoftware.fp.entity;

import com.dpSoftware.fp.world.Biomes;

public enum SpawnableEntities {

	Monkey(new EntitySpawnCondition(2, Biomes.Forest), "Monkey"), // 2% chance to spawn per spawn cycle in forests
	Spider(new EntitySpawnCondition(), "Spider"), // Special spawn condition (when eggs broken)
	BloodTarantulaBoss(new EntitySpawnCondition(), "Blood Tarantula"), // Spawns when item used
	YetiBoss(new EntitySpawnCondition(), "Yeti"), // Spawns when item used
	Wizard(new EntitySpawnCondition(2.5, Biomes.Forest, Biomes.Shrubland), "Wizard"), // 2.5% chance to spawn per spawn cycle in forest/shrubland 
	Bee(new EntitySpawnCondition(), "Bee"), // Custom spawn condition (when tree w/ hive broken)
	Ghost(new EntitySpawnCondition(1.5, Biomes.Shrubland, Biomes.Plains, Biomes.Desert), "Ghost"), // 1.5% chance to spawn per cycle in shrubland/plains/desert
	Skeleton(new EntitySpawnCondition(1.5, Biomes.Desert, Biomes.Shrubland), "Skeleton"), // 1.5% chance to spawn per cycle in desert/shrubland
	Salmon(new EntitySpawnCondition(1.5, Biomes.Ocean), "Salmon", 2, 5), // 1.5% chance per cycle in oceans (packs of 2-5)
	Carp(new EntitySpawnCondition(1.5, Biomes.Ocean), "Carp", 3, 6), // 1.5% chance per cycle in oceans (packs of 3-6)
	;
	
	private EntitySpawnCondition spawnCondition;
	private int packSizeMin;
	private int packSizeMax;
	private String name;
	SpawnableEntities(EntitySpawnCondition spawnCondition, String name) {
		this(spawnCondition, name, 1, 1);
	}
	SpawnableEntities(EntitySpawnCondition spawnCondition, String name, int packSizeMin, int packSizeMax) {
		this.spawnCondition = spawnCondition;
		this.name = name;
		this.packSizeMin = packSizeMin;
		this.packSizeMax = packSizeMax;
	}
	public EntitySpawnCondition getSpawnCondition() {
		return spawnCondition;
	}
	public int getPackSizeMin() {
		return packSizeMin;
	}
	public int getPackSizeMax() {
		return packSizeMax;
	}
	public String getName() {
		return name;
	}
}
