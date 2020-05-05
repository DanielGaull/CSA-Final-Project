package com.dpSoftware.fp.entity;

import java.util.ArrayList;
import java.util.Arrays;

import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.tiles.TileTypes;

public class EntitySpawnCondition {

	private SpawnConditionTypes conditionType;
	private ArrayList<Biomes> biomes;
	private ArrayList<TileTypes> tiles;
	// Structure spawning is only for entities that continue to spawn after a structure's generation
	private double spawnChance;
	
	public EntitySpawnCondition() {
		conditionType = SpawnConditionTypes.Special;
	}
	public EntitySpawnCondition(double chance, Biomes... biomes) {
		conditionType = SpawnConditionTypes.Biome;
		this.biomes = new ArrayList<Biomes>(Arrays.asList(biomes));
		this.spawnChance = chance;
	}
	public EntitySpawnCondition(double chance, TileTypes... tiles) {
		conditionType = SpawnConditionTypes.Tile;
		this.tiles = new ArrayList<TileTypes>(Arrays.asList(tiles));
		this.spawnChance = chance;
	}
	
	public SpawnConditionTypes getConditionType() {
		return conditionType;
	}
	public ArrayList<Biomes> getBiomes() {
		return biomes;
	}
	public ArrayList<TileTypes> getTiles() {
		return tiles;
	}
	public double getSpawnChance() {
		return spawnChance;
	}

	public enum SpawnConditionTypes {
		Biome, // Spawns only in specific biomes
		Structure, // Spawns only in specific structures
		Tile, // Spawns only on specific tiles
		Special // Essentially means, let something else handle it - Chunks should not be attempting to spawn this entity
	}
}
