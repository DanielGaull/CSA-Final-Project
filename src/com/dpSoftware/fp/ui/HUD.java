package com.dpSoftware.fp.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.dpSoftware.fp.crafting.CraftListener;
import com.dpSoftware.fp.crafting.CraftingInterface;
import com.dpSoftware.fp.crafting.CraftingRecipe;
import com.dpSoftware.fp.crafting.CraftingRecipeDisplay;
import com.dpSoftware.fp.crafting.CraftingRecipes;
import com.dpSoftware.fp.entity.Creature;
import com.dpSoftware.fp.entity.PlayerEntity;
import com.dpSoftware.fp.items.Inventory;
import com.dpSoftware.fp.items.InventoryDisplay;
import com.dpSoftware.fp.items.ItemDisplay;
import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.Input;

public class HUD implements CraftListener {

	// The player who's stats show up in the HUD
	private PlayerEntity player;
	private final int winWidth;
	private final int winHeight;

	private InventoryDisplay invDisplay;

	private ProgressBar healthBar;
	private static final int HPBAR_X_SPACING = 5;
	private static final int HPBAR_Y_SPACING = 15;
	private static final Color HPBAR_BORDER = new Color(127, 0, 0);
	private static final Color HPBAR_FILL = new Color(175, 0, 0);
	private static final Color HPBAR_BG = new Color(150, 0, 0);
	private static final Color HPBAR_TEXT = new Color(242, 194, 194);
	
	private ProgressBar energyBar;
	private static final int ENBAR_X_SPACING = HPBAR_X_SPACING;
	private static final int ENBAR_Y_SPACING = 5;
	private static final Color ENBAR_BORDER = new Color(163, 106, 0);
	private static final Color ENBAR_FILL = new Color(219, 143, 0);
	private static final Color ENBAR_BG = new Color(186, 121, 0);
	private static final Color ENBAR_TEXT = new Color(240, 219, 180);

	private ProgressBar xpBar;
	private static final int XPBAR_X_SPACING = HPBAR_X_SPACING;
	private static final int XPBAR_Y_SPACING = HPBAR_Y_SPACING;
	private static final Color XPBAR_BORDER = new Color(2, 51, 64);
	private static final Color XPBAR_FILL = new Color(5, 144, 179);
	private static final Color XPBAR_BG = new Color(4, 101, 125);
	private static final Color XPBAR_TEXT = new Color(178, 211, 219);
	
	private static final int PROGRESSBAR_WIDTH = 150;
	private static final int PROGRESSBAR_HEIGHT = 25;
	private static final Font PROGRESSBAR_FONT = new Font("Arial", Font.PLAIN, 15);
	
	private final int coinboxX;
	private final int coinboxY;
	private static final int COINBOX_WIDTH = 100;
	private static final int COINBOX_HEIGHT = 40;
	private static final Color COINBOX_COLOR = new Color(140, 117, 0); 
	private static final Color COINTEXT_COLOR = new Color(255, 252, 240);
	private static final Color COINBOX_BORDER = new Color(107, 89, 0);
	private static final Font COINTEXT_FONT = new Font("Arial", Font.PLAIN, 25);
	private static final int COINBOX_X_SPACING = HPBAR_X_SPACING;
	private static final int COINBOX_Y_SPACING = HPBAR_Y_SPACING;
	private static final int COINTEXT_X_SPACING = 10;
	private static final int COINTEXT_Y_SPACING = 11;
	private Image coinImg;
	
	private final int defboxX;
	private final int defboxY;
	private final int attboxX;
	private final int attboxY;
	private static final int STATBOX_WIDTH = 50;
	private static final int STATBOX_HEIGHT = 50;
	private static final Color STATBOX_COLOR = new Color(80, 80, 80); 
	private static final Color STATTEXT_COLOR = new Color(255, 252, 240);
	private static final Color STATBOX_BORDER = new Color(43, 43, 43);
	private static final int STATBOX_BORDER_WIDTH = 4;
	private static final Font STATTEXT_FONT = new Font("Arial", Font.PLAIN, 25);
	private static final int STATIMG_SIZE = 40;
	private static final int STATBOX_X_SPACING = HPBAR_X_SPACING;
	private static final int STATBOX_Y_SPACING = HPBAR_Y_SPACING;
	private static final int STATTEXT_Y_SPACING = 11;
	private Image defImg;
	private Image attImg;
	
	private static final int INV_Y_SPACING = 15;
	private static final int INV_SQUARE_SIZE = 50;
	private static final int INV_SQUARE_BORDER_WIDTH = 4;
	private static final int INV_SQUARE_BORDER_WIDTH_SEL = 6;
	private static final int INV_SQUARE_SEL_SPACING = INV_SQUARE_BORDER_WIDTH_SEL - INV_SQUARE_BORDER_WIDTH;
	private static final Color INV_SQUARE_BORDER = new Color(65, 65, 65);
	private static final Color INV_SQUARE_BORDER_SEL = new Color(20, 225, 20);
	private static final Color INV_SQUARE_FILL = new Color(100, 100, 100);
	
	private static final Font HELD_ITEM_FONT = new Font("Arial", Font.BOLD, 25);
	private static final int HELD_ITEM_BOX_WIDTH = 200;
	private static final int HELD_ITEM_BOX_HEIGHT = 35;
	private static final int HELD_ITEM_BOX_SPACING = 10;
	private static final int HELD_ITEM_BOX_BORDER_WIDTH = 4;
	private static final Color HELD_ITEM_BOX_BORDER = new Color(50, 50, 50);
	private static final Color HELD_ITEM_BOX_FILL = new Color(75, 75, 75);
	private static final Color HELD_ITEM_TEXT_COLOR = new Color(220, 220, 220);
	private final int heldItemBoxX;
	private static final int HELD_ITEM_BOX_Y = INV_Y_SPACING + INV_SQUARE_SIZE + HELD_ITEM_BOX_SPACING;

	private CraftingInterface craftingInterface;
	private static final int CRINT_SPACING = 105;
	private static final int CRINT_Y = INV_Y_SPACING + (INV_SQUARE_SIZE * Inventory.INV_ROWS) + CRINT_SPACING;
	
	private static final String COIN_FILE_PATH = "misc\\coin";
	private static final String DEF_FILE_PATH = "ui\\deficon";
	private static final String ATT_FILE_PATH = "ui\\atticon";

	private boolean inventoryOpen;
	private boolean invKeyPrevPressed;

	public HUD(int winWidth, int winHeight) {
		this.winWidth = winWidth;
		this.winHeight = winHeight;

		inventoryOpen = false;
		
		heldItemBoxX = winWidth / 2 - HELD_ITEM_BOX_WIDTH  / 2;

		invDisplay = new InventoryDisplay(winWidth, winHeight, INV_SQUARE_SIZE, INV_SQUARE_BORDER, INV_SQUARE_FILL,
				INV_SQUARE_BORDER_WIDTH, INV_Y_SPACING);
		invKeyPrevPressed = false;

		healthBar = new ProgressBar(HPBAR_X_SPACING, HPBAR_Y_SPACING, PROGRESSBAR_WIDTH, PROGRESSBAR_HEIGHT,
				HPBAR_BORDER, HPBAR_FILL, HPBAR_BG, 0, 0, PROGRESSBAR_FONT, HPBAR_TEXT, "Health", true);
		energyBar = new ProgressBar(ENBAR_X_SPACING, healthBar.getY() + healthBar.getHeight() + ENBAR_Y_SPACING, 
				PROGRESSBAR_WIDTH, PROGRESSBAR_HEIGHT, ENBAR_BORDER, ENBAR_FILL, ENBAR_BG, 0, 
				0, PROGRESSBAR_FONT, ENBAR_TEXT, "Energy", true);
		xpBar = new ProgressBar(winWidth - PROGRESSBAR_WIDTH - XPBAR_X_SPACING, XPBAR_Y_SPACING, 
				PROGRESSBAR_WIDTH, PROGRESSBAR_HEIGHT, XPBAR_BORDER, XPBAR_FILL, XPBAR_BG, 0, 
				0, PROGRESSBAR_FONT, XPBAR_TEXT, "XP (L0)", true);

		craftingInterface = new CraftingInterface(CraftingRecipes.getRecipes(), winWidth / 2 - CraftingRecipeDisplay.WIDTH / 2, 
				CRINT_Y, this, winWidth, winHeight);
		
		int coinImgSpacing = COINBOX_HEIGHT / 2 - STATIMG_SIZE / 2;
		coinboxX = winWidth - COINBOX_WIDTH - COINBOX_X_SPACING;
		coinboxY = xpBar.getY() + xpBar.getHeight() + COINBOX_Y_SPACING;
		coinImg = new Image(coinboxX + coinImgSpacing, coinboxY + coinImgSpacing, STATIMG_SIZE, STATIMG_SIZE,
				Images.loadImage(COIN_FILE_PATH));
		
		attboxX = coinboxX;
		attboxY = coinboxY + COINBOX_HEIGHT + STATBOX_Y_SPACING;
		defboxX = attboxX + STATBOX_WIDTH + STATBOX_X_SPACING;
		defboxY = attboxY;
		attImg = new Image(attboxX + STATBOX_WIDTH / 2 - STATIMG_SIZE / 2, attboxY + STATBOX_HEIGHT / 2 - STATIMG_SIZE / 2,
				STATIMG_SIZE, STATIMG_SIZE, Images.loadImage(ATT_FILE_PATH));
		defImg = new Image(defboxX + STATBOX_WIDTH / 2 - STATIMG_SIZE / 2, defboxY + STATBOX_HEIGHT / 2 - STATIMG_SIZE / 2,
				STATIMG_SIZE, STATIMG_SIZE, Images.loadImage(DEF_FILE_PATH));
	}
	public void setPlayer(PlayerEntity player) {
		this.player = player;
		invDisplay.setCreature(player);
	}

	public void update(long passedTime) {
		if (inventoryOpen) {
			craftingInterface.update(passedTime);
		}
		invDisplay.update(inventoryOpen);

		if (Input.isKeyPressed(KeyEvent.VK_Q) && !invKeyPrevPressed && !player.isActing() && !craftingInterface.isTyping()) {
			invKeyPrevPressed = true;
			inventoryOpen = !inventoryOpen;

			if (!inventoryOpen) {
				// Just closed the inventory
				// If the player was dragging an item around and then closed the
				// inventory, just place it back
				// in the inventory (or drop it if there's no room)
				if (invDisplay.isMovingItem()) {
					player.pickupItemStack(invDisplay.getMovingItem());
					invDisplay.resetMovingItem();
				}
			} else {
				// Just opened the inventory
				// Need to resort recipes
				craftingInterface.reset();
				craftingInterface.sort(player.getInventory());
			}
		} else if (!Input.isKeyPressed(KeyEvent.VK_Q)) {
			invKeyPrevPressed = false;
		}

		healthBar.setMaxValue(player.getMaxHealth());
		healthBar.setValue(player.getHealth());
		energyBar.setMaxValue(player.getMaxEnergy());
		energyBar.setValue(player.getEnergy());
		xpBar.setMaxValue(player.getXpToLevelUp(player.getLevel()));
		xpBar.setValue(player.getXp());
		xpBar.setValueName("XP (L" + player.getLevel() + ")");
	}

	public void draw(Graphics2D g) {
		healthBar.draw(g);
		energyBar.draw(g);
		xpBar.draw(g);
		
		g.setColor(COINBOX_COLOR);
		g.fillRect(coinboxX, coinboxY, COINBOX_WIDTH, COINBOX_HEIGHT);
		g.setStroke(new BasicStroke(STATBOX_BORDER_WIDTH));
		g.setColor(COINBOX_BORDER);
		g.drawRect(coinboxX, coinboxY, COINBOX_WIDTH, COINBOX_HEIGHT);
		coinImg.draw(g);
		g.setColor(COINTEXT_COLOR);
		g.setFont(COINTEXT_FONT);
		FontMetrics metrics = g.getFontMetrics();
		String coinString = Integer.toString(player.getCoins()); 
		g.drawString(coinString, coinboxX + COINBOX_WIDTH - metrics.stringWidth(coinString) - COINTEXT_X_SPACING, 
				coinImg.getY() + coinImg.getHeight() - COINTEXT_Y_SPACING);
		
		g.setColor(STATBOX_COLOR);
		g.fillRect(attboxX, attboxY, STATBOX_WIDTH, STATBOX_HEIGHT);
		g.fillRect(defboxX, defboxY, STATBOX_WIDTH, STATBOX_HEIGHT);
		g.setStroke(new BasicStroke(STATBOX_BORDER_WIDTH));
		g.setColor(STATBOX_BORDER);
		g.drawRect(attboxX, attboxY, STATBOX_WIDTH, STATBOX_HEIGHT);
		g.drawRect(defboxX, defboxY, STATBOX_WIDTH, STATBOX_HEIGHT);
		attImg.draw(g);
		defImg.draw(g);
		g.setColor(STATTEXT_COLOR);
		g.setFont(STATTEXT_FONT);
		metrics = g.getFontMetrics();
		String attString = Integer.toString(player.getDamage()); 
		g.drawString(attString, attboxX + STATBOX_WIDTH / 2 - metrics.stringWidth(attString) / 2, 
				attImg.getY() + attImg.getHeight() - STATTEXT_Y_SPACING);
		String defString = Integer.toString(player.getDefense()); 
		g.drawString(defString, defboxX + STATBOX_WIDTH / 2 - metrics.stringWidth(defString) / 2, 
				defImg.getY() + defImg.getHeight() - STATTEXT_Y_SPACING);

		invDisplay.draw(g, inventoryOpen);
		if (inventoryOpen) {
			craftingInterface.draw(g);
		} else {
			// Draw a border around the selected slot
			int selX = winWidth / 2 - (INV_SQUARE_SIZE * Inventory.HOTBAR_SLOTS) / 2
					+ player.getSelectedSlot() * INV_SQUARE_SIZE - INV_SQUARE_SEL_SPACING;
			int selY = INV_Y_SPACING - INV_SQUARE_SEL_SPACING;
			g.setStroke(new BasicStroke(INV_SQUARE_BORDER_WIDTH_SEL));
			g.setColor(INV_SQUARE_BORDER_SEL);
			g.drawRect(selX, selY, INV_SQUARE_SEL_SPACING + INV_SQUARE_SIZE, INV_SQUARE_SEL_SPACING + INV_SQUARE_SIZE);
			
			if (!player.getHeldItem().checkEmpty()) {
				g.setColor(HELD_ITEM_BOX_FILL);
				g.fillRect(heldItemBoxX, HELD_ITEM_BOX_Y, HELD_ITEM_BOX_WIDTH, HELD_ITEM_BOX_HEIGHT);
				g.setStroke(new BasicStroke(HELD_ITEM_BOX_BORDER_WIDTH));
				g.setColor(HELD_ITEM_BOX_BORDER);
				g.drawRect(heldItemBoxX, HELD_ITEM_BOX_Y, HELD_ITEM_BOX_WIDTH, HELD_ITEM_BOX_HEIGHT);
				g.setFont(HELD_ITEM_FONT);
				g.setColor(HELD_ITEM_TEXT_COLOR);
				FontMetrics fm = g.getFontMetrics();
				g.drawString(player.getHeldItem().getItem().getName(), 
						heldItemBoxX + HELD_ITEM_BOX_WIDTH / 2 - fm.stringWidth(player.getHeldItem().getItem().getName()) / 2, 
						HELD_ITEM_BOX_Y + HELD_ITEM_BOX_HEIGHT / 2 + fm.getHeight() / 2 - HELD_ITEM_BOX_SPACING / 2);
			}
		}
	}

	public boolean isInventoryOpen() {
		return inventoryOpen;
	}

	public void craft(CraftingRecipe recipe) {
		if (player.canCraft(recipe)) {
			player.craft(recipe);
		}
	}

	public boolean canCraft(CraftingRecipe recipe) {
		return player.canCraft(recipe);
	}
}
