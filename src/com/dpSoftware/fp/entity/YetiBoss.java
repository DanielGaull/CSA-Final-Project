package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class YetiBoss extends SwingingHumanoidEnemy {

	private static final Color BODY_COLOR = new Color(214, 250, 255);
	private static final Color HAND_COLOR = new Color(147, 204, 211);

	private static final int MAX_HEALTH = 5000;
	private static final int DEFENSE = 750;
	private static final int SIGHT_RANGE = 50;
	private static final double ATTACK_REACH = 2.5;
	private static final double MOVE_SPEED = 1;
	private static final int BASE_DMG = -100;
	private static final int ATTACK_CD = 5000;

	private static final int COINS_MIN = 100;
	private static final int COINS_MAX = 175;
	private static final int XP_MIN = 250;
	private static final int XP_MAX = 300;

	public YetiBoss(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, BODY_COLOR, HAND_COLOR, HAND_COLOR, random, "Yeti", 
				ATTACK_CD, Items.ArcticSword, BASE_DMG, SIGHT_RANGE, ATTACK_REACH);

		setMaxHealth(MAX_HEALTH);
		setIsBoss(true);
		setBossbarColor(BODY_COLOR);
		setBaseDefense(DEFENSE);
		setSpeed(MOVE_SPEED);

		dropTable.addEntry(new LootEntry(Items.YetiIcicle, 1, 1, 100));
		dropTable.addEntry(new LootEntry(Items.Ice, 5, 15, 100));
		dropTable.addEntry(new LootEntry(Items.Snow, 5, 25, 100));

		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
	}

}
