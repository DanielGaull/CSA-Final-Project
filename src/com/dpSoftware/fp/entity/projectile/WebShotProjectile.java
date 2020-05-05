package com.dpSoftware.fp.entity.projectile;

public class WebShotProjectile extends SpinningProjectile {
	
	private static final int SPEED = 3;
	private static final int DAMAGE = 15;
	private static final long LIFETIME = 5000;
	private static final double SPIN_SPD = 20;
	private static final String FILE_NAME = "webshot";
	
	public WebShotProjectile(double worldX, double worldY, int size, int tileSize) {
		super(worldX, worldY, size, tileSize, SPEED, DAMAGE, Projectiles.Fireball, LIFETIME, SPIN_SPD, FILE_NAME);
	}

}
