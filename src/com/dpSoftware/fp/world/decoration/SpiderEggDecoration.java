package com.dpSoftware.fp.world.decoration;

import java.util.Random;

import com.dpSoftware.fp.entity.EntitySpawner;
import com.dpSoftware.fp.entity.SpawnableEntities;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.world.LootEntry;

public class SpiderEggDecoration extends ImageDecoration {

	private static final String TEXTURE_NAME = "spideregg";
	
	private static final int SPIDERS_MIN = 1;
	private static final int SPIDERS_MAX = 3;
	private Random random;
	
	private static final double HB1_LEFT_RATIO = 36.0 / 128;
	private static final double HB1_TOP_RATIO = 56.0 / 128;
	private static final double HB1_RIGHT_RATIO = 84.0 / 128;
	private static final double HB1_BOTTOM_RATIO = 122.0 / 128;
	
	private EntitySpawner spawner;
	
	public SpiderEggDecoration(int tileSize, int worldX, int worldY, EntitySpawner spawner, Random random) {
		super(tileSize, worldX, worldY, TEXTURE_NAME, Items.Pickaxe, SHORT_BREAK_TIME, true, false);
		
		this.spawner = spawner;
		this.random = random;
		drops.addEntry(new LootEntry(Items.Web, 1, 2, 100));
		
		addHitbox(HB1_LEFT_RATIO, HB1_TOP_RATIO, HB1_RIGHT_RATIO, HB1_BOTTOM_RATIO);
	}

	public void onBreak() {
		spawner.spawnEntity(SpawnableEntities.Spider, (int) getWorldX(), (int) getWorldY(), 
				RandomUtils.randomIntBetween(random, SPIDERS_MIN, SPIDERS_MAX));
	}
	
}
