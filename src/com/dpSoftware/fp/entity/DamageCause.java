package com.dpSoftware.fp.entity;

import com.dpSoftware.fp.world.tiles.Tile;

public class DamageCause {
	
	private DamageCauses enumCause;
	private Entity entityCause;
	private Tile tileCause;
	
	public static final DamageCause UNKNOWN = new DamageCause(DamageCauses.Unknown);
	
	public DamageCause(DamageCauses cause) {
		enumCause = cause;
	}
	public DamageCause(DamageCauses cause, Entity entity) {
		enumCause = cause;
		entityCause = entity;
	}
	public DamageCause(DamageCauses cause, Tile tile) {
		enumCause = cause;
		tileCause = tile;
	}
	
	public DamageCauses getCause() {
		return enumCause;
	}
	public Entity getCausingEntity() {
		return entityCause;
	}
	public Tile getCausingTile() {
		return tileCause;
	}
	
}
