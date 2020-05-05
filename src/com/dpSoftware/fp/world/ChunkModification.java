package com.dpSoftware.fp.world;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChunkModification {

	private TileModification[] tileModifications;
	private int chunkX;
	private int chunkY;
	
	public ChunkModification(int chunkX, int chunkY) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		tileModifications = new TileModification[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		
		for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
			for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
				int effX = i + chunkX * Chunk.CHUNK_SIZE;
				int effY = j + chunkY * Chunk.CHUNK_SIZE;
				tileModifications[i * Chunk.CHUNK_SIZE + j] = new TileModification(effX, effY);
			}			
		}
	}
	public ChunkModification(int chunkX, int chunkY, TileModification[] tileModifications) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.tileModifications = tileModifications;
	}
	
	public TileModification getTileModification(int index) {
		return tileModifications[index];
	}
	public TileModification getTileModification(int x, int y) {
		for (int i = 0; i < tileModifications.length; i++) {
			if (tileModifications[i].getX() == x && 
					tileModifications[i].getY() == y) {
				return tileModifications[i];
			}
		}
		return null;
	}
	public void setTileModification(int index, TileModification value) {
		tileModifications[index] = value;
	}
	public void setTileModification(int x, int y, TileModification value) {
		for (int i = 0; i < tileModifications.length; i++) {
			if (tileModifications[i].getX() == x && 
					tileModifications[i].getY() == y) {
				tileModifications[i] = value;
			}
		}
	}
	
	public int getX() {
		return chunkX;
	}
	public int getY() {
		return chunkY;
	}
	public TileModification[] getTileModifications() {
		return tileModifications;
	}
	
	// Returns true if no modifications have been made to this chunk
	public boolean checkDefault() {
		for (int i = 0; i < tileModifications.length; i++) {
			if (!tileModifications[i].checkDefault()) {
				return false;
			}
		}
		return true;
	}
	
	public static ChunkModification fromJsonObj(JSONObject obj) {
		int chunkX = obj.getInt("x");
		int chunkY = obj.getInt("y");
		TileModification[] tileModifications = new TileModification[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		JSONArray modsArray = obj.getJSONArray("tileModifications");
		for (int i = 0; i < tileModifications.length; i++) {
			tileModifications[i] = TileModification.fromJsonObject(modsArray.getJSONObject(i));
		}
		return new ChunkModification(chunkX, chunkY, tileModifications);
	}
}
