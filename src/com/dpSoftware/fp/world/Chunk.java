package com.dpSoftware.fp.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.spongepowered.noise.Noise;
import org.spongepowered.noise.NoiseQuality;

import com.dpSoftware.fp.entity.EntitySpawnCondition;
import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.entity.SpawnableEntities;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.ArrayUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.tiles.*;

public class Chunk {

	// Chunks are a square of 16x16 tiles
	public static final int CHUNK_SIZE = 16;
	private Tile[] tiles;
	private ArrayList<Biomes> allChunkBiomes; // Stores all of the biomes of any
												// tile in this chunk; makes
												// certain updating functions
												// faster
	private ArrayList<TileTypes> allChunkTileTypes; // Similar use to the
													// allChunkBiomes ArrayList
	private int chunkX;
	private int chunkY;
	private final int tileSize;
	private final double biomeDistributionFactor;
	// The random object used for events (spawning entities, decoration drops, etc)
	private Random eventRandom;
	// The random object used for world generation
	private Random generationRandom;
	private long chunkSeed; // The seed for this chunk

	private EntitySpawner entitySpawner;
	private long lastTimeSpawned;
	private long lastSpawnAttempt;
	// Stores the current cooldown before attempting another spawn (which is
	// randomly decided after each attempt)
	private int spawnAttemptCooldown;
	// Waits at least 1.5 seconds before attempting another spawn
	private static final int SPAWN_ATTEMPT_COOLDOWN_MIN = 1500;
	// Waits at most 10 seconds before attempting another spawn
	private static final int SPAWN_ATTEMPT_COOLDOWN_MAX = 10000;

	public Chunk(int chunkX, int chunkY, int tileSize, double biomeDistributionFactor, Random rand, EntitySpawner entitySpawner) {
		tiles = new Tile[CHUNK_SIZE * CHUNK_SIZE];
		this.eventRandom = rand;
		this.entitySpawner = entitySpawner;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.tileSize = tileSize;
		this.biomeDistributionFactor = biomeDistributionFactor;

		allChunkBiomes = new ArrayList<>();
		allChunkTileTypes = new ArrayList<>();
	}

	public void update(long timePassed, boolean spawnEntities) {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i].update(timePassed);
		}

		if (spawnEntities) {
			if (System.currentTimeMillis() >= lastSpawnAttempt + spawnAttemptCooldown || lastSpawnAttempt <= 0) {
				// Enough time has passed since the last spawn attempt, so try
				// again
				trySpawning();
			}
		}
	}

	private void trySpawning() {
		lastSpawnAttempt = System.currentTimeMillis();
		spawnAttemptCooldown = RandomUtils.randomIntBetween(eventRandom, SPAWN_ATTEMPT_COOLDOWN_MIN,
				SPAWN_ATTEMPT_COOLDOWN_MAX);
		// Shuffle this so that certain entities don't have a higher chance to
		// spawn just because they appear
		// earlier in the array
		SpawnableEntities[] spawnables = ArrayUtils.shuffle(SpawnableEntities.values(), eventRandom);
		for (int i = 0; i < spawnables.length; i++) {
			EntitySpawnCondition spawnCondition = spawnables[i].getSpawnCondition();
			switch (spawnCondition.getConditionType()) {
				case Biome:
					for (int j = 0; j < spawnCondition.getBiomes().size(); j++) {
						if (allChunkBiomes.contains(spawnCondition.getBiomes().get(j))) {
							// Pick a random tile and try to spawn on this tile
							// If the tile happens to not fit the conditions, oh
							// well, we can try again next update cycle
							Tile tryTile = tiles[RandomUtils.randomIntBetween(eventRandom, 0, tiles.length - 1)];
							if (tryTile.getBiome() == spawnCondition.getBiomes().get(j)
									&& RandomUtils.doesChanceSucceed(eventRandom, spawnCondition.getSpawnChance())) {
								lastTimeSpawned = System.currentTimeMillis();
								int packSize = spawnables[i].getPackSizeMin();
								if (spawnables[i].getPackSizeMin() != spawnables[i].getPackSizeMax()) {
									packSize = RandomUtils.randomIntBetween(eventRandom, spawnables[i].getPackSizeMin(),
											spawnables[i].getPackSizeMax());
								}
								entitySpawner.spawnEntity(spawnables[i], (int) tryTile.getWorldX(),
										(int) tryTile.getWorldY(), packSize);
								// Return once an entity has been spawned so
								// that we don't spawn another one
								return;
							}
						}
					}
					break;
			}
		}
	}
	
	public boolean containsBiome(Biomes biome) {
		return allChunkBiomes.contains(biome);
	}
	public boolean containsAnyBiomes(Biomes[] biomes) {
		return !Collections.disjoint(allChunkBiomes, Arrays.asList(biomes));
	}
	public ArrayList<Biomes> getChunkBiomes() {
		return allChunkBiomes;
	}

	public void draw(Graphics2D g, Camera c, boolean drawBorder, int tileMinX, int tileMinY, int tileMaxX,
			int tileMaxY) {
		for (int i = 0; i < tiles.length; i++) {
			// Some further optimization - only draws the tile if it's within
			// the visible window
			if (tiles[i].getDrawX(c) + tiles[i].getWidth() < tileMinX || tiles[i].getDrawX(c) > tileMaxX
					|| tiles[i].getDrawY(c) + tiles[i].getHeight() < tileMinY || tiles[i].getDrawY(c) > tileMaxY) {
				continue;
			}
			tiles[i].draw(g, c);
		}

		if (drawBorder) {
			int funcChunkX = (int) Math.round(chunkX * tileSize * CHUNK_SIZE - c.getX() * tileSize);
			int funcChunkY = (int) Math.round(chunkY * tileSize * CHUNK_SIZE - c.getY() * tileSize);

			g.setStroke(new BasicStroke(5));
			// Uppermost border
			g.setColor(Color.red);
			g.drawLine(funcChunkX, funcChunkY, funcChunkX + CHUNK_SIZE * tileSize, funcChunkY);
			// Rightmost border
			g.drawLine(funcChunkX, funcChunkY, funcChunkX, funcChunkY + CHUNK_SIZE * tileSize);
			// Leftmost border
			g.drawLine(funcChunkX + CHUNK_SIZE * tileSize, funcChunkY, funcChunkX + CHUNK_SIZE * tileSize,
					funcChunkY + CHUNK_SIZE * tileSize);
			// Bottommost border
			g.drawLine(funcChunkX, funcChunkY + CHUNK_SIZE * tileSize, funcChunkX + CHUNK_SIZE * tileSize,
					funcChunkY + CHUNK_SIZE * tileSize);
		}
	}

	public void drawDecorations(Graphics2D g, Camera c, boolean drawBorder, int tileMinX, int tileMinY, int tileMaxX,
			int tileMaxY) {
		for (int i = 0; i < tiles.length; i++) {
			// Some further optimization - only draws the tile if it's within
			// the visible window
			if (tiles[i].getDrawX(c) + tiles[i].getWidth() < tileMinX || tiles[i].getDrawX(c) > tileMaxX
					|| tiles[i].getDrawY(c) + tiles[i].getHeight() < tileMinY || tiles[i].getDrawY(c) > tileMaxY) {
				continue;
			}
			tiles[i].drawDecoration(g, c);
		}
	}

	public void generate(long elevationSeed, long moistureSeed, long worldSeed, ChunkModification chunkModification) {
		chunkSeed = (long) chunkX * 341873128712L * worldSeed + (long) chunkY * 132897987541L * worldSeed;
		generationRandom = new Random(chunkSeed);

		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_SIZE; j++) {
				int effectiveX = i + chunkX * CHUNK_SIZE;
				int effectiveY = j + chunkY * CHUNK_SIZE;
				double biomePerlinX = effectiveX / biomeDistributionFactor;
				double biomePerlinY = effectiveY / biomeDistributionFactor;

				double variation = generationRandom.nextDouble();
				double elevation = Noise.valueCoherentNoise3D(biomePerlinX, 0.77, biomePerlinY, elevationSeed,
						NoiseQuality.FAST);
				double moisture = Noise.valueCoherentNoise3D(biomePerlinX, 0.77, biomePerlinY, moistureSeed,
						NoiseQuality.FAST);
				Biomes tileBiome = Biomes.getBiome(elevation, moisture);
				TileTypes tileType = Biomes.getTileType(tileBiome, elevation, variation);
				if (!allChunkBiomes.contains(tileBiome)) {
					allChunkBiomes.add(tileBiome);
				}
				if (!allChunkTileTypes.contains(tileType)) {
					allChunkTileTypes.add(tileType);
				}
				tiles[i * CHUNK_SIZE + j] = createTile(tileType, tileBiome, elevation, moisture, variation, effectiveX,
						effectiveY, generationRandom);

				if (chunkModification != null) {
					if (chunkModification.getTileModification(effectiveX, effectiveY).getBrokeDecoration()) {
						// The decoration on this tile has been removed by some
						// process, so remove it after generating
						tiles[i * CHUNK_SIZE + j].removeDecoration();
					}
				}
			}
		}
	}

	public int getX() {
		return chunkX;
	}

	public int getY() {
		return chunkY;
	}

	public Point getChunkPoint() {
		return new Point(chunkX, chunkY);
	}
	
	public Tile getFirstTile() {
		if (tiles != null && tiles.length > 0) {
			return tiles[0];
		}
		return null;
	}

	public Tile getTileAt(int worldX, int worldY) {
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getWorldX() == worldX && tiles[i].getWorldY() == worldY) {
				return tiles[i];
			}
		}
		return null;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	private Tile createTile(TileTypes tileType, Biomes biome, double elevation, double moisture, double variation,
			int worldX, int worldY, Random rand) {
		switch (tileType) {
			case Grass:
				return new GrassTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand,
						entitySpawner);
			case PatchyGrass:
				return new PatchyGrassTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
			case Snow:
				return new SnowTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
			case Sand:
				return new SandTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
			case Water:
				return new WaterTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
			case Stone:
				return new StoneTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
			case Cobalt:
				return new CobaltTile(tileSize, biome, variation, elevation, moisture, worldX, worldY, rand);
		}
		return null;
	}
}
