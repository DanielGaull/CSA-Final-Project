package com.dpSoftware.fp.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.dpSoftware.fp.entity.Creature;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Input;

public class InventoryDisplay {

	private ItemDisplay[][] itemDisplays;
	private ItemDisplay shieldDisplay;
	private final int slotSize;
	private final Color border;
	private final Color fill;
	private final int borderSize;
	private ItemDisplay movingSlot; // The ItemDisplay that the player is moving
									// around - only used when moving an item in
									// the inventory
	private boolean isMovingItem;
	private boolean mousePrevDown;

	private Creature creature;
	private final int winWidth;
	private final int winHeight;
	public static final int SHIELD_SLOT_SPACING = 15;

	public InventoryDisplay(int winWidth, int winHeight, int slotSize, Color border, Color fill, int borderSize,
			int ySpacing) {
		this.winWidth = winWidth;
		this.winHeight = winHeight;
		this.slotSize = slotSize;
		this.border = border;
		this.fill = fill;
		this.borderSize = borderSize;

		itemDisplays = new ItemDisplay[Inventory.INV_ROWS][Inventory.INV_COLS];
		int startX = winWidth / 2 - (slotSize * Inventory.INV_COLS) / 2;
		int startY = ySpacing;
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				itemDisplays[i][j] = new ItemDisplay(startX + j * slotSize, startY + i * slotSize, slotSize, slotSize,
						ItemStack.empty(), true, winWidth, winHeight);
			}
		}

		shieldDisplay = new ItemDisplay(startX + (slotSize * Inventory.INV_COLS) + SHIELD_SLOT_SPACING, startY,
				slotSize, slotSize, ItemStack.empty(), true, winWidth, winHeight);

		isMovingItem = false;
		mousePrevDown = false;
		movingSlot = new ItemDisplay(0, 0, slotSize, slotSize, ItemStack.empty(), false, winWidth, winHeight);
	}

	public void setCreature(Creature creature) {
		this.creature = creature;
	}

	public void update(boolean canMoveItems) {
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				itemDisplays[i][j].trySetItemStack(creature.getInventory().getInvItem(i, j));
			}
		}
		shieldDisplay.trySetItemStack(creature.getShield());

		if (isMovingItem) {
			movingSlot.setX(Input.getMouseX() - movingSlot.getWidth() / 2);
			movingSlot.setY(Input.getMouseY() - movingSlot.getHeight() / 2);

			if (Input.isKeyPressed(KeyEvent.VK_D)) {
				creature.dropItem(movingSlot.getItemStack());
				movingSlot.setItemStack(ItemStack.empty());
				isMovingItem = false;
				return;
			}

			if (Input.isLeftDown() && !mousePrevDown) {
				int mx = Input.getMouseX();
				int my = Input.getMouseY();
				// Need to change the item stack of the slot we just placed this
				// item in
				int cRow = getClickedRow(mx, my);
				int cCol = getClickedCol(mx, my);
				if (cRow >= 0 && cCol >= 0) {
					ItemStack tempStack = creature.getInventory().getInvItem(cRow, cCol);
					if (movingSlot.getItemStack().getItem() == tempStack.getItem() && tempStack.getItem().getMaxStackSize() > 1) {
						// We are clicking on a stack that is the same item as the one being held, so attempt to combine them
						int placedAmount = movingSlot.getItemStack().getAmount() + tempStack.getAmount();
						if (placedAmount > tempStack.getItem().getMaxStackSize()) {
							int leftovers = placedAmount - tempStack.getItem().getMaxStackSize();
							creature.getInventory().setSlot(cRow, cCol, new ItemStack(tempStack.getItem(), tempStack.getItem().getMaxStackSize()));
							movingSlot.setItemStack(new ItemStack(tempStack.getItem(), leftovers));
						} else {
							creature.getInventory().setSlot(cRow, cCol, new ItemStack(tempStack.getItem(), placedAmount));
							movingSlot.setItemStack(ItemStack.empty());
							isMovingItem = false;
						}
					} else {
						creature.getInventory().setSlot(cRow, cCol, movingSlot.getItemStack());
						movingSlot.setItemStack(tempStack);
						if (tempStack.checkEmpty()) {
							isMovingItem = false;
						}
					}
				} else if (clickedOnShield(mx, my)) {
					// Only allow empty or no shield in this slot
					if (movingSlot.getItemStack().checkEmpty()
							|| movingSlot.getItemStack().getItem().getCategory() == ItemCategories.Shield) {
						ItemStack tempStack = creature.getShield();
						creature.getInventory().setShield(movingSlot.getItemStack());
						movingSlot.setItemStack(tempStack);
						if (tempStack.checkEmpty()) {
							isMovingItem = false;
						}
					}
				}
			}
		} else if (canMoveItems) {
			if (Input.isLeftDown() && !mousePrevDown) {
				// Find the slot that is being clicked & pick it up
				int mx = Input.getMouseX();
				int my = Input.getMouseY();
				ItemDisplay clickedSlot = getClickedSlot(mx, my);
				if (clickedSlot != null && !clickedSlot.getItemStack().checkEmpty()) {
					movingSlot.setItemStack(clickedSlot.getItemStack());
					creature.getInventory().setSlot(getClickedRow(mx, my), getClickedCol(mx, my), ItemStack.empty());
					isMovingItem = true;
				} else if (clickedOnShield(mx, my)) {
					movingSlot.setItemStack(creature.getShield());
					creature.getInventory().setShield(ItemStack.empty());
					isMovingItem = true;
				}
				if (isMovingItem) {
					movingSlot.setX(mx - movingSlot.getWidth() / 2);
					movingSlot.setY(my - movingSlot.getHeight() / 2);
				}
			}
		}
		if (Input.isLeftDown())
			mousePrevDown = true;
		else
			mousePrevDown = false;
	}

	public void draw(Graphics2D g, boolean invOpen) {
		int maxRows = Inventory.INV_ROWS;
		if (!invOpen) {
			maxRows = 1;
		}
		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				g.setColor(fill);
				g.fillRect(itemDisplays[i][j].getX(), itemDisplays[i][j].getY(), slotSize, slotSize);
				g.setColor(border);
				g.setStroke(new BasicStroke(borderSize));
				g.drawRect(itemDisplays[i][j].getX(), itemDisplays[i][j].getY(), slotSize, slotSize);
				itemDisplays[i][j].draw(g);
			}
		}
		int shieldSlotX = itemDisplays[0][Inventory.INV_COLS - 1].getX() + slotSize + SHIELD_SLOT_SPACING;
		int shieldSlotY = itemDisplays[0][0].getY();
		g.setColor(fill);
		g.fillRect(shieldSlotX, shieldSlotY, slotSize, slotSize);
		g.setColor(border);
		g.drawRect(shieldSlotX, shieldSlotY, slotSize, slotSize);
		shieldDisplay.draw(g);

		if (isMovingItem && invOpen) {
			movingSlot.draw(g);
		}

		if (invOpen && !isMovingItem) {
			for (int i = 0; i < maxRows; i++) {
				for (int j = 0; j < Inventory.INV_COLS; j++) {
					itemDisplays[i][j].drawDesc(g);
				}
			}
			shieldDisplay.drawDesc(g);
		}
	}

	public boolean isMovingItem() {
		return isMovingItem;
	}

	public ItemStack getMovingItem() {
		return movingSlot.getItemStack();
	}

	public void resetMovingItem() {
		movingSlot.setItemStack(ItemStack.empty());
		isMovingItem = false;
	}

	private ItemDisplay getClickedSlot(int mouseX, int mouseY) {
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				if (itemDisplays[i][j].getRectangle().includes(new Point(mouseX, mouseY))) {
					return itemDisplays[i][j];
				}
			}
		}
		return null;
	}

	private int getClickedRow(int mouseX, int mouseY) {
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				if (itemDisplays[i][j].getRectangle().includes(new Point(mouseX, mouseY))) {
					return i;
				}
			}
		}
		return -1;
	}

	private int getClickedCol(int mouseX, int mouseY) {
		for (int i = 0; i < Inventory.INV_ROWS; i++) {
			for (int j = 0; j < Inventory.INV_COLS; j++) {
				if (itemDisplays[i][j].getRectangle().includes(new Point(mouseX, mouseY))) {
					return j;
				}
			}
		}
		return -1;
	}

	private boolean clickedOnShield(int mouseX, int mouseY) {
		return new Rectangle(shieldDisplay.getX(), shieldDisplay.getY(), shieldDisplay.getWidth(),
				shieldDisplay.getHeight()).includes(mouseX, mouseY);
	}
}
