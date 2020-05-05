package com.dpSoftware.fp.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.world.World;

public class EntitySet {

	private ArrayList<Entity> entities;
	private double updateRange;

	public EntitySet(double updateRange) {
		this.updateRange = updateRange;
		entities = new ArrayList<>();
	}

	public int size() {
		return entities.size();
	}

	public Entity getEntity(int index) {
		return entities.get(index);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(int index) {
		entities.remove(index);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
//		long start = System.currentTimeMillis();
		
		for (int i = 0; i < entities.size(); i++) {
			// Entities are only updated if they are a certain range from
			// this specified point (which is generally the player's position)
			if (entities.get(i).distanceTo(updateCenter) <= updateRange) {
				entities.get(i).update(passedTime, c, updateCenter);
			}
			if (entities.get(i).distanceTo(updateCenter) >= entities.get(i).getDespawnDistance()
					&& !entities.get(i).isPersistent()) {
				entities.get(i).dispose();
			}
			if (entities.get(i).isDisposed()) {
				removeEntity(i);
				i--;
			}
//			if (System.currentTimeMillis() - start > 5) {
//				System.out.println("Entity Updating Time (" + entities.get(i).getClass().toString() + "): " + (System.currentTimeMillis() - start) + " ms");
//			}
//			start = System.currentTimeMillis();
		}
	}

	public void draw(Graphics2D g, Camera c, int minX, int minY, int maxX, int maxY) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			// Only render entities that are visible
			if (entities.get(i).getDrawX(c) + entities.get(i).getWidth() < minX || entities.get(i).getDrawX(c) > maxX
					|| entities.get(i).getDrawY(c) + entities.get(i).getHeight() < minY
					|| entities.get(i).getDrawY(c) > maxY) {
				continue;
			}
			entities.get(i).draw(g, c);
		}
	}
	public void drawGui(Graphics2D g, Camera c, int minX, int minY, int maxX, int maxY) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			// Only render entities that are visible
			if (entities.get(i).getDrawX(c) + entities.get(i).getWidth() < minX || entities.get(i).getDrawX(c) > maxX
					|| entities.get(i).getDrawY(c) + entities.get(i).getHeight() < minY
					|| entities.get(i).getDrawY(c) > maxY) {
				continue;
			}
			entities.get(i).drawGui(g, c);
		}
	}

}
