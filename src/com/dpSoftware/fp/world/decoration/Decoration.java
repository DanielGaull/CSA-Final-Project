package com.dpSoftware.fp.world.decoration;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.world.LootTable;
import com.dpSoftware.fp.world.WorldObject;

public abstract class Decoration extends WorldObject {

	protected final int tileSize;
	private Items tool;
	private long breakTime;
	
	private ArrayList<Rectangle> hitboxes;
	private boolean canCollide;
	private boolean canSlow;
	
	protected static final long LONG_BREAK_TIME = 3000;
	protected static final long MODERATE_BREAK_TIME = 1750;
	protected static final long MED_BREAK_TIME = 750;
	protected static final long SHORT_BREAK_TIME = 250;
	protected static final long INSTANT_BREAK_TIME = 100;
	
	protected LootTable drops;
	
	public Decoration(int tileSize, int worldX, int worldY, Items tool, long breakTime, boolean canCollide, boolean canSlow) {
		super(worldX, worldY, tileSize, tileSize, tileSize);
		this.tileSize = tileSize;
		
		drops = new LootTable();
		
		this.tool = tool;
		this.breakTime = breakTime;
		
		this.canCollide = canCollide;
		this.canSlow = canSlow;
		
		hitboxes = new ArrayList<>();
	}
	
	public void update() {
		
	}
	public abstract void draw(Graphics2D g, Camera c);
	
	// Child classes can override the onBreak method
	public void onBreak() {
		
	}
	
	public void addHitbox(Rectangle rect) {
		hitboxes.add(rect);
	}
	public void addHitbox(double leftRatio, double topRatio, double rightRatio, double bottomRatio) {
		addHitbox(new Rectangle(leftRatio + worldX, topRatio + worldY,
				rightRatio - leftRatio, bottomRatio - topRatio));
	}
	public ArrayList<Rectangle> getHitboxes() {
		return hitboxes;
	}
	public boolean intersects(Rectangle rect) {
		for (int i = 0; i < hitboxes.size(); i++) {
			if (hitboxes.get(i).intersects(rect)) {
				return true;
			}
		}
		return false;
	}
	public boolean intersects(WorldObject wObj) {
		return intersects(wObj.getRect());
	}
	public boolean intersects(Point point) {
		for (int i = 0; i < hitboxes.size(); i++) {
			if (hitboxes.get(i).includes(point)) {
				return true;
			}
		}
		return false;
	}
	public Rectangle getCollidedHitbox(Rectangle rect) {
		for (int i = 0; i < hitboxes.size(); i++) {
			if (hitboxes.get(i).intersects(rect)) {
				return hitboxes.get(i);
			}
		}
		return null;
	}
	public Rectangle getCollidedHitbox(WorldObject wObj) {
		return getCollidedHitbox(wObj.getRect());
	}
	
	public boolean canCollide() {
		return canCollide;
	}
	public boolean canSlow() {
		return canSlow;
	}
	
	public Items getBreakingTool() {
		return tool;
	}
	public long getBreakTime() {
		return breakTime;
	}
	
	public ArrayList<ItemStack> getDrops(Random rand) {
		if (drops != null) return drops.runLootTable(rand);
		else return new ArrayList<ItemStack>();
	}
	
}
