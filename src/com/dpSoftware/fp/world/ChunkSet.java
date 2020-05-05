package com.dpSoftware.fp.world;

import java.util.ArrayList;
import java.util.Random;

import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.ArrayUtils;
import com.dpSoftware.fp.world.tiles.Tile;

public class ChunkSet {

	private ArrayList<Chunk> activeChunks;
	private ChunkModificationHandler chunkModHandler;

	private final double biomeDistributionFactor;
	private final int tileSize;
	private Random random;
	private final long worldSeed;
	private final long elevSeed;
	private final long moistSeed;
	private EntitySpawner entitySpawner;

	public ChunkSet(Random random, final long elevSeed, final long moistSeed, final long worldSeed, final int tileSize,
			final double biomeDistributionFactor, EntitySpawner entitySpawner, ChunkModificationHandler modHandler) {
		this.random = random;
		this.elevSeed = elevSeed;
		this.moistSeed = moistSeed;
		this.worldSeed = worldSeed;

		this.tileSize = tileSize;
		this.biomeDistributionFactor = biomeDistributionFactor;

		this.entitySpawner = entitySpawner;
		this.chunkModHandler = modHandler;

		activeChunks = new ArrayList<>();
	}

	public void updateChunkLoading(ArrayList<Point> chunkLocs) {
		ArrayList<Point> chunksToLoad = ArrayUtils.clone(chunkLocs);

		// We first unload any chunks that don't belong
		for (int i = 0; i < activeChunks.size(); i++) {
			if (!ArrayUtils.contains(chunksToLoad, activeChunks.get(i).getChunkPoint())) {
				activeChunks.remove(i);
				i--;
			} else {
				// We've already loaded in this chunk, so remove it from the
				// list of chunks that still need to be added
				chunksToLoad.remove(chunksToLoad.indexOf(activeChunks.get(i).getChunkPoint()));
			}
		}

		// Now load in the chunks that need to be added
		for (int i = 0; i < chunksToLoad.size(); i++) {
			int chunkX = (int) chunksToLoad.get(i).getX();
			int chunkY = (int) chunksToLoad.get(i).getY();
			activeChunks.add(generateChunk(chunkX, chunkY));
		}
	}

	public void updateChunks(long timePassed, boolean spawnEntities) {
		for (int i = 0; i < activeChunks.size(); i++) {
			activeChunks.get(i).update(timePassed, spawnEntities);
		}
	}

	// Used to force-reload all the chunks
	public void clear() {
		activeChunks.clear();
	}

	public int size() {
		return activeChunks.size();
	}

	public Chunk getChunk(int index) {
		return activeChunks.get(index);
	}

	private Chunk getChunk(Point point) {
		for (int i = 0; i < activeChunks.size(); i++) {
			if (activeChunks.get(i).getX() == point.getX() && activeChunks.get(i).getY() == point.getY()) {
				return activeChunks.get(i);
			}
		}
		return null;
	}

	public Chunk getChunkAtTile(double tileX, double tileY) {
		Point chunkLoc = getChunkLocForTile(tileX, tileY);
		for (int i = 0; i < activeChunks.size(); i++) {
			if (activeChunks.get(i).getChunkPoint().equals(chunkLoc)) {
				return activeChunks.get(i);
			}
		}
		// This chunk has not been loaded, so let's quickly load it and return it
		return generateChunk((int) chunkLoc.getX(), (int) chunkLoc.getY());
	}
	public Chunk getChunkAtTileNoGenerate(double tileX, double tileY) {
		Point chunkLoc = getChunkLocForTile(tileX, tileY);
		for (int i = 0; i < activeChunks.size(); i++) {
			if (activeChunks.get(i).getChunkPoint().equals(chunkLoc)) {
				return activeChunks.get(i);
			}
		}
		// This chunk has not been loaded, so return null
		return null;
	}

	private Point getChunkLocForTile(double tileX, double tileY) {
		// Need to figure out which chunk this would fall into
		int chunkX = (int) tileX / Chunk.CHUNK_SIZE;
		int chunkY = (int) tileY / Chunk.CHUNK_SIZE;
		// With negative numbers, for example, the point (-3, -3) returns
		// chunk at (0, 0) when it should be chunk at (-1, -1), so this corrects that
		// However, this doesn't occur at multiples of 16, so only make the correction for
		// non-multiples of 16 (i.e. the chunk size)
		if (tileX < 0 && tileX % Chunk.CHUNK_SIZE != 0)
			chunkX--;
		if (tileY < 0 && tileY % Chunk.CHUNK_SIZE != 0)
			chunkY--;
		return new Point(chunkX, chunkY);
	}

	private Chunk generateChunk(int chunkX, int chunkY) {
		Chunk newChunk = new Chunk(chunkX, chunkY, tileSize, biomeDistributionFactor, random, entitySpawner);
		newChunk.generate(elevSeed, moistSeed, worldSeed, chunkModHandler.getModification(chunkX, chunkY));
		return newChunk;
	}

	public Tile getTileAt(double worldX, double worldY) {
		return getChunkAtTile((int) worldX, (int) worldY).getTileAt((int) worldX, (int) worldY);
	}
	public Tile getTileAtNoGenerate(double worldX, double worldY) {
		Chunk chunk = getChunkAtTileNoGenerate((int) worldX, (int) worldY);
		if (chunk != null) {
			return chunk.getTileAt((int) worldX, (int) worldY);
		}
		return null;
	}


	public boolean isChunkGeneratedAt(double tileX, double tileY) {
		Point chunkLoc = getChunkLocForTile(tileX, tileY);
		for (int i = 0; i < activeChunks.size(); i++) {
			if (activeChunks.get(i).getChunkPoint().equals(chunkLoc)) {
				return true;
			}
		}
		// This chunk has not been loaded yet
		return false;
	}
}
