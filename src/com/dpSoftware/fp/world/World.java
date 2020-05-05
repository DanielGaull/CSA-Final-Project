package com.dpSoftware.fp.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import com.dpSoftware.fp.entity.*;
import com.dpSoftware.fp.entity.projectile.*;
import com.dpSoftware.fp.items.*;
import com.dpSoftware.fp.util.*;
import com.dpSoftware.fp.world.decoration.*;
import com.dpSoftware.fp.world.tiles.*;
import com.dpSoftware.fp.ui.BossBar;
import com.dpSoftware.fp.ui.Point;

public class World implements EntitySpawner {

	private ChunkSet chunks;
	private ChunkModificationHandler chunkModHandler;
	private EntitySet entities;
	private long seed;
	private long elevSeed;
	private long moistureSeed;
	private String name;
	private Random generationRandom; // Used for world generation
	private Random eventRandom; // Used for events (entity spawning, decoration drops, etc)
	private boolean hasEntities;
	private boolean spawnEntities;
	private Point spawnPoint;
	
	public static final Biomes[] SUITABLE_SPAWN_BIOMES = { Biomes.Beach, Biomes.Plains, Biomes.Desert, Biomes.Forest, 
			Biomes.Shrubland, Biomes.Snowy, Biomes.Swamp, Biomes.Tundra };
	private static final int FINDING_SPAWN_JUMP_VALUE = 5;

	public static final int TILE_SIZE = 75;
	private static final int CHUNK_LOAD_RADIUS = 3; // Load chunks up to 3 away from the camera
	public static final int ITEM_SIZE = 32;
	private static final int PROJ_SIZE = 20;
	private static final double ENTITY_UPDATE_RANGE = 30;
	private static final int CREATURE_SIZE = 35;

	private static final int SPIDER_SIZE_MIN = 35;
	private static final int SPIDER_SIZE_MAX = 45;
	private static final int TARANTULA_SIZE = 100;
	private static final int YETI_SIZE = 100;

	private ArrayList<BossBar> bossBars;
	private static final Font BOSSBAR_FONT = new Font("Arial", Font.BOLD, 25);
	private static final double BOSSBAR_WIDTH_RATIO = 0.85;
	private static final double BOSSBAR_HEIGHT_RATIO = 0.05;
	private static final int BOSSBAR_SPACING = 10;
	private final int bossbarX;
	private final int bossbarWidth;
	private final int bossbarHeight;

	private static final double BIOME_DISTRIBUTION_FACTOR = 307;

	private final int windowWidth;
	private final int windowHeight;

	private boolean drawChunkBorders;

	public World(int windowWidth, int windowHeight, long seed, String name) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		this.name = name;

		drawChunkBorders = false;

		bossbarWidth = (int) (BOSSBAR_WIDTH_RATIO * windowWidth);
		bossbarHeight = (int) (BOSSBAR_HEIGHT_RATIO * windowWidth);
		bossbarX = windowWidth / 2 - bossbarWidth / 2;

		bossBars = new ArrayList<>();

		chunkModHandler = new ChunkModificationHandler();

		this.seed = seed;
		generationRandom = new Random(seed);
		eventRandom = new Random();
		long elevSeed = generationRandom.nextLong();
		long moistSeed = generationRandom.nextLong();
		this.elevSeed = elevSeed;
		this.moistureSeed = moistSeed;
		chunks = new ChunkSet(eventRandom, elevSeed, moistSeed, seed, TILE_SIZE, BIOME_DISTRIBUTION_FACTOR, this, chunkModHandler);
		
		hasEntities = true; // By default, the world has entities
		spawnEntities = true;

		entities = new EntitySet(ENTITY_UPDATE_RANGE);
	}

	public void update(Camera c, long passedTime, Point entityUpdateCenter) {
		// Need to load/unload chunks as fit
		updateChunkLoading(c.getX(), c.getY(), windowWidth, windowHeight);
		chunks.updateChunks(passedTime, hasEntities);
		if (hasEntities) {
			entities.update(passedTime, c, entityUpdateCenter);
		}
		for (int i = 0; i < bossBars.size(); i++) {
			bossBars.get(i).update();
		}
	}

	public void draw(Graphics2D g, Camera c) {
		for (int i = 0; i < chunks.size(); i++) {
			chunks.getChunk(i).draw(g, c, drawChunkBorders, 0, 0, windowWidth, windowHeight);
		}

		if (hasEntities) {
			entities.draw(g, c, 0, 0, windowWidth, windowHeight);
		}

		// Draw after entities so that entities display below decorations, but
		// above the tile ("ground") itself
		for (int i = 0; i < chunks.size(); i++) {
			chunks.getChunk(i).drawDecorations(g, c, drawChunkBorders, 0, 0, windowWidth, windowHeight);
		}
		
		if (hasEntities) {
			entities.drawGui(g, c, 0, 0, windowWidth, windowHeight);
		}

		for (int i = 0; i < bossBars.size(); i++) {
			bossBars.get(i).draw(g);
			if (bossBars.get(i).getTrackingEntity().isDisposed()) {
				bossBars.remove(i);
				i--;
			}
		}
	}
	
	public ArrayList<ChunkModification> getChunkMods() {
		return chunkModHandler.getModifications();
	}
	public void setChunkMods(ArrayList<ChunkModification> modifications) {
		for (int i = 0; i < modifications.size(); i++) {
			chunkModHandler.addModification(modifications.get(i));
		}
		chunks.clear();
	}
	
	public void setHasEntities(boolean value) {
		hasEntities = value;
	}
	public boolean hasEntities() {
		return hasEntities;
	}
	public void setSpawnEntities(boolean value) {
		spawnEntities = value;
	}
	public boolean canSpawnEntities() {
		return spawnEntities; 
	}

	public void updateChunkLoading(double x, double y, int winWidth, int winHeight) {
		// To do this, we calculate which chunks SHOULD be displayed
		ArrayList<Point> chunkLocations = new ArrayList<Point>();
		// Calculate what chunk the camera is in
		int cx = (int) (x / Chunk.CHUNK_SIZE);
		int cy = (int) (y / Chunk.CHUNK_SIZE);
		int leftX = cx - CHUNK_LOAD_RADIUS;
		int upperY = cy - CHUNK_LOAD_RADIUS;
		int rightX = cx + CHUNK_LOAD_RADIUS;
		int lowerY = cy + CHUNK_LOAD_RADIUS;

		for (int i = leftX; i < rightX; i++) {
			for (int j = upperY; j < lowerY; j++) {
				chunkLocations.add(new Point(i, j));
			}
		}

		// Now time to check which chunks are loaded and which ones aren't, so
		// we can properly load the
		// chunks we need and unload the ones we don't
		chunks.updateChunkLoading(chunkLocations);
	}

	public long getSeed() {
		return seed;
	}
	public String getName() {
		return name;
	}

	public void addEntity(Entity e) {
		entities.addEntity(e);
	}

	public Tile getTileAt(double x, double y) {
		return chunks.getTileAt(x, y);
	}

	private Chunk getChunkForTile(int tileX, int tileY) {
		return chunks.getChunkAtTile(tileX, tileY);
	}
	
	public Point findSuitableSpawnpoint(Point start) {
		// Determine a move direction, and go out from (0, 0) until a suitable biome for spawning is found
		// We generate a completely fresh random from the world seed here (we want this to always return the same
		// result for the same world)
		Directions moveDirection = Directions.random(new Random(seed));
		Point chunkLocation = new Point(start.getX(), start.getY());
		Chunk currentChunk = new Chunk((int) chunkLocation.getX(), (int) chunkLocation.getY(), TILE_SIZE, BIOME_DISTRIBUTION_FACTOR,
				generationRandom, this);
		currentChunk.generate(elevSeed, moistureSeed, seed, new ChunkModification((int) chunkLocation.getX(), (int) chunkLocation.getY()));
		while (!currentChunk.containsAnyBiomes(SUITABLE_SPAWN_BIOMES)) {
			switch (moveDirection) {
				case Up:
					chunkLocation.setY(chunkLocation.getY() - FINDING_SPAWN_JUMP_VALUE);
					break;
				case Down:
					chunkLocation.setY(chunkLocation.getY() + FINDING_SPAWN_JUMP_VALUE);
					break;
				case Left:
					chunkLocation.setX(chunkLocation.getY() - FINDING_SPAWN_JUMP_VALUE);
					break;
				case Right:
					chunkLocation.setX(chunkLocation.getY() + FINDING_SPAWN_JUMP_VALUE);
					break;
			}
			currentChunk = new Chunk((int) chunkLocation.getX(), (int) chunkLocation.getY(), TILE_SIZE, BIOME_DISTRIBUTION_FACTOR,
					generationRandom, this);
			currentChunk.generate(elevSeed, moistureSeed, seed, new ChunkModification((int) chunkLocation.getX(), (int) chunkLocation.getY()));
		}
		// Now get a tile that doesn't have a collideable decoration
		Tile[] tiles = currentChunk.getTiles();
		// Default to 0; if for some strange reason every tile has a collideable decoration, then just
		// return the first one and that's the downside to this approach
		// But, this would mean 256 random tiles have a collideable decoration, so this should be okay
		int validTileIndex = 0;
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getDecoration() == null || 
					(tiles[i].getDecoration() != null && !tiles[i].getDecoration().canCollide())) {
				validTileIndex = i;
				break;
			}
		}
		return tiles[validTileIndex].getPoint();
	}
	public Point getWorldSpawn() {
		return spawnPoint;
	}
	public void setWorldSpawn(Point point) {
		this.spawnPoint = point;
	}

	public static Point screenCoordToWorldCoord(Point screenLoc, Camera c) {
		double x = 0;
		double y = 0;
		x = screenLoc.getX() / TILE_SIZE + c.getX();
		y = screenLoc.getY() / TILE_SIZE + c.getY();
		return new Point(x, y);
	}

	public Decoration getDecorationAt(double x, double y) {
		Tile tile = getTileAt((int) Math.floor(x), (int) Math.floor(y));
		if (tile != null) {
			return tile.getDecoration();
		}
		return null;
	}

	public ArrayList<Decoration> getDecorations() {
		ArrayList<Decoration> allDecorations = new ArrayList<>();
		for (int i = 0; i < chunks.size(); i++) {
			for (int j = 0; j < chunks.getChunk(i).getTiles().length; j++) {
				if (chunks.getChunk(i).getTiles()[j].getDecoration() != null) {
					allDecorations.add(chunks.getChunk(i).getTiles()[j].getDecoration());
				}
			}
		}
		return allDecorations;
	}

	public ArrayList<Decoration> getDecorations(Point center, double radius) {
		ArrayList<Decoration> allDecorations = new ArrayList<>();
		double centerChunkX = center.getX() / Chunk.CHUNK_SIZE;
		double centerChunkY = center.getY() / Chunk.CHUNK_SIZE;
		for (int i = 0; i < chunks.size(); i++) {
			// Only even try chunks that are close to the center
			if (Math.abs(centerChunkX - chunks.getChunk(i).getX()) > 1
					|| Math.abs(centerChunkY - chunks.getChunk(i).getY()) > 1) {
				continue;
			}
			for (int j = 0; j < chunks.getChunk(i).getTiles().length; j++) {
				if (chunks.getChunk(i).getTiles()[j].getPoint().distanceTo(center) <= radius
						&& chunks.getChunk(i).getTiles()[j].getDecoration() != null) {
					allDecorations.add(chunks.getChunk(i).getTiles()[j].getDecoration());
				}
			}
		}
		return allDecorations;
	}
	
	public Tile[] getNearbyTiles(Point center) {
		return getNearbyTiles(center, 1);
	}
	public Tile[] getNearbyTiles(Point center, int radius) {
		int diameter = radius * 2 + 1;
		Tile[] tiles = new Tile[diameter * diameter];
		Tile centerTile = getTileAt(center.getX(), center.getY());
		int startX = (int) centerTile.getWorldX() - radius;
		int startY = (int) centerTile.getWorldY() - radius;
		for (int i = 0; i < diameter; i++) {
			for (int j = 0; j < diameter; j++) {
				tiles[i * diameter + j] = getTileAt(startX + i, startY + j);
			}
		}
		return tiles;
	}
	// Gets nearby tiles without generating non-existing tiles
	public Tile[] getNearbyTilesNoGenerate(Point center, int radius) {
		int diameter = radius * 2 + 1;
		Tile[] tiles = new Tile[diameter * diameter];
		Tile centerTile = getTileAt(center.getX(), center.getY());
		int startX = (int) centerTile.getWorldX() - radius;
		int startY = (int) centerTile.getWorldY() - radius;
		for (int i = 0; i < diameter; i++) {
			for (int j = 0; j < diameter; j++) {
				tiles[i * diameter + j] = chunks.getTileAtNoGenerate(startX + i, startY + j);
			}
		}
		return tiles;
	}
	
	public boolean inGeneratedChunk(Point point) {
		return chunks.isChunkGeneratedAt(point.getX(), point.getY());
	}

	public void attemptBreak(Creature creature, Point breakLoc) {
		int tileX = (int) Math.floor(breakLoc.getX());
		int tileY = (int) Math.floor(breakLoc.getY());
		Tile tile = getTileAt(tileX, tileY);
		Decoration decor = tile.getDecoration();
		if (decor != null
				&& creature.distanceTo(decor.getCenterX(), decor.getCenterY()) <= creature.getInteractDistance()
				&& creature.getHeldItem().getItem() == decor.getBreakingTool()) {
			decor.onBreak();
			ArrayList<ItemStack> drops = decor.getDrops(eventRandom);
			for (int i = 0; i < drops.size(); i++) {
				double itemX = RandomUtils.randomDoubleBetween(eventRandom, tileX,
						tileX + 1 - (ITEM_SIZE * 1.0 / TILE_SIZE));
				double itemY = RandomUtils.randomDoubleBetween(eventRandom, tileY,
						tileY + 1 - (ITEM_SIZE * 1.0 / TILE_SIZE));
				entities.addEntity(new ItemEntity(itemX, itemY, ITEM_SIZE, TILE_SIZE, drops.get(i)));
			}
			tile.removeDecoration();
			// Now log the chunk modification
			logDecorationBreak(tileX, tileY);
		}
	}

	public void attemptGrab(PlayerEntity player, Point grabLoc) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.getEntity(i).intersects(grabLoc.getX(), grabLoc.getY())
					&& player.distanceTo(entities.getEntity(i).getCenter()) <= player.getPickupDistance()) {
				if (entities.getEntity(i) instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) entities.getEntity(i);
					player.pickupItemStack(item.getItemStack());
					entities.removeEntity(i);
					return;
				} else if (entities.getEntity(i) instanceof CoinEntity) {
					CoinEntity coin = (CoinEntity) entities.getEntity(i);
					player.addCoins(coin.getAmount());
					entities.removeEntity(i);
					return;
				}
			}
		}
	}

	public void attemptDrop(Creature creature, ItemStack itemStack) {
		addEntityDrop(creature, new ItemEntity(0, 0, ITEM_SIZE, TILE_SIZE, itemStack));
	}
	private void addEntityDrop(Creature creature, Entity entity) {
		// Drops can appear in the area that the creature's body covered
		double creatureWorldWidth = creature.getWidth() * 1.0 / TILE_SIZE;
		double creatureWorldHeight = creature.getHeight() * 1.0 / TILE_SIZE;
		double itemWorldSize = ITEM_SIZE * 1.0 / TILE_SIZE;
		double minX = creature.getWorldX();
		double minY = creature.getWorldY();
		double maxX = creature.getWorldX() + creatureWorldWidth - itemWorldSize;
		double maxY = creature.getWorldY() + creatureWorldHeight - itemWorldSize;
		
		double x = RandomUtils.randomDoubleBetween(eventRandom, minX, maxX);
		double y = RandomUtils.randomDoubleBetween(eventRandom, minY, maxY);
		entity.setWorldX(x);
		entity.setWorldY(y);
		entities.addEntity(entity);
	}
	public void onDeath(Creature creature, DamageCause deathCause) {
		ArrayList<ItemStack> drops = creature.getDrops();
		for (int i = 0; i < drops.size(); i++) {
			attemptDrop(creature, drops.get(i));
		}
		
		int coins = creature.getCoinDrops();
		if (coins > 0) {
			addEntityDrop(creature, new CoinEntity(0, 0, ITEM_SIZE, TILE_SIZE, coins));
		}
		
		if (deathCause.getCause() == DamageCauses.Entity) {
			// See if the killer was a player, so we can possibly reward them with XP
			if (deathCause.getCausingEntity() instanceof PlayerEntity) {
				PlayerEntity killer = (PlayerEntity) deathCause.getCausingEntity();
				int xp = creature.getXpDrop();
				if (xp > 0) {
					killer.addXp(xp);
				}
			}
		}
	}

	public void attemptLaunch(Creature creature, Projectiles projectile, Point target) {
		Projectile newProj = null;
		switch (projectile) {
			case Fireball:
				newProj = new FireballProjectile(creature.getCenterX(), creature.getCenterY(), PROJ_SIZE, TILE_SIZE);
				break;
			case WebShot:
				newProj = new WebShotProjectile(creature.getCenterX(), creature.getCenterY(), PROJ_SIZE, TILE_SIZE);
				break;
		}
		if (newProj != null) {
			newProj.launch(target, creature);
			entities.addEntity(newProj);
		}
	}

	public void attemptAttack(Creature attacker, Point target) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.getEntity(i) instanceof Creature
					&& entities.getEntity(i).intersects(target.getX(), target.getY())
					&& attacker.canAttack((Creature) entities.getEntity(i))
					&& attacker.distanceTo(entities.getEntity(i).getCenter()) <= attacker.getAttackReach()) {
				Creature defender = (Creature) entities.getEntity(i);
				defender.damage(attacker.getDamage(), new DamageCause(DamageCauses.Entity, attacker));
				return; // Can only attack one enemy at a time
			}
		}
	}

	public EntitySet getEntities() {
		return entities;
	}

	public void spawnEntity(SpawnableEntities entity, double x, double y, int packSize) {
		if (!spawnEntities) return;
		
		for (int i = 0; i < packSize; i++) {
			Creature entityToSpawn = null;
			// Randomly offset the entity from the original spawn point (if
			// there is more than one being spawned)
			double spX = x;
			double spY = y;
			if (packSize > 1) {
				spX = x + RandomUtils.randomDoubleBetween(eventRandom, -1.5, 1.5);
				spY = y + RandomUtils.randomDoubleBetween(eventRandom, -1.5, 1.5);
			}
			switch (entity) {
				case Monkey:
					entityToSpawn = new Monkey(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Spider:
					entityToSpawn = new Spider(spX, spY, RandomUtils.randomIntBetween(eventRandom, SPIDER_SIZE_MIN, SPIDER_SIZE_MAX), TILE_SIZE,
							this, eventRandom);
					break;
				case BloodTarantulaBoss:
					entityToSpawn = new TarantulaBoss(spX, spY, TARANTULA_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case YetiBoss:
					entityToSpawn = new YetiBoss(spX, spY, YETI_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Wizard:
					entityToSpawn = new Wizard(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Bee:
					entityToSpawn = new Bee(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Ghost:
					entityToSpawn = new Ghost(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Skeleton:
					entityToSpawn = new Skeleton(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Salmon:
					entityToSpawn = new SalmonEntity(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
				case Carp:
					entityToSpawn = new CarpEntity(spX, spY, CREATURE_SIZE, TILE_SIZE, this, eventRandom);
					break;
			}
			//System.out.println("Spawned " + entityToSpawn.getName() + " X: " + spX + " Y: " + spY);
			entities.addEntity(entityToSpawn);
			if (entityToSpawn.isBoss()) {
				BossBar bossBar = new BossBar(bossbarX,
						windowHeight - (bossbarHeight + BOSSBAR_SPACING) * (bossBars.size() + 1), bossbarWidth,
						bossbarHeight, entityToSpawn.getBossbarColor(), entityToSpawn, BOSSBAR_FONT);
				bossBars.add(bossBar);
			}
		}
	}

	private void logDecorationBreak(int tileX, int tileY) {
		// The chunk in which the break occurred
		Chunk chunk = chunks.getChunkAtTile(tileX, tileY);
		int chunkX = chunk.getX();
		int chunkY = chunk.getY();
		if (chunkModHandler.hadModification(chunkX, chunkY)) {
			ChunkModification newMod = chunkModHandler.getModification(chunkX, chunkY);
			newMod.setTileModification(tileX, tileY, new TileModification(tileX, tileY, true));
		} else {
			ChunkModification newMod = new ChunkModification(chunkX, chunkY);
			newMod.setTileModification(tileX, tileY, new TileModification(tileX, tileY, true));
			chunkModHandler.addModification(newMod);
		}
	}
}
