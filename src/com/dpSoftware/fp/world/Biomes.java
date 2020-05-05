package com.dpSoftware.fp.world;

import java.awt.Color;

import com.dpSoftware.fp.world.tiles.TileTypes;

public enum Biomes {

	Plains(0, "Plains"),
	Forest(1, "Forest"),
	Desert(2, "Desert"),
	Ocean(3, "Ocean"),
	Snowy(4, "Snowy"),
	Beach(5, "Beach"),
	Shrubland(6, "Shrubland"),
	Tundra(7, "Tundra"),
	Swamp(8, "Swamp"),
	CobaltValley(9, "CobaltValley"),
	FruitGrove(10, "FruitGrove"),
	;
	
	private int id;
	private String name;
	Biomes(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public String toString() {
		return name;
	}
	public int getId() {
		return id;
	}
	
	public static Biomes getBiome(double elevation, double moisture) {
		if (elevation < 0.32) {
			if (moisture > 0.25) {
				if (elevation < 0.3) return Ocean;
				else return Beach;
			} else return CobaltValley;
		}
		if (elevation > 0.7) {
			if (moisture < 0.4) return Tundra;
			else return Snowy;
		} else if (elevation > 0.5) {
			if (moisture < 0.33) return Desert;
			else return Shrubland;
		} else { // elevation < 0.5
			if (moisture < 0.4) return Plains;
			else if (moisture < 0.6) return Forest;
			else if (moisture < 0.7) return FruitGrove;
			else return Swamp;
		}
	}
	
	public static TileTypes getTileType(Biomes biome, double elevation, double rand) {
		switch (biome) {
			case Plains:
				return TileTypes.Grass;
			case Forest:
				return TileTypes.Grass;
			case Desert:
				return TileTypes.Sand;
			case Snowy:
				return TileTypes.Snow;
			case Ocean:
				return TileTypes.Water;
			case Beach:
				return TileTypes.Sand;
			case Shrubland:
				return TileTypes.PatchyGrass;
			case Tundra:
				if (elevation > 0.85) return TileTypes.Snow;
				else return TileTypes.Stone;
			case Swamp:
				if (elevation * rand < 0.1) return TileTypes.Water;
				else return TileTypes.Grass;
			case CobaltValley:
				return TileTypes.Cobalt;
			case FruitGrove:
				return TileTypes.Grass;
		}
		return TileTypes.Grass;
	}
	
}
