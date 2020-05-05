package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.Random;

import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.World;

public class SwingingHumanoidEnemy extends Humanoid {

	private static final double SWING_SPD = 5;
	private static final double SWING_MIN = -Math.PI / 3;
	private static final double SWING_MAX = Math.PI / 3;
	private boolean swinging;
	private double swingLerp;
	private int swingDirection;
	private Timer attackTimer;
	
	private final double sightRange;
	
	public SwingingHumanoidEnemy(double worldX, double worldY, int size, int tileSize, World world, Color bodyColor,
			Color hand1Color, Color hand2Color, Random random, String name, int attackCooldown, Items weapon, 
			int baseDamage, double sightRange, double attackReach) {
		super(worldX, worldY, size, tileSize, world, bodyColor, hand1Color, hand2Color, random, name);
		
		attackTimer = new Timer(attackCooldown);
		swingDirection = 1;
		
		this.sightRange = sightRange;
		
		pickupItemStack(new ItemStack(weapon, 1));
		getInventory().setSelectedSlot(0);
		
		this.attackReach = attackReach;
		
		setBaseDamage(baseDamage);
	}
	
	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);
		
		if (targetEntity == null || targetEntity.isDisposed()) {
			targetNearestPlayer(sightRange);
		}
		
		double angleOffset = 0;
		if (swinging) {
			double passedSeconds = passedTime / 1000.0;
			swingLerp += passedSeconds * swingDirection * SWING_SPD;
			if ((swingLerp >= 1 && swingDirection > 0) || 
					(swingLerp <= 0 && swingDirection < 0)) {
				if (swingDirection < 0) {
					targetEntity.damage(getDamage(), new DamageCause(DamageCauses.Entity, this));
					swingDirection = 1;
					swingLerp = 0;
					swinging = false;
				} else {
					swingDirection *= -1;
				}
			}
			angleOffset = MathUtils.lerp(SWING_MIN, SWING_MAX, swingLerp);
		} else {
			updateFollowingTarget(passedTime);
			
			if (inTargetRange && targetEntity != null) {
				attackTimer.update(passedTime);
				if (attackTimer.query()) {
					swinging = true;
					attackTimer.reset();
				}
			}
			
		}
		if (targetEntity != null) {
			face(getFaceAngleTo(targetEntity.getDrawCenter(c), c));
			setHandAngleOffset(angleOffset);
		}
	}

}
