package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.entity.projectile.Projectiles;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.LootEntry;
import com.dpSoftware.fp.world.World;

public class TarantulaBoss extends Spider {

	private static final Color TARANTULA_BODY_COLOR = new Color(104, 20, 14);

	private static final int MAX_HEALTH = 1000;
	private static final int DEFENSE = 500;
	private static final int SIGHT_RANGE = 50;
	private static final double MOVE_SPEED = 1;
	private static final double MELEE_REACH = 2.5;
	private static final double WEB_REACH = 10.5;
	private static final double SPAWN_SPIDER_REACH = SIGHT_RANGE;

	private static final int MELEE_DMG = 20;
	private static final int SUMMON_SPIDER_COUNT = 3;

	private static final int COINS_MIN = 50;
	private static final int COINS_MAX = 100;
	private static final int XP_MIN = 150;
	private static final int XP_MAX = 250;
	
	// Stores the next attack that the spider is going to execute
	private TarantulaAttacks nextAttack; 

	private Timer attackTimer;
	private static final int ATTACK_CD_MIN = 5500;
	private static final int ATTACK_CD_MAX = 8500;

	public TarantulaBoss(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, random, TARANTULA_BODY_COLOR, EYE_COLOR);

		setMaxHealth(MAX_HEALTH);
		setIsBoss(true);
		setName("Blood Tarantula");
		setBossbarColor(TARANTULA_BODY_COLOR);
		setBaseDefense(DEFENSE);
		setSpeed(MOVE_SPEED);
		
		// Starts by taking a very long pause to attack
		attackTimer = new Timer(ATTACK_CD_MAX);

		pickNextAttack();

		// Clear the default spider drops
		dropTable.clear();
		dropTable.addEntry(new LootEntry(Items.Web, 10, 35, 100));
		dropTable.addEntry(new LootEntry(Items.Poison, 5, 15, 100));
		dropTable.addEntry(new LootEntry(Items.BloodSilk, 5, 30, 100));
		
		setCoinsMin(COINS_MIN);
		setCoinsMax(COINS_MAX);
		setXpMin(XP_MIN);
		setXpMax(XP_MAX);
	}

	public void updateBehavior(long passedTime, Camera c, Point updateCenter) {
		if (targetEntity == null || targetEntity.isDisposed()) {
			targetNearestPlayer(SIGHT_RANGE);
		}
		updateFollowingTarget(passedTime);
		if (inTargetRange && targetEntity != null) {
			attackTimer.update(passedTime);
			if (attackTimer.query()) {
				executeAttack();
				pickNextAttack();
				attackTimer.reset(RandomUtils.randomIntBetween(random, ATTACK_CD_MIN, ATTACK_CD_MAX));
			}
		}
		if (targetEntity != null) {
			face(getFaceAngleTo(targetEntity.getDrawCenter(c), c));
		}

		updateMovingLegs(passedTime);
	}

	private void executeAttack() {
		switch (nextAttack) {
			case Melee:
				targetEntity.damage(MELEE_DMG, new DamageCause(DamageCauses.Entity, this));				
				break;
			case WebShot:
				world.attemptLaunch(this, Projectiles.WebShot, targetEntity.getCenter());
				break;
			case SummonSpiders:
				world.spawnEntity(SpawnableEntities.Spider, getWorldX(), getWorldY(), SUMMON_SPIDER_COUNT);
				break;
		}
	}

	private void pickNextAttack() {
		nextAttack = TarantulaAttacks.random(random);
		switch (nextAttack) {
			case Melee:
				attackReach = MELEE_REACH;
				break;
			case WebShot:
				attackReach = WEB_REACH;
				break;
			case SummonSpiders:
				attackReach = SPAWN_SPIDER_REACH;
				break;
		}
	}

	private enum TarantulaAttacks {
		Melee(0), WebShot(1), SummonSpiders(2);

		private int value;

		TarantulaAttacks(int value) {
			this.value = value;
		}

		public static TarantulaAttacks random(Random random) {
			return getType(RandomUtils.randomIntBetween(random, 0, getAttackCount() - 1));
		}

		public static int getAttackCount() {
			return TarantulaAttacks.class.getEnumConstants().length;
		}

		public int getValue() {
			return value;
		}

		public static TarantulaAttacks getType(int value) {
			TarantulaAttacks[] possibleValues = TarantulaAttacks.class.getEnumConstants();
			for (int i = 0; i < possibleValues.length; i++) {
				if (possibleValues[i].value == value) {
					return possibleValues[i];
				}
			}
			return TarantulaAttacks.Melee;
		}
	}

}
