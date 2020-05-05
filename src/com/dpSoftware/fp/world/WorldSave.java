package com.dpSoftware.fp.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.*;

import com.dpSoftware.fp.entity.PlayerData;
import com.dpSoftware.fp.items.Inventory;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.FileUtils;

public class WorldSave {
	
	private String name;
	private static final String DIRECTORY = "save\\worlds\\";
	private static final String FILE_EXTENSION = ".dpworld";
	
	private PlayerData playerData;
	private Point worldSpawn;
	
	private long seed;
	private ArrayList<ChunkModification> chunkModifications;
	
	public WorldSave(long seed, String name, Point worldSpawn) {
		this.seed = seed;
		this.name = name;
		this.worldSpawn = worldSpawn;
		this.chunkModifications = new ArrayList<>();
		playerData = PlayerData.createDefault();
	}
	public WorldSave(long seed, String name, ArrayList<ChunkModification> chunkModifications, PlayerData playerData, Point worldSpawn) {
		this.seed = seed;
		this.name = name;
		this.chunkModifications = chunkModifications;
		this.playerData = playerData;
		this.worldSpawn = worldSpawn;
	}
	
	public String getFileLocation() {
		return getFileLocationFor(name);
	}
	
	public static String getFileLocationFor(String name) {
		return DIRECTORY + name + FILE_EXTENSION;
	}
	
	public String getName() {
		return name;
	}
	public long getSeed() {
		return seed;
	}
	public ArrayList<ChunkModification> getChunkModifications() {
		return chunkModifications;
	}
	public PlayerData getPlayerData() {
		return playerData;
	}
	public Point getWorldSpawn() {
		return worldSpawn;
	}
	
	public static WorldSave fromFile(File file) {
		try {
			JSONTokener tokener = new JSONTokener(new FileReader(file));
			JSONObject worldObj = new JSONObject(tokener);
			long seed = worldObj.getLong("seed");
			String name = worldObj.getString("name");
			JSONArray chunkModsArray = worldObj.getJSONArray("chunkModifications");
			PlayerData playerData = PlayerData.fromJsonObject(worldObj.getJSONObject("playerData"));
			Point worldSpawn = new Point(0, 0);
			// Checking to see if the file contains a certain key will be important for loading legacy worlds in newer versions
			// This also allows a default value if the property has not been set
			if (worldObj.has("worldSpawn")) {
				worldSpawn = Point.fromJsonObject(worldObj.getJSONObject("worldSpawn"));
			}
			ArrayList<ChunkModification> chunkMods = new ArrayList<>();
			for (int i = 0; i < chunkModsArray.length(); i++) {
				chunkMods.add(ChunkModification.fromJsonObj(chunkModsArray.getJSONObject(i)));
			}
			return new WorldSave(seed, name, chunkMods, playerData, worldSpawn);
			
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	public static WorldSave fromFile(String worldName) {
		return fromFile(new File(DIRECTORY + worldName + FILE_EXTENSION));
	}
	
	public void saveToFile() {
		String filePath = getFileLocation();
		JSONObject worldJsonObj = new JSONObject(this);
		FileUtils.writeToFile(worldJsonObj.toString(), filePath);
	}
	
}
