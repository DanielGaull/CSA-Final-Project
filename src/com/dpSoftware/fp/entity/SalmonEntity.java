package com.dpSoftware.fp.entity;

import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class SalmonEntity extends FishEntity {

	private static final String TEXTURE_PATH = "entity\\fish\\salmon";
	
	public SalmonEntity(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, random, "Salmon", TEXTURE_PATH);
		
		dropTable.addEntry(new LootEntry(Items.Salmon, 1, 1, 100));
	}

}
