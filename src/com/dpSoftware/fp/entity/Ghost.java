package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class Ghost extends PunchingHumanoidEnemy {

	private static final Color GHOST_COLOR = new Color(237, 209, 255, 200);
	
	private static final int ATTACK_COOLDOWN = 1500;
	private static final double SIGHT_RANGE = 20;
	private static final double ATT_DMG = 17;
	private static final double ATT_RANGE = 1;
	private static final int MAX_HEALTH = 75;
	private static final int DEFENSE = 0;
	
	private static final int COINS_MIN = 10;
	private static final int COINS_MAX = 20;
	private static final int XP_MIN = 15;
	private static final int XP_MAX = 25;
	
	public Ghost(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, GHOST_COLOR, GHOST_COLOR, GHOST_COLOR, random, "Ghost", 
				ATTACK_COOLDOWN, SIGHT_RANGE, ATT_DMG, ATT_RANGE);
		
		setMaxHealth(MAX_HEALTH);
		setBaseDefense(DEFENSE);
		setCanPhase(true);
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
		
		dropTable.addEntry(new LootEntry(Items.Ectoplasm, 0, 2, 100));
	}
	
}
