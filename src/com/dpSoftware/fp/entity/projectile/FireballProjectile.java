package com.dpSoftware.fp.entity.projectile;

public class FireballProjectile extends SpinningProjectile {

	private static final int SPEED = 5;
	private static final int DAMAGE = 10;
	private static final long LIFETIME = 10000;
	private static final double SPIN_SPD = 15;
	private static final String FILE_NAME = "fireball";
	
	public FireballProjectile(double worldX, double worldY, int size, int tileSize) {
		super(worldX, worldY, size, tileSize, SPEED, DAMAGE, Projectiles.Fireball, LIFETIME, SPIN_SPD, FILE_NAME);
	}

}
