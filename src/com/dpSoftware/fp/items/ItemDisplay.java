package com.dpSoftware.fp.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.dpSoftware.fp.ui.Image;
import com.dpSoftware.fp.ui.InterfaceTheme;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.ui.Rectangle;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.Input;
import com.dpSoftware.fp.util.ResourceLoader;

public class ItemDisplay {

	private int x;
	private int y;
	private int width;
	private int height;
	private ItemStack itemStack;
	private Rectangle boundingBox;
	
	private boolean canDrawDesc;
	private final int winWidth;
	private final int winHeight;
	private ArrayList<String> descLines;
	private static final int DESCBOX_WIDTH = 235;
	private static final int DESCBOX_HEIGHT_PER_LINE = 20;
	private int descboxHeight;
	private static final int DESCBOX_BORDER_WIDTH = 5;
	private static final int DESC_INITIAL_SPACING = 3;

	private Image img;
	private String amountText;
	private Font font;
	private boolean hasItem;
	private static final int FONT_SIZE = 15;
	private static final int AMOUNT_SPACING = 5;
	private static final int AMOUNT_SHADOW_SPACING = 1;

	public ItemDisplay(int x, int y, int width, int height, ItemStack itemStack, boolean canDrawDescription,
			int winWidth, int winHeight) {
		this.x = x;
		this.y = y;
		this.winWidth = winWidth;
		this.winHeight = winHeight;
		this.width = width;
		this.height = height;
		this.canDrawDesc = canDrawDescription;
		boundingBox = new Rectangle(x, y, width, height);
		img = new Image(x, y, width, height, null);
		font = new Font("Calibri", Font.PLAIN, FONT_SIZE);
		setItemStack(itemStack);
		
		Items item = itemStack.getItem();
		// No point in initializing the description if it will never be used
		if (canDrawDesc && item != null) {
			updateDescription(item);
		} 
	}

	public void draw(Graphics2D g) {
		if (hasItem) {
			img.draw(g);

			// Only draw the amount if this item can be stacked
			if (itemStack.getItem().isStackable()) {
				g.setFont(font);

				int amountX = x + width - g.getFontMetrics().stringWidth(amountText) - AMOUNT_SPACING;
				int amountY = y + height - FONT_SIZE / 2;

				// Draw a shadow so the text appears 3D
				g.setColor(new Color(200, 200, 200));
				g.drawString(amountText, amountX + AMOUNT_SHADOW_SPACING, amountY + AMOUNT_SHADOW_SPACING);
				g.setColor(Color.white);
				g.drawString(amountText, amountX, amountY);
			}
		}
	}
	public void drawDesc(Graphics2D g) {
		if (canDrawDesc && boundingBox.includes(Input.getMouseLoc()) && itemStack.getItem() != null) {
			int drawX = 0;
			int drawY = 0;
			if (Input.getMouseX() + DESCBOX_WIDTH > winWidth) {
				drawX = Input.getMouseX() - DESCBOX_WIDTH;
			} else {
				drawX = Input.getMouseX();
			}
			if (Input.getMouseY() + descboxHeight > winHeight) {
				drawY = Input.getMouseY() - descboxHeight;
			} else {
				drawY = Input.getMouseY();
			}
			
			g.setColor(InterfaceTheme.INTERFACE_BG);
			g.fillRect(drawX, drawY, DESCBOX_WIDTH, descboxHeight);
			g.setColor(InterfaceTheme.INTERFACE_BORDER);
			g.setStroke(new BasicStroke(DESCBOX_BORDER_WIDTH));
			g.drawRect(drawX, drawY, DESCBOX_WIDTH, descboxHeight);
			
			int descX = drawX + DESCBOX_BORDER_WIDTH + DESC_INITIAL_SPACING;
			int descY = drawY + DESCBOX_BORDER_WIDTH + DESC_INITIAL_SPACING + DESCBOX_HEIGHT_PER_LINE;
			g.setColor(InterfaceTheme.INTERFACE_TEXT);
			g.setFont(InterfaceTheme.MED_FONT);
			g.drawString(itemStack.getItem().getName(), descX, descY);
			descY += DESCBOX_HEIGHT_PER_LINE;
			g.setFont(InterfaceTheme.MED_FONT_NOBOLD);
			for (int i = 0; i < descLines.size(); i++) {
				g.drawString(descLines.get(i), descX, descY);
				descY += DESCBOX_HEIGHT_PER_LINE;
			}
			g.setColor(itemStack.getItem().getRarity().getColor());
			g.setFont(InterfaceTheme.MED_FONT);
			g.drawString(itemStack.getItem().getRarity().toString(), descX, descY);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		if (img != null) {
			img.setX(x);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		if (img != null) {
			img.setY(y);
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		if (img != null) {
			img.setWidth(width);
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		if (img != null) {
			img.setHeight(height);
		}
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		// Clone the ItemStack to prevent bugs from reference issues
		this.itemStack = itemStack.clone();

		if (itemStack.getItem() != null) {
			img.setBufferedImg(Images.loadImage(itemStack.getItem().getTextureDirectory()));
			amountText = "x" + itemStack.getAmount();
			hasItem = true;
			if (canDrawDesc) {
				updateDescription(itemStack.getItem());
			}
		} else {
			hasItem = false;
		}
	}
	public void trySetItemStack(ItemStack itemStack) {
		if (!this.itemStack.equals(itemStack)) {
			setItemStack(itemStack);
		}
	}
	
	private void updateDescription(Items item) {
		descLines = new ArrayList<>();
		if (item.getCategory() != ItemCategories.Sword && item.getCategory() != ItemCategories.Shield &&
				item.getCategory() != ItemCategories.Tool) {
			descLines.add("Max Stack Size: " + item.getMaxStackSize());
		}
		switch (item.getCategory()) {
			case Sword:
				descLines.add("+" + item.getBaseDamage() + " Damage");
				descLines.add("+" + item.getBaseDefense() + " Defense");
				break;
			case Shield:
				descLines.add("+" + item.getBaseDefense() + " Defense");
				descLines.add("+" + item.getBaseDamage() + " Damage");
				break;
			case Tool:
				descLines.add("A useful tool");
				break;
			case Food:
				descLines.add("+" + item.getEnergyHealed() + " Energy");
				descLines.add("+" + item.getHealthHealed() + " Health");
				break;
			case Summon:
				descLines.add("Summons the " + item.getEntitySpawned().getName());
				break;
		}
		descLines.addAll(item.getAbility().getDescription());
		
		descboxHeight = DESCBOX_HEIGHT_PER_LINE * (descLines.size() + 3) + DESCBOX_BORDER_WIDTH * 2;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
	
}
