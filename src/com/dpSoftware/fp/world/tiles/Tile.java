package com.dpSoftware.fp.world.tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.world.Biomes;
import com.dpSoftware.fp.world.WorldObject;
import com.dpSoftware.fp.world.decoration.Decoration;

public abstract class Tile extends WorldObject {

	private Color color;
	private Biomes biome;
	private TileTypes tileType;
	private double elevation;
	private double moisture;
	protected Decoration decoration;
	
	public Tile(int size, TileTypes tileType, Biomes biome, double variation, 
			double elevation, double moisture, int worldX, int worldY) {
		super(worldX, worldY, size, size, size);
		this.tileType = tileType;
		this.biome = biome;
		this.elevation = elevation;
		this.moisture = moisture;
		
		color = Color.black;
		decoration = null;
	}
	
	public void update(long timePassed) {
		if (decoration != null) {
			decoration.update();
		}
	}
	public void draw(Graphics2D g, Camera c) {
		g.setColor(color);
		int size = getWidth();
		int functionalX = getDrawX(c);
		int functionalY = getDrawY(c);
		g.fillRect(functionalX, functionalY, size, size);
	}	
	public void drawDecoration(Graphics2D g, Camera c) {
		if (decoration != null) {
			decoration.draw(g, c);
		}
	}
	
	public Biomes getBiome() {
		return biome;
	}
	public TileTypes getTileType() {
		return tileType;
	}
	
	public Color getColor() {
		return color;
	}
	protected void setColor(Color color) {
		this.color = color;
	}
	
	protected Color varyColor(Color baseColor, double variation) {
		int r = baseColor.getRed();
		int g = baseColor.getGreen();
		int b = baseColor.getBlue();
		int reduceAmount = (int) (variation * 25);
		if (r - reduceAmount > 0) {
			r -= reduceAmount;
		}
		if (g - reduceAmount > 0) {
			g -= reduceAmount;
		}
		if (b - reduceAmount > 0) {
			b -= reduceAmount;
		}
		return new Color(r, g, b);
	}
	
	public Decoration getDecoration() {
		return decoration;
	}
	public void removeDecoration() {
		decoration = null;
	}
}
