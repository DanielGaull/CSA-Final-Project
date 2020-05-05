package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.dpSoftware.fp.crafting.CraftingRecipe;
import com.dpSoftware.fp.crafting.CraftingRecipes;
import com.dpSoftware.fp.items.Inventory;
import com.dpSoftware.fp.items.ItemAbility;
import com.dpSoftware.fp.items.ItemCategories;
import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.ui.ProgressBar;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.Directions;
import com.dpSoftware.fp.util.Input;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.util.Timer;
import com.dpSoftware.fp.world.World;
import com.dpSoftware.fp.world.decoration.Decoration;
import com.dpSoftware.fp.world.tiles.Tile;
import com.dpSoftware.fp.world.tiles.TileTypes;

public class PlayerEntity extends Humanoid {

	private static final Color SKIN_COLOR = new Color(150, 95, 65);
	private static final int HAND_COLOR_REDUCE_AMOUNT = 20;
	private static final Color HAND_COLOR = new Color(SKIN_COLOR.getRed() - HAND_COLOR_REDUCE_AMOUNT,
			SKIN_COLOR.getGreen() - HAND_COLOR_REDUCE_AMOUNT, SKIN_COLOR.getBlue() - HAND_COLOR_REDUCE_AMOUNT);

	private ProgressBar actionProgress;
	private Timer actionTimer;
	private Point clickLoc;
	private boolean isAttacking;
	private boolean isBreaking;
	private static final int BREAKBAR_WIDTH = 35;
	private static final int BREAKBAR_HEIGHT = 7;
	private static final int BREAKBAR_Y_SPACING = 10;
	private static final Color BREAKBAR_BORDER = new Color(0, 127, 0);
	private static final Color BREAKBAR_FILL = new Color(0, 200, 0);
	private static final Color BREAKBAR_BG = new Color(0, 155, 0);
	private static final String AXE_BREAK_TEXT = "Chopping";
	private static final String PICKAXE_BREAK_TEXT = "Mining";
	private static final String SHOVEL_BREAK_TEXT = "Digging";
	private static final String CLIPPERS_BREAK_TEXT = "Clipping";
	private static final Font BREAKTEXT_FONT = new Font("Arial", Font.BOLD, 12);
	private double actionAnimationLerpFactor;
	private int actionAnimationDirection;
	private double actionAnimationAngleOffset;
	private double actionAnimationSpd;
	private double actionAnimationOffsetMin;
	private double actionAnimationOffsetMax;
	private double CHOPPING_OFFSET_MIN = -Math.PI / 4;
	private double CHOPPING_OFFSET_MAX = Math.PI / 4;
	private double MINING_OFFSET_MIN = -Math.PI / 4;
	private double MINING_OFFSET_MAX = Math.PI / 4;
	private double DIGGING_OFFSET_MIN = 0;
	private double DIGGING_OFFSET_MAX = Math.PI / 2;
	private double CLIPPING_OFFSET_MIN = -Math.PI / 24;
	private double CLIPPING_OFFSET_MAX = Math.PI / 24;
	private double SWINGING_OFFSET_MIN = -Math.PI / 3;
	private double SWINGING_OFFSET_MAX = Math.PI / 3;
	private double CHOPPING_SPD = 5;
	private double MINING_SPD = 5;
	private double DIGGING_SPD = 5;
	private double CLIPPING_SPD = 3;
	private double SWINGING_SPD = 5;

	private double energy;
	private double maxEnergy;

	public static final double DEFAULT_ENERGY = 100;
	public static final double DEFAULT_HEALTH = 100;

	private int coins;
	private int level;
	private int xp;

	private static final long EAT_TIME = 1500; // Takes 1.5 seconds to eat food
	private boolean isEating;
	private Timer eatTimer;
	private double eatAngle;
	private static final double EAT_HAND_OFFSET_X_RATIO = -0.2;
	private final int eatingHandOffsetX;
	private static final double EAT_HAND_OFFSET_Y_RATIO = -0.2;
	private final int eatingHandOffsetY;

	private Timer naturalHealingTimer;
	// Costs 1 unit of energy to heal 2 health 
	private static final double NATURAL_HEALING_AMOUNT = 1;
	private static final double NATURAL_HEALING_COST = 0.5;
	private static final int NATURAL_HEALING_COOLDOWN = 750;
	// Requires more than 75% of max energy to heal naturally
	private static final double NATURAL_HEAL_ENERGY_PERCENT_REQUIRED = 0.75;

	// Requires more than 50% of max energy to sprint
	private static final double SPRINT_ENERGY_PERCENT_REQUIRED = 0.5;
	private boolean sprinting;
	private boolean swimming;
	private static final double WALK_SPD = 5;
	private static final double SPRINT_SPD_BOOST = 4;
	private static final double SWIM_SPD = 2;
	private static final double SLOW_DECORATION_SPD_PERCENT = 0.65; // Takes off 65% of your speed

	private Timer sprintEnergyTimer;
	private Timer walkEnergyTimer;
	private Timer swimEnergyTimer;
	// How many milliseconds it takes for sprinting to consume energy
	private static final long SPRINT_ENERGY_MILLIS = 4500;
	// How many milliseconds it takes for walking to consume energy
	private static final long WALK_ENERGY_MILLIS = 9500;
	// How many milliseconds it takes for swimming to consume energy
	private static final long SWIM_ENERGY_MILLIS = 5500; 
	// Cost of each unit time of movement (determined by the type of movement - walking/sprinting/swimming) 
	private static final double MOVE_ENERGY_COST = 1.0;
	// Cost of each attack
	private static final double ATTACK_ENERGY_COST = 0.25;
	// Cost of each decoration broken
	private static final double BREAK_ENERGY_COST = 0.5;
	
	private Timer itemHealTimerHand;
	private Timer itemHealTimerShield;
	private Timer itemEnergyTimerHand;
	private Timer itemEnergyTimerShield;
	private double maxHealthBoostHand;
	private double maxHealthBoostShield;
	private double maxEnergyBoostHand;
	private double maxEnergyBoostShield;
	
	private ItemStack lastHeldItem;
	private ItemStack lastShield;

	private boolean leftClickStarted;

	private boolean hotbarKeyPrevDown;

	public PlayerEntity(double worldX, double worldY, int size, int tileSize, World world, Random random) {
		super(worldX, worldY, size, tileSize, world, SKIN_COLOR, HAND_COLOR, HAND_COLOR, random, "Player");

		actionProgress = new ProgressBar(0, 0, BREAKBAR_WIDTH, BREAKBAR_HEIGHT, BREAKBAR_BORDER, BREAKBAR_FILL,
				BREAKBAR_BG, 0, 0);
		isBreaking = false;
		leftClickStarted = false;

		eatingHandOffsetX = (int) (EAT_HAND_OFFSET_X_RATIO * size);
		eatingHandOffsetY = (int) (EAT_HAND_OFFSET_Y_RATIO * size);

		sprinting = false;

		setMaxHealth(DEFAULT_HEALTH);
		setDisposeOnDeath(false);
		
		// Make the base class avoid messing with the player's speed values; the player will move themselves
		setCanPhase(true);
		setCanSpeedSwim(true);

		clickLoc = new Point();

		energy = maxEnergy = DEFAULT_ENERGY;

		naturalHealingTimer = new Timer(NATURAL_HEALING_COOLDOWN);

		hotbarKeyPrevDown = false;

		sprintEnergyTimer = new Timer(SPRINT_ENERGY_MILLIS);
		walkEnergyTimer = new Timer(WALK_ENERGY_MILLIS);
		swimEnergyTimer = new Timer(SWIM_ENERGY_MILLIS);

		eatTimer = new Timer(EAT_TIME);

		coins = 0;
		xp = 0;
		level = 1;
		
		lastHeldItem = ItemStack.empty();
		lastShield = ItemStack.empty();
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);

		double passedSeconds = passedTime / 1000.0;

		if (!Input.isLeftDown()) {
			leftClickStarted = false;
		}

		// Want to make sure that they will have 80 energy when healing runs out
		if (Math.floor(energy) > maxEnergy * NATURAL_HEAL_ENERGY_PERCENT_REQUIRED && getHealth() < getMaxHealth()) {
			naturalHealingTimer.update(passedTime);
			if (naturalHealingTimer.query()) {
				changeEnergy(-NATURAL_HEALING_COST);
				changeHealth(NATURAL_HEALING_AMOUNT);
				naturalHealingTimer.reset();
			}
		}

		actionProgress.setX(getDrawX(c) + getWidth() / 2 - actionProgress.getWidth() / 2);
		actionProgress.setY(getDrawY(c) - actionProgress.getHeight() - BREAKBAR_Y_SPACING);

		if (isBreaking || isAttacking) {
			// Update the "swinging" animation of the character using their
			// tool/sword
			// The "actionAnimationDirection" will always be either 1 or -1, and
			// will switch the direction
			// that the character "swings" in order to keep the animation cyclic
			actionAnimationLerpFactor += actionAnimationSpd * passedSeconds * actionAnimationDirection;
			actionAnimationAngleOffset = MathUtils.lerp(actionAnimationOffsetMin, actionAnimationOffsetMax,
					actionAnimationLerpFactor);
			
			if ((actionAnimationDirection > 0 && actionAnimationLerpFactor >= 1.0)
					|| (actionAnimationDirection < 0 && actionAnimationLerpFactor <= 0.0)) {
				actionAnimationDirection *= -1;
				if (isAttacking && actionAnimationDirection < 0) {
					// Switching to the positive swing direction means that the
					// attack has finished
					isAttacking = false;
					changeEnergy(-ATTACK_ENERGY_COST);
					world.attemptAttack(this, World.screenCoordToWorldCoord(clickLoc, c));
					actionAnimationAngleOffset = 0;
				}
			}
			if (isBreaking) {
				// Update the breaking timer and finish breaking the target if
				// the timer is up
				actionTimer.update(passedTime);
				actionProgress.setValue(actionTimer.getTimer());
				if (actionTimer.query()) {
					changeEnergy(-BREAK_ENERGY_COST);
					world.attemptBreak(this, World.screenCoordToWorldCoord(clickLoc, c));
					isBreaking = false;
					actionAnimationAngleOffset = 0;
				}

				if (Input.isLeftDown() && !leftClickStarted) {
					isBreaking = false;
					leftClickStarted = true;
				}
			}

			super.face(getFaceAngleTo(clickLoc, c));
			super.setHandAngleOffset(actionAnimationAngleOffset);

		} else if (isEating) {
			eatTimer.update(passedTime);
			actionProgress.setValue(eatTimer.getTimer());
			hand1OffsetX = eatingHandOffsetX;
			hand1OffsetY = eatingHandOffsetY;
			heldItemOffsetX = -eatingHandOffsetX;
			heldItemOffsetY = eatingHandOffsetY;
			forceHandOffset = true;
			if (eatTimer.query()) {
				forceHandOffset = false;
				isEating = false;
				changeEnergy(getHeldItem().getItem().getEnergyHealed());
				changeHealth(getHeldItem().getItem().getHealthHealed());
				inventory.removeFromSelectedSlot(1);
				hand1OffsetX = hand1OffsetY = 0;
				heldItemOffsetX = heldItemOffsetY = 0;
				eatTimer.reset();
			}

			if (Input.isLeftDown() && !leftClickStarted) {
				forceHandOffset = false;
				isEating = false;
				leftClickStarted = true;
				hand1OffsetX = hand1OffsetY = 0;
				heldItemOffsetX = heldItemOffsetY = 0;
				eatTimer.reset();
			}

			super.face(eatAngle);
		} else {
			double speed = 0;
			if (intersectsTileType(TileTypes.Water)) {
				speed = SWIM_SPD;
				swimming = true;
				sprinting = false;
			} else {
				speed = WALK_SPD;
				sprinting = false;
				swimming = false;
			}

			if (Input.isShiftPressed() && energy >= SPRINT_ENERGY_PERCENT_REQUIRED * maxEnergy) {
				speed += SPRINT_SPD_BOOST;
				sprinting = true;
				swimming = false;
			}
			ArrayList<Decoration> decorations = world.getDecorations(getCenter(), COLLISION_CHECK_RADIUS);
			for (int i = 0; i < decorations.size(); i++) {
				if (decorations.get(i).intersects(this)) {
					if (decorations.get(i).canSlow()) {
						speed -= speed * SLOW_DECORATION_SPD_PERCENT;
					}
				}
			}
			setSpeed(speed);

			// Update the player's position
			boolean moving = false;
			Directions moveDir = Directions.Up;
			// Requires at least some energy to move
			if (energy > 0) {
				if (Input.isKeyPressed(KeyEvent.VK_W)) {
					moveDir = Directions.Up;
					moving = true;
				} else if (Input.isKeyPressed(KeyEvent.VK_S)) {
					moveDir = Directions.Down;
					moving = true;
				}
				if (Input.isKeyPressed(KeyEvent.VK_A)) {
					moveDir = Directions.Left;
					moving = true;
				} else if (Input.isKeyPressed(KeyEvent.VK_D)) {
					moveDir = Directions.Right;
					moving = true;
				}

				if (moving && canMove(moveDir, decorations, passedTime)) {
					moveInDirection(moveDir, passedTime);
				} else {
					// Don't take away energy for trying to move somewhere the
					// player can't
					moving = false;
				}
			}

			if (moving) {
				if (sprinting) {
					sprintEnergyTimer.update(passedTime);
					if (sprintEnergyTimer.query()) {
						changeEnergy(-MOVE_ENERGY_COST);
						sprintEnergyTimer.reset();
					}
				} else if (swimming) {
					swimEnergyTimer.update(passedTime);
					if (swimEnergyTimer.query()) {
						changeEnergy(-MOVE_ENERGY_COST);
						swimEnergyTimer.reset();
					}
				} else {
					walkEnergyTimer.update(passedTime);
					if (walkEnergyTimer.query()) {
						changeEnergy(-MOVE_ENERGY_COST);
						walkEnergyTimer.reset();
					}
				}
			}

			if (Input.isLeftDown() && !leftClickStarted) {
				Point worldPoint = World.screenCoordToWorldCoord(new Point(Input.getMouseX(), Input.getMouseY()), c);
				if (!getHeldItem().checkEmpty()) {
					switch (getHeldItem().getItem().getCategory()) {
						case Tool:
							leftClickStarted = true;
							// Attempting to break some sort of decoration, so
							// check to make sure this is valid
							Decoration decor = world.getDecorationAt(worldPoint.getX(), worldPoint.getY());
							if (decor != null && decor.getBreakingTool() == getHeldItem().getItem()
									&& distanceTo(decor.getCenterX(), decor.getCenterY()) <= interactDistance) {
								isBreaking = true;
								actionTimer = new Timer(decor.getBreakTime());
								actionProgress.setMaxValue(actionTimer.getWaitTime());
								actionProgress.setValue(0);
								clickLoc.setX(Input.getMouseX());
								clickLoc.setY(Input.getMouseY());
								switch (getHeldItem().getItem()) {
									case Axe:
										actionAnimationSpd = CHOPPING_SPD;
										actionAnimationOffsetMin = CHOPPING_OFFSET_MIN;
										actionAnimationOffsetMax = CHOPPING_OFFSET_MAX;
										break;
									case Pickaxe:
										actionAnimationSpd = MINING_SPD;
										actionAnimationOffsetMin = MINING_OFFSET_MIN;
										actionAnimationOffsetMax = MINING_OFFSET_MAX;
										break;
									case Shovel:
										actionAnimationSpd = DIGGING_SPD;
										actionAnimationOffsetMin = DIGGING_OFFSET_MIN;
										actionAnimationOffsetMax = DIGGING_OFFSET_MAX;
										break;
									case Clippers:
										actionAnimationSpd = CLIPPING_SPD;
										actionAnimationOffsetMin = CLIPPING_OFFSET_MIN;
										actionAnimationOffsetMax = CLIPPING_OFFSET_MAX;
										break;
								}
								actionAnimationLerpFactor = 0;
								actionAnimationAngleOffset = actionAnimationOffsetMin;
								actionAnimationDirection = 1;
								return;
							}
							break;

						case Sword:
							// Clicking with a sword; don't care if it's on an enemy (unless already attacking),
							// the sword will swing first, check if it can hit something second

							// Also, don't set leftClickStarted to true so the player can hold click to continue attacking,
							// without having to spam-click
							if (!isAttacking) {
								clickLoc.setX(Input.getMouseX());
								clickLoc.setY(Input.getMouseY());
								actionAnimationSpd = SWINGING_SPD;
								actionAnimationOffsetMin = SWINGING_OFFSET_MIN;
								actionAnimationOffsetMax = SWINGING_OFFSET_MAX;
								actionAnimationLerpFactor = 0;
								actionAnimationAngleOffset = actionAnimationOffsetMin;
								actionAnimationDirection = -1;
								isAttacking = true;
								return;
							}
							break;

						case Food:
							leftClickStarted = true;
							if (energy < maxEnergy) {
								isEating = true;
								eatAngle = facingAngle;
								actionProgress.setMaxValue(eatTimer.getWaitTime());
								return;
							}
							break;
						case Summon:
							leftClickStarted = true;
							world.spawnEntity(getHeldItem().getItem().getEntitySpawned(), getWorldX(), getWorldY(), 1);
							inventory.removeFromSelectedSlot(1);
							break;
					}
				}
			}
			if (Input.isRightDown()) {
				world.attemptGrab(this, World.screenCoordToWorldCoord(Input.getMouseLoc(), c));
			}
			
			updateHotbarSwitching();

			super.face(getFaceAngleTo(new Point(Input.getMouseX(), Input.getMouseY()), c));
		}
		
		if (!getShield().checkEmpty()) {
			updateItemAbility(passedTime, getShield().getItem().getAbility(), true, !lastShield.equals(getShield()));
		} else {
			maxHealthBoostShield = 0;
			maxEnergyBoostShield = 0;
		}
		if (!getHeldItem().checkEmpty()) {
			updateItemAbility(passedTime, getHeldItem().getItem().getAbility(), false, !lastHeldItem.equals(getHeldItem()));
		} else {
			maxHealthBoostHand = 0;
			maxEnergyBoostHand = 0;
		}
		
		setMaxHealth(DEFAULT_HEALTH + maxHealthBoostHand + maxHealthBoostShield);
		setMaxEnergy(DEFAULT_ENERGY + maxEnergyBoostHand + maxEnergyBoostShield);
		
		lastHeldItem = getHeldItem();
		lastShield = getShield();
	}

	public void drawGui(Graphics2D g, Camera c) {
		if (isBreaking || isEating) {
			actionProgress.draw(g);
			g.setFont(BREAKTEXT_FONT);
			g.setColor(BREAKBAR_FILL);
			g.drawString(getHeldItem().getItem().getActionText() + "...", actionProgress.getX(), actionProgress.getY());
		}
	}

	public void moveInDirection(Directions direction, long passedTime) {
		switch (direction) {
			case Up:
				decreaseY(passedTime);
				break;
			case Down:
				increaseY(passedTime);
				break;
			case Left:
				decreaseX(passedTime);
				break;
			case Right:
				increaseX(passedTime);
				break;
		}
	}

	public boolean canMove(Directions direction, ArrayList<Decoration> decorations, long passedTime) {
		moveInDirection(direction, passedTime);
		for (int i = 0; i < decorations.size(); i++) {
			if (decorations.get(i).intersects(this)) {
				if (decorations.get(i).canCollide()) {
					while (decorations.get(i).intersects(this)) {
						moveInDirection(direction.opposite(), passedTime);
					}
					return false;
				}
			}
		}
		moveInDirection(direction.opposite(), passedTime);
		return true;
	}
	
	private void updateHotbarSwitching() {
		// First see if the use is scrolling, since using hotkeys overrides that
		int selectedSlot = inventory.getSelectedSlot();
		if (Input.getScroll() > 0) {
			if (selectedSlot + 1 < Inventory.INV_COLS) {
				selectedSlot++;
			} else {
				selectedSlot = 0;
			}
			inventory.setSelectedSlot(selectedSlot);
		} else if (Input.getScroll() < 0) {
			if (selectedSlot - 1 >= 0) {
				selectedSlot--;
			} else {
				selectedSlot = Inventory.INV_COLS - 1;
			}
			inventory.setSelectedSlot(selectedSlot);
		}
		
		// Check if switching item
		// Very bad way of doing this; will clean up if time
		if (Input.isKeyPressed(KeyEvent.VK_1)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(0);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_2)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(1);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_3)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(2);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_4)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(3);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_5)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(4);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_6)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(5);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_7)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(6);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_8)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(7);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_9)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(8);
			}
			hotbarKeyPrevDown = true;
		} else if (Input.isKeyPressed(KeyEvent.VK_0)) {
			if (!hotbarKeyPrevDown) {
				inventory.setSelectedSlot(9);
			}
			hotbarKeyPrevDown = true;
		} else {
			hotbarKeyPrevDown = false;
		}
	}

	private void updateItemAbility(long passedTime, ItemAbility ability, boolean isShield, boolean isNew) {
		if (isNew) {
			if (ability.getHealthHealUnitTime() > 0) {
				if (isShield) {
					itemHealTimerShield = new Timer(ability.getHealthHealUnitTime());
				} else {
					itemHealTimerHand = new Timer(ability.getHealthHealUnitTime());
				}
			}
			if (ability.getEnergyHealUnitTime() > 0) {
				if (isShield) {
					itemEnergyTimerShield = new Timer(ability.getEnergyHealUnitTime());
				} else {
					itemEnergyTimerHand = new Timer(ability.getEnergyHealUnitTime());
				}
			}
			if (isShield) {
				maxHealthBoostShield = ability.getMaxHealthAdded();
				maxEnergyBoostShield = ability.getMaxEnergyAdded();
			} else {
				maxHealthBoostHand = ability.getMaxHealthAdded();
				maxEnergyBoostHand = ability.getMaxEnergyAdded();
			}
		}
		
		if (ability.getHealthHealed() > 0) {
			if (isShield) {
				itemHealTimerShield.update(passedTime);
				if (itemHealTimerShield.query()) {
					itemHealTimerShield.reset();
					changeHealth(ability.getHealthHealed());
				}
			} else {
				itemHealTimerHand.update(passedTime);
				if (itemHealTimerHand.query()) {
					itemHealTimerHand.reset();
					changeHealth(ability.getHealthHealed());
				}
			}
		}
		if (ability.getEnergyHealed() > 0) {
			if (isShield) {
				itemEnergyTimerShield.update(passedTime);
				if (itemEnergyTimerShield.query()) {
					itemEnergyTimerShield.reset();
					changeHealth(ability.getEnergyHealed());
				}
			} else {
				itemEnergyTimerHand.update(passedTime);
				if (itemEnergyTimerHand.query()) {
					itemEnergyTimerHand.reset();
					changeHealth(ability.getEnergyHealed());
				}
			}
		}
	}
	
	public boolean isActing() {
		return isEating || isBreaking;
	}

	public ArrayList<ItemStack> getDrops() {
		ArrayList<ItemStack> drops = new ArrayList<>();
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				if (!inventory.getInvItem(i, j).checkEmpty()) {
					drops.add(inventory.getInvItem(i, j));
				}
			}
		}
		return drops;
	}

	public int getCoinDrops() {
		return coins;
	}

	public double getEnergy() {
		return energy;
	}

	public double getMaxEnergy() {
		return maxEnergy;
	}

	public double getEnergyPercent() {
		return energy / maxEnergy * 100.0;
	}
	
	public void setEnergy(double value) {
		energy = value;
	}

	public void setMaxEnergy(double value) {
		double percentage = energy / maxEnergy;
		maxEnergy = value;
		energy = percentage * value;
	}

	public PlayerData getPlayerData() {
		return new PlayerData(getHealthPercent(), getEnergyPercent(), getWorldX(), getWorldY(), getInventory(), level, xp, coins);
	}

	public void setPlayerData(PlayerData playerData) {
		setWorldX(playerData.getX());
		setWorldY(playerData.getY());
		setHealth(playerData.getHealthPercent() / 100.0 * getMaxHealth());
		setInventory(playerData.getInventory());

		energy = playerData.getEnergyPercent() / 100.0 * maxEnergy;
		xp = playerData.getXp();
		level = playerData.getLevel();
		coins = playerData.getCoins();
	}

	public void changeEnergy(double amount) {
		energy += amount;
		if (energy < 0) {
			energy = 0;
		}
		if (energy > maxEnergy) {
			energy = maxEnergy;
		}
	}

	public int getXp() {
		return xp;
	}

	public int getXpToLevelUp(int currentLevel) {
		return currentLevel * 50 + 1000;
	}

	public int getLevel() {
		return level;
	}

	public int getCoins() {
		return coins;
	}

	public void addXp(int amount) {
		xp += amount;
		if (xp >= getXpToLevelUp(level)) {
			xp -= getXpToLevelUp(level);
			level++;
			// TODO: Announce to the player that they've leveled up
		}
	}

	public void addCoins(int amount) {
		coins += amount;
	}
	
	public void setCoins(int value) {
		coins = value;
	}
	public void setXp(int value) {
		xp = value;
	}
	public void setLevel(int value) {
		level = value;
	}
}
