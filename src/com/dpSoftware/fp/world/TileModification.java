package com.dpSoftware.fp.world;

import org.json.JSONObject;

public class TileModification {
	
	private int x;
	private int y;
	private boolean brokeDecoration;
	
	public TileModification(int x, int y) {
		this(x, y, false);
	}
	public TileModification(int x, int y, boolean brokeDecoration) {
		this.x = x;
		this.y = y;
		this.brokeDecoration = brokeDecoration;
	}

	public boolean getBrokeDecoration() {
		return brokeDecoration;
	}
	public void setBrokeDecoration(boolean brokeDecoration) {
		this.brokeDecoration = brokeDecoration;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	// Returns true if this is the default state of this class (as in, no modifications have been made to this tile)
	public boolean checkDefault() {
		return !brokeDecoration;
	}
	
	public static TileModification fromJsonObject(JSONObject obj) {
		int x = obj.getInt("x");
		int y = obj.getInt("y");
		boolean brokeDecor = obj.getBoolean("brokeDecoration");
		return new TileModification(x, y, brokeDecor);
	}

}
