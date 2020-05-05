package com.dpSoftware.fp.world;

import java.util.ArrayList;
import java.util.Objects;

import org.w3c.dom.Node;

import com.dpSoftware.fp.ui.Point;

public class WorldNode {

	private int x;
	private int y;
	
	public WorldNode() {
		x = y = 0;
	}
	public WorldNode(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof WorldNode)) return false;
		
		WorldNode node = (WorldNode) obj;
		return node.x == x && node.y == y;
	}
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public int distanceBetween(WorldNode n2) {
		return Math.abs(x - n2.x) + Math.abs(y - n2.y);
	}
	
	public ArrayList<WorldNode> getNeighbors() {
		ArrayList<WorldNode> neighbors = new ArrayList<>();
		neighbors.add(new WorldNode(x + 1, y));
		neighbors.add(new WorldNode(x - 1, y));
		neighbors.add(new WorldNode(x, y + 1));
		neighbors.add(new WorldNode(x, y - 1));
		return neighbors;
	}
	public boolean isNeighbor(WorldNode node) {
		return node.distanceBetween(this) == 1;
	}
	
	public Point getCenterPoint() {
		return new Point(getPointCenterX(), getPointCenterY());
	}
	public double getPointCenterX() {
		return x + 0.5;
	}
	public double getPointCenterY() {
		return y + 0.5;
	}
	
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
