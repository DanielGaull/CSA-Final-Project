package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.World;

public abstract class PunchingHumanoidEnemy extends Humanoid {

	private Timer attackTimer;
	private boolean hand1Punching; // If true, hand 1 punches; otherwise, hand 2 punches
	private boolean attacking;
	private boolean pullingHandBack;
	private static final int PUNCH_SPD = 250;
	private static final int PULLBACK_SPD = 75;
	private static final int ATT_OFFSET_MAX = 10;
	
	private final double sightRange;
	private final double attackDamage;

	public PunchingHumanoidEnemy(double worldX, double worldY, int size, int tileSize, World world, Color bodyColor,
			Color hand1Color, Color hand2Color, Random random, String name, int attackCooldown, double sightRange,
			double attackDamage, double attackReach) {
		super(worldX, worldY, size, tileSize, world, bodyColor, hand1Color, hand2Color, random, name);
		this.sightRange = sightRange;
		this.attackDamage = attackDamage;
		this.attackReach = attackReach;
		
		attackTimer = new Timer(attackCooldown);
		forceHandOffset = true;
	}
	
	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		if (targetEntity == null || targetEntity.isDisposed()) {
			targetNearestPlayer(sightRange);
		}
		updateFollowingTarget(passedTime);
		if (inTargetRange && targetEntity != null) {
			if (attacking) {
				// This means the attack animation is running
				if (hand1Punching) {
					hand1OffsetY += PUNCH_SPD * (passedTime / 1000.0);
					if (hand1OffsetY >= ATT_OFFSET_MAX) {
						hand1OffsetY = ATT_OFFSET_MAX;
						attackTarget();
					}
				} else {
					hand2OffsetY += PUNCH_SPD * (passedTime / 1000.0);
					if (hand2OffsetY >= ATT_OFFSET_MAX) {
						hand2OffsetY = ATT_OFFSET_MAX;
						attackTarget();
					}
				}
			} else if (pullingHandBack) {
				if (hand1Punching) {
					hand1OffsetY -= PULLBACK_SPD * (passedTime / 1000.0);
					if (hand1OffsetY <= 0) {
						hand1OffsetY = 0;
						hand1Punching = !hand1Punching;
						pullingHandBack = false;
					}
				} else {
					hand2OffsetY -= PULLBACK_SPD * (passedTime / 1000.0);
					if (hand2OffsetY <= 0) {
						hand2OffsetY = 0;
						hand1Punching = !hand1Punching;
						pullingHandBack = false;
					}
				}
			} else {
				attackTimer.update(passedTime);
				if (attackTimer.query()) {
					attacking = true;
				}
			}
		} else {
			attackTimer.reset();
			attacking = false;
		}
		if (targetEntity != null) {
			face(getFaceAngleTo(targetEntity.getDrawCenter(c), c));
		}
	}
	private void attackTarget() {
		targetEntity.damage(attackDamage, new DamageCause(DamageCauses.Entity, this));
		pullingHandBack = true;
		attacking = false;
		attackTimer.reset();
	}
	
}
