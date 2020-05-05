package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import com.dpSoftware.fp.crafting.CraftingRecipe;
import com.dpSoftware.fp.entity.projectile.Projectile;
import com.dpSoftware.fp.items.Inventory;
import com.dpSoftware.fp.items.ItemCategories;
import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.ArrayUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Input;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.RandomUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.util.Vector;
import com.dpSoftware.fp.world.LootTable;
import com.dpSoftware.fp.world.World;
import com.dpSoftware.fp.world.WorldNode;
import com.dpSoftware.fp.world.decoration.Decoration;
import com.dpSoftware.fp.world.tiles.Tile;
import com.dpSoftware.fp.world.tiles.TileTypes;

public abstract class Creature extends Entity {

	protected Inventory inventory;
	protected LootTable dropTable;

	protected Random random;

	private double health;
	private double maxHealth;
	private String name;
	private boolean disposeOnDeath;

	private int coinsMin;
	private int coinsMax;
	private int xpMin;
	private int xpMax;

	private boolean isBoss;
	private Color bossbarColor;

	private int damage;
	private int defense;

	protected Creature targetEntity;
	protected boolean inTargetRange;
	protected boolean isMoving;
	protected boolean isRotating;
	private boolean canPhase;
	private boolean canSpeedSwim;
	private WorldNode lastTargetNode;
	private ArrayList<WorldNode> path;
	private int pathIndex;
	private static final double PATH_DISTANCE_MIN = 0.1;
	private static final int ASTAR_DEFAULT_DIST = Integer.MAX_VALUE;
	private static final int ASTAR_SIGHT_RANGE = 10;

	private static final Color BASE_RED_COLOR = new Color(250, 0, 0);
	private static final double RED_BASE_LERP = 0.75;
	private double flashRedLerp;
	private boolean flashingRed;
	private int flashRedDirection;
	private static final double FLASH_RED_SPD = 5;

	// By default, despawns when 100 or more tiles from the player
	private static final double DESPAWN_DISTANCE = 100;
	// Distance to check for decoration collisions
	public static final double COLLISION_CHECK_RADIUS = 2.5;

	private boolean doingDamageCooldown;
	private Timer damageCooldownTimer;
	private static final long DAMAGE_COOLDOWN = 350;

	protected double interactDistance;
	protected double pickupDistance;
	protected double attackReach;

	private double baseSpeed;
	private static final double SWIM_SPD_PERCENT = 0.4;
	private static final double SLOW_DECORATION_SPD_PERCENT = 0.65; // Takes off
																	// 65% of
																	// your
																	// speed

	protected World world;

	public Creature(double worldX, double worldY, int width, int height, int tileSize, World world, Random random,
			String name) {
		super(worldX, worldY, width, height, tileSize);

		inventory = new Inventory();

		interactDistance = 2.25;
		pickupDistance = 3.5;
		attackReach = 2.25;

		maxHealth = 50;
		health = maxHealth;
		disposeOnDeath = true;
		canPhase = false;

		damage = defense = 0;

		this.world = world;
		this.random = random;
		this.name = name;

		inTargetRange = false;
		isMoving = false;
		isRotating = false;

		dropTable = new LootTable();

		damageCooldownTimer = new Timer(DAMAGE_COOLDOWN);

		path = new ArrayList<>();

		setDespawnDistance(DESPAWN_DISTANCE);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		if (doingDamageCooldown) {
			damageCooldownTimer.update(passedTime);
			if (damageCooldownTimer.query()) {
				damageCooldownTimer.reset();
				doingDamageCooldown = false;
			}
		}

		if (flashingRed) {
			flashRedLerp += (passedTime / 1000.0) * flashRedDirection * FLASH_RED_SPD;
			if (flashRedLerp >= 1 && flashRedDirection > 0) {
				flashRedLerp = 1;
				flashRedDirection = -1;
			} else if (flashRedLerp <= 0 && flashRedDirection < 0) {
				flashingRed = false;
				flashRedLerp = 0;
				flashRedDirection = 1;
			}
		}

		for (int i = 0; i < world.getEntities().size(); i++) {
			if (world.getEntities().getEntity(i) instanceof Projectile) {
				Projectile proj = (Projectile) world.getEntities().getEntity(i);
				if (proj.getLauncher().canAttack(this) && this.intersects(proj)) {
					damage(proj.getDamage(), new DamageCause(DamageCauses.Entity, proj));
					proj.impact();
				}
			}
		}
	}

	public void face(double angle) {
		isRotating = (facingAngle != angle);
		super.face(angle);
	}

	protected void targetNearestPlayer(double maxSearchRange) {
		PlayerEntity closestPlayer = null;
		double closestPlayerDistance = 0;
		for (int i = 0; i < world.getEntities().size(); i++) {
			if (world.getEntities().getEntity(i) instanceof PlayerEntity) {
				if ((closestPlayer == null
						|| closestPlayerDistance > distanceTo(world.getEntities().getEntity(i).getCenter()))
						&& distanceTo(world.getEntities().getEntity(i).getCenter()) <= maxSearchRange) {
					closestPlayer = (PlayerEntity) world.getEntities().getEntity(i);
					closestPlayerDistance = distanceTo(closestPlayer.getCenter());
				}
				break;
			}
		}
		targetEntity = closestPlayer;
	}

	public boolean intersectsTileType(TileTypes tile) {
		Tile[] nearbyTiles = world.getNearbyTiles(getCenter());
		for (int i = 0; i < nearbyTiles.length; i++) {
			if (nearbyTiles[i].getTileType() == tile && nearbyTiles[i].intersects(this)) {
				return true;
			}
		}
		return false;
	}

	public void pickupItemStack(ItemStack stack) {
		int leftovers = inventory.addItem(stack);
		if (leftovers > 0) {
			dropItem(new ItemStack(stack.getItem(), leftovers));
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory value) {
		inventory = value;
	}

	public ItemStack getHeldItem() {
		return inventory.getSelectedItem();
	}

	public ItemStack getShield() {
		return inventory.getShield();
	}

	public int getSelectedSlot() {
		return inventory.getSelectedSlot();
	}

	public double getInteractDistance() {
		return interactDistance;
	}

	public double getPickupDistance() {
		return pickupDistance;
	}

	public double getAttackReach() {
		return attackReach;
	}

	public double getHealth() {
		return health;
	}

	public double getHealthPercent() {
		return health / maxHealth * 100.0;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	// Default drop behavior
	public ArrayList<ItemStack> getDrops() {
		return dropTable.runLootTable(random);
	}

	public int getCoinDrops() {
		if (coinsMin == coinsMax)
			return coinsMin;
		return RandomUtils.randomIntBetween(random, coinsMin, coinsMax);
	}

	public int getXpDrop() {
		if (xpMin == xpMax)
			return xpMin;
		return RandomUtils.randomIntBetween(random, xpMin, xpMax);
	}

	public void setCoinsMin(int value) {
		coinsMin = value;
	}

	public void setCoinsMax(int value) {
		coinsMax = value;
	}

	public void setXpMin(int value) {
		xpMin = value;
	}

	public void setXpMax(int value) {
		xpMax = value;
	}

	public void setSpeed(double value) {
		super.setSpeed(value);
		baseSpeed = value;
	}

	public void setDisposeOnDeath(boolean value) {
		disposeOnDeath = value;
	}

	public void damage(double damage, DamageCause cause) {
		if (!doingDamageCooldown) {
			double trueDamage = damage * (1 - getDamageReduction());
			health -= trueDamage;
			doingDamageCooldown = true;
			if (health <= 0) {
				health = 0;
				onDeath(cause);
				if (disposeOnDeath) {
					dispose();
				}
			}
			flashingRed = true;
			flashRedLerp = 0;
			flashRedDirection = 1;
		}
	}

	public Color getDamageColor(Color baseColor) {
		// Start by calculating the final red color
		Color finalRedColor = MathUtils.colorLerp(baseColor, BASE_RED_COLOR, RED_BASE_LERP);
		return MathUtils.colorLerp(baseColor, finalRedColor, flashRedLerp);
	}

	public Color getDamageColor() {
		return MathUtils.colorLerp(new Color(255, 255, 255, 0), BASE_RED_COLOR, flashRedLerp);
	}

	public void onDeath(DamageCause deathCause) {
		world.onDeath(this, deathCause);
	}

	public boolean canAttack(Creature other) {
		if (other == this) {
			return false;
		}
		return true;
	}

	public int getDamage() {
		int damage = this.damage;
		if (!inventory.getSelectedItem().checkEmpty()) {
			damage += inventory.getSelectedItem().getItem().getBaseDamage();
		}
		if (inventory.getShield() != null && !inventory.getShield().checkEmpty()
				&& !(!inventory.getSelectedItem().checkEmpty()
						&& inventory.getSelectedItem().getItem().getCategory() == ItemCategories.Shield)) {
			// If you hold a shield (in the main slot), it overrides the shield
			// in your shield slot
			damage += inventory.getShield().getItem().getBaseDamage();
		}
		return damage;
	}

	public int getDefense() {
		int defense = this.defense;
		if (inventory.getShield() != null && !inventory.getShield().checkEmpty()
				&& !(!inventory.getSelectedItem().checkEmpty()
						&& inventory.getSelectedItem().getItem().getCategory() == ItemCategories.Shield)) {
			// If you hold a shield (in the main slot), it overrides the shield
			// in your shield slot
			defense += inventory.getShield().getItem().getBaseDefense();
		}
		if (!inventory.getSelectedItem().checkEmpty()) {
			defense += inventory.getSelectedItem().getItem().getBaseDefense();
		}
		return defense;
	}

	public int getBaseDamage() {
		return damage;
	}

	public int getBaseDefense() {
		return defense;
	}

	public void setBaseDamage(int value) {
		damage = value;
	}

	public void setBaseDefense(int value) {
		defense = value;
	}

	public double getDamageReduction() {
		return getDefense() / (100.0 + getDefense());
	}

	public boolean canCraft(CraftingRecipe recipe) {
		return inventory.canCraft(recipe);
	}

	public void craft(CraftingRecipe recipe) {
		if (canCraft(recipe)) {
			// Consume the ingredients from the inventory
			for (int i = 0; i < recipe.getIngredients().size(); i++) {
				inventory.remove(recipe.getIngredients().get(i));
			}
			// Give the creature the item it just crafted
			pickupItemStack(recipe.getResult());
		}
	}

	public boolean isBoss() {
		return isBoss;
	}

	public void setIsBoss(boolean value) {
		isBoss = value;
	}

	public Color getBossbarColor() {
		return bossbarColor;
	}

	public void setBossbarColor(Color color) {
		bossbarColor = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public void setHealth(double value) {
		health = value;
	}

	public void setMaxHealth(double value) {
		double percentage = health / maxHealth;
		maxHealth = value;
		health = percentage * value;
	}

	public void changeHealth(double amount) {
		health += amount;
		if (health > maxHealth) {
			health = maxHealth;
		} else if (health <= 0) {
			health = 0;
			onDeath(DamageCause.UNKNOWN);
		}
	}

	public void dropInvItem(int row, int col, int amount) {
		ItemStack stack = new ItemStack(inventory.getInvItem(row, col).getItem(), amount);
		inventory.removeFromSlot(row, col, amount);
		dropItem(stack);
	}

	public void dropItem(ItemStack stack) {
		world.attemptDrop(this, stack);
	}

	public boolean canPhase() {
		return canPhase;
	}

	public void setCanPhase(boolean value) {
		canPhase = value;
	}

	public boolean canSpeedSwim() {
		return canSpeedSwim;
	}

	public void setCanSpeedSwim(boolean value) {
		canSpeedSwim = value;
	}

	protected void updateFollowingTarget(long passedTime) {
		if (targetEntity != null) {
			if (distanceTo(targetEntity.getCenter()) > attackReach) {
				if (canPhase) {
					followTargetPhasing(passedTime);
				} else {
					followTargetAStar(passedTime);
				}
				inTargetRange = false;
				isMoving = true;
			} else {
				inTargetRange = true;
				isMoving = false;
			}
		}
		isMoving = false;
	}

	private void followTargetPhasing(long passedTime) {
		double angle = MathUtils.angleBetween(getCenter(), targetEntity.getCenter());
		Vector vector = new Vector(Math.cos(angle), Math.sin(angle));
		changeX(vector.getX() * getSpeed() * (passedTime / 1000.0));
		changeY(vector.getY() * getSpeed() * (passedTime / 1000.0));
	}

	private void followTargetAStar(long passedTime) {
		if (pathIndex >= path.size()) {
			reloadAStarPathTrack();
		} else {
			if (!lastTargetNode.equals(targetEntity.getWorldNode())) {
				reloadAStarPathTrack();
			}
			// If the path list is still empty, it means that we weren't close
			// enough to the player to actually establish a
			// successful path to them
			if (path.size() > 0) {
				if (getCenter().distanceTo(path.get(pathIndex).getCenterPoint()) < PATH_DISTANCE_MIN) {
					pathIndex++;
					if (pathIndex >= path.size()) {
						reloadAStarPathTrack();
					}
				}
				double angle = MathUtils.angleBetween(getCenter(), path.get(pathIndex).getCenterPoint());
				Vector vector = new Vector(Math.cos(angle), Math.sin(angle));
				changeX(vector.getX() * getSpeed() * (passedTime / 1000.0));
				changeY(vector.getY() * getSpeed() * (passedTime / 1000.0));
			}
		}
	}

	private void reloadAStarPathTrack() {
		lastTargetNode = targetEntity.getWorldNode();
		// Need to generate the list of valid nodes
		ArrayList<Tile> tiles = new ArrayList<>(Arrays.asList(world.getNearbyTilesNoGenerate(getCenter(), ASTAR_SIGHT_RANGE)));
		ArrayList<WorldNode> nodes = new ArrayList<>();
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i) != null && (tiles.get(i).getDecoration() == null || !tiles.get(i).getDecoration().canCollide())) {
				// Non-collideable or non-existant decoration
				nodes.add(tiles.get(i).getWorldNode());
			}
		}
		aStar(getWorldNode(), targetEntity.getWorldNode(), nodes);
	}

	private void reloadAStarPathRandom(TileTypes[] canMoveTo, boolean avoidCollideableDecorations, int searchRange) {
		ArrayList<Tile> tiles = new ArrayList<>(Arrays.asList(world.getNearbyTiles(getCenter(), searchRange)));
		ArrayList<WorldNode> nodes = new ArrayList<>();
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getDecoration() != null) {
				if (avoidCollideableDecorations && tiles.get(i).getDecoration().canCollide()) {
					// Skip this tile, since it has a collideable decoration
					continue;
				}
			}
			if (ArrayUtils.arrayContains(canMoveTo, tiles.get(i).getTileType()) || canMoveTo.length <= 0) {
				if (!tiles.get(i).getWorldNode().equals(getWorldNode())) {
					nodes.add(tiles.get(i).getWorldNode());
				}
			}
		}
		aStar(getWorldNode(), ArrayUtils.random(nodes, random), nodes);
	}

	// Runs the A* Pathfinding Algorithm
	private void aStar(WorldNode start, WorldNode goal, ArrayList<WorldNode> nodes) {
		pathIndex = 0;
		path.clear();
		if (start.equals(goal)) {
			return;
		}
		if (!nodes.contains(goal)) {
			return;
		}

		ArrayList<WorldNode> openSet = new ArrayList<>();
		openSet.add(start);
		HashMap<WorldNode, WorldNode> cameFrom = new HashMap<>();
		ArrayList<WorldNode> closedSet = new ArrayList<>();
		WorldNode current = new WorldNode();

		HashMap<WorldNode, Integer> gScores = new HashMap<>();
		for (int i = 0; i < nodes.size(); i++) {
			gScores.put(nodes.get(i), ASTAR_DEFAULT_DIST);
		}
		gScores.put(start, 0);

		HashMap<WorldNode, Integer> fScores = new HashMap<>();
		for (int i = 0; i < nodes.size(); i++) {
			fScores.put(nodes.get(i), ASTAR_DEFAULT_DIST);
		}
		fScores.put(start, heuristic(start, goal));

		while (openSet.size() > 0) {
			// Calculate current node by finding node in openSet with lowest
			// fScore
			double min = ASTAR_DEFAULT_DIST;
			for (int i = 0; i < openSet.size(); i++) {
				if (fScores.get(openSet.get(i)) < min) {
					min = fScores.get(openSet.get(i));
					current = openSet.get(i);
				}
			}

			if (current.equals(goal)) {
				path = reconstructPath(cameFrom, current);
				Collections.reverse(path);
				// Don't include the current node in the path
				if (path.size() > 0 && path.get(0).equals(getWorldNode())) {
					path.remove(0);
				}
				return;
			}

			openSet.remove(current);
			closedSet.add(current);

			ArrayList<WorldNode> neighbors = current.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				if (closedSet.contains(neighbors.get(i))) {
					// Neighbor has already been evaluated; continue
					continue;
				}
				if (!nodes.contains(neighbors.get(i))) {
					// This node is not permitted to travel to
					continue;
				}

				if (!openSet.contains(neighbors.get(i))) {
					// Discover a new node
					openSet.add(neighbors.get(i));
				}

				int tentativeGScore = gScores.get(current) + current.distanceBetween(neighbors.get(i));
				if (tentativeGScore < gScores.get(neighbors.get(i))) {
					cameFrom.put(neighbors.get(i), current);
					gScores.put(neighbors.get(i), tentativeGScore);
					fScores.put(neighbors.get(i), gScores.get(neighbors.get(i)) + heuristic(neighbors.get(i), goal));
				}
			}
		}
	}

	public void updateWandering(long passedTime, boolean avoidCollideableDecorations, int searchRange,
			TileTypes... allowedTiles) {
		if (pathIndex >= path.size()) {
			reloadAStarPathRandom(allowedTiles, avoidCollideableDecorations, searchRange);
		} else {
			if (path.size() > 0) {
				if (getCenter().distanceTo(path.get(pathIndex).getCenterPoint()) < PATH_DISTANCE_MIN) {
					pathIndex++;
					if (pathIndex >= path.size()) {
						reloadAStarPathRandom(allowedTiles, avoidCollideableDecorations, searchRange);
					}
				}
				double angle = MathUtils.angleBetween(getCenter(), path.get(pathIndex).getCenterPoint());
				face(angle);
				Vector vector = new Vector(Math.cos(angle), Math.sin(angle));
				changeX(vector.getX() * getSpeed() * (passedTime / 1000.0));
				changeY(vector.getY() * getSpeed() * (passedTime / 1000.0));
			}
		}
	}

	private int heuristic(WorldNode start, WorldNode end) {
		return Math.abs(start.getY() - end.getY()) + Math.abs(start.getX() - end.getX());
	}

	private ArrayList<WorldNode> reconstructPath(HashMap<WorldNode, WorldNode> cameFrom, WorldNode current) {
		ArrayList<WorldNode> totalPath = new ArrayList<>();
		totalPath.add(current);
		while (cameFrom.keySet().contains(current)) {
			current = cameFrom.get(current);
			totalPath.add(current);
		}
		return totalPath;
	}
}
