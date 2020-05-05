package com.dpSoftware.fp.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.dpSoftware.fp.items.ItemCategories;
import com.dpSoftware.fp.items.ItemDisplay;
import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.BufferedImageUtils;
import com.dpSoftware.fp.util.Camera;
import com.dpSoftware.fp.util.GraphicsUtils;
import com.dpSoftware.fp.util.Images;
import com.dpSoftware.fp.util.MathUtils;
import com.dpSoftware.fp.world.World;

public abstract class Humanoid extends Creature {

	private Color bodyColor;
	private Color hand1Color;
	private Color hand2Color;

	private boolean bodyVisible;
	private boolean handsVisible;

	private final int size;
	private final int handSize;
	private static final double HAND_SIZE_RATIO = 0.4;
	private final int handOffsetX;
	private final int handOffsetY;
	private final int handOffsetClose;
	private static final double HAND_OFFSET_X_RATIO = 0.4;
	private static final double HAND_OFFSET_Y_RATIO = 0.46;
	private static final double HAND_OFFSET_CLOSE_RATIO = 0.2;
	private double handAngleOffset;

	private int bodyDrawX;
	private int bodyDrawY;

	private int hand1X;
	private int hand1Y;
	private int hand2X;
	private int hand2Y;
	// Used to offset the positions of hands (by child classes): the offset is
	// applied in the trig calculation
	protected double hand1OffsetX;
	protected double hand1OffsetY;
	protected double hand2OffsetX;
	protected double hand2OffsetY;
	protected boolean forceHandOffset; // Used by child class to determine if
										// they decide the hand offset

	private static final double HELD_TOOL_SIZE_RATIO = 1.25;
	private final int heldToolSize;
	private static final double HELD_ITEM_SIZE_RATIO = 0.65;
	private final int heldItemSize;
	private static final double SHIELD_SIZE_RATIO = 1;
	private final int shieldSize;
	private static final int HELD_TOOL_OFFSET_Y = -25;
	private static final int HELD_CLIPPERS_OFFSET_Y = 0;
	private static final int HELD_SWORD_OFFSET_Y = -25;
	private static final int SHIELD_OFFSET_Y = -15;
	private final int itemOffsetX;
	private final int itemOffsetY;
	private int heldItemX;
	private int heldItemY;
	protected int heldItemOffsetX;
	protected int heldItemOffsetY;
	private HoldOrientations holdOrientation;

	public Humanoid(double worldX, double worldY, int size, int tileSize, World world, Color bodyColor,
			Color hand1Color, Color hand2Color, Random random, String name) {
		super(worldX, worldY, size, size, tileSize, world, random, name);

		this.bodyColor = bodyColor;
		this.hand1Color = hand1Color;
		this.hand2Color = hand2Color;

		this.size = size;
		handSize = (int) (HAND_SIZE_RATIO * size);
		handOffsetX = (int) (HAND_OFFSET_X_RATIO * size);
		handOffsetY = (int) (HAND_OFFSET_Y_RATIO * size);
		handOffsetClose = (int) (HAND_OFFSET_CLOSE_RATIO * size);

		hand1X = size / 2 - handOffsetX - handSize;
		hand1Y = size - handOffsetY;
		hand2X = size / 2 + handOffsetX;
		hand2Y = size - handOffsetY;

		heldToolSize = (int) (HELD_TOOL_SIZE_RATIO * size);
		heldItemSize = (int) (HELD_ITEM_SIZE_RATIO * size);
		shieldSize = (int) (SHIELD_SIZE_RATIO * size);
		heldItemX = heldItemY = 0;
		holdOrientation = HoldOrientations.Normal;

		itemOffsetX = -handOffsetX - heldItemSize / 2;
		itemOffsetY = handOffsetY - heldItemSize / 2;

		handsVisible = bodyVisible = true;

		updateHandsPos(0);
	}

	public void update(long passedTime, Camera c, Point updateCenter) {
		super.update(passedTime, c, updateCenter);

		bodyDrawX = getDrawX(c);
		bodyDrawY = getDrawY(c);
		heldItemX = bodyDrawX + size / 2;
		heldItemY = bodyDrawY + size / 2;
	}

	public void draw(Graphics2D g, Camera c) {
		// Draw the body
		if (bodyVisible) {
			g.setColor(getDamageColor(bodyColor));
			g.fillOval(bodyDrawX, bodyDrawY, size, size);
		}

		boolean drawShield = !getShield().checkEmpty()
				&& (getHeldItem().checkEmpty() || (getHeldItem().getItem().getCategory() != ItemCategories.Tool
						&& getHeldItem().getItem().getCategory() != ItemCategories.Shield));
		boolean itemOnTop = !getHeldItem().checkEmpty() && getHeldItem().getItem().getCategory() != ItemCategories.Sword
				&& getHeldItem().getItem().getCategory() != ItemCategories.Tool;

		// Draw the hands (if the hold orientation calls for the hands to be
		// drawn
		if (holdOrientation != HoldOrientations.OneHandedRight && holdOrientation != HoldOrientations.OneHandedCenter
				&& !drawShield && handsVisible) {
			g.setColor(getDamageColor(hand2Color));
			g.fillOval(bodyDrawX + hand2X + size / 2 - handSize / 2, bodyDrawY + hand2Y + size / 2 - handSize / 2,
					handSize, handSize);
		}
		if (drawShield) {
			drawShield(inventory.getShield().getItem(), g, c);
		}
		// Draw the held item here so it is rendered under on hand but above the
		// other
		if (!getHeldItem().checkEmpty() && !itemOnTop) {
			drawHeldItem(g, c);
		}
		if (holdOrientation != HoldOrientations.OneHandedLeft && handsVisible) {
			g.setColor(getDamageColor(hand1Color));
			g.fillOval(bodyDrawX + hand1X + size / 2 - handSize / 2, bodyDrawY + hand1Y + size / 2 - handSize / 2,
					handSize, handSize);
		}
		if (itemOnTop) {
			drawHeldItem(g, c);
		}
	}

	private void drawHeldItem(Graphics2D g, Camera c) {
		if (getHeldItem().getItem().getCategory() == ItemCategories.Shield) {
			drawShield(getHeldItem().getItem(), g, c);
			return;
		}

		int offsetX = 0;
		int offsetY = 0;
		int itemSize = 0;
		double handBaseAngle = handAngleOffset + facingAngle;
		double angle = handBaseAngle;
		switch (getHeldItem().getItem().getCategory()) {
			case Tool:
				offsetX = 0;
				if (getHeldItem().getItem() == Items.Clippers) {
					offsetY = HELD_CLIPPERS_OFFSET_Y;
				} else {
					offsetY = HELD_TOOL_OFFSET_Y;
				}
				itemSize = heldToolSize;
				angle = handBaseAngle - Math.PI / 4;
				break;
			case Sword:
				offsetX = 0;
				offsetY = HELD_SWORD_OFFSET_Y;
				itemSize = heldToolSize;
				angle = handBaseAngle - Math.PI / 4;
				break;
			case Shield:
				offsetX = 0;
				offsetY = 0;
				angle = handBaseAngle - Math.PI / 2;
				itemSize = heldToolSize;
				break;
			default:
				offsetX = itemOffsetX;
				offsetY = itemOffsetY;
				itemSize = heldItemSize;
				angle = handBaseAngle - Math.PI / 2;
				break;
		}
		offsetX += heldItemOffsetX;
		offsetY += heldItemOffsetY;
		GraphicsUtils.drawRotatedImg(Images.loadImage(getHeldItem().getItem().getTextureDirectory()), angle, heldItemX,
				heldItemY, itemSize, itemSize, offsetX, offsetY, false, g);
	}

	private void drawShield(Items shield, Graphics2D g, Camera c) {
		GraphicsUtils.drawRotatedImg(Images.loadImage(shield.getTextureDirectory()), facingAngle - Math.PI / 2,
				heldItemX, heldItemY, shieldSize, shieldSize, 0, SHIELD_OFFSET_Y, false, g);
	}

	// Face towards the specified point, which changes the hand locations
	public void face(double angle) {
		super.face(angle);
		updateHoldOrientation();
		updateHandsPos(facingAngle + handAngleOffset);
	}

	public void setHandAngleOffset(double value) {
		handAngleOffset = value;
		updateHandsPos(facingAngle + handAngleOffset);
	}

	private void updateHandsPos(double angle) {
		double hand1CenterX = (handOffsetY + hand1OffsetY) * Math.cos(angle);
		double hand1CenterY = (handOffsetY + hand1OffsetY) * Math.sin(angle);
		double hand2CenterX = (handOffsetY + hand2OffsetY) * Math.cos(angle);
		double hand2CenterY = (handOffsetY + hand2OffsetY) * Math.sin(angle);
		// The angle from the hand center to each hand
		double hand1Angle = angle + Math.PI / 2;
		double hand2Angle = angle - Math.PI / 2;
		hand1X = (int) ((handOffsetX + hand1OffsetX) * Math.cos(hand1Angle) + hand1CenterX);
		hand1Y = (int) ((handOffsetX + hand1OffsetX) * Math.sin(hand1Angle) + hand1CenterY);
		hand2X = (int) ((handOffsetX + hand2OffsetX) * Math.cos(hand2Angle) + hand2CenterX);
		hand2Y = (int) ((handOffsetX + hand2OffsetX) * Math.sin(hand2Angle) + hand2CenterY);
	}

	// Sets the offsets for the hands in the facing animation
	private void updateHoldOrientation() {
		if (!forceHandOffset) {
			if (!getHeldItem().checkEmpty()) {
				holdOrientation = getHeldItem().getItem().getHoldOrientation();
			} else {
				holdOrientation = HoldOrientations.Normal;
			}
			switch (holdOrientation) {
				case Normal:
				case OneHandedRight:
				case OneHandedLeft:
					hand1OffsetX = hand1OffsetY = hand2OffsetX = hand2OffsetY = 0;
					break;
				case DoubleHandedClose:
					hand1OffsetX = hand2OffsetX = -handOffsetClose;
					hand1OffsetY = hand2OffsetY = 0;
					break;
				case OneHandedCenter:
					hand1OffsetX = -handOffsetX;
					hand1OffsetY = hand2OffsetY = 0;
					break;
			}
		}
	}

	public double getOrientation() {
		return facingAngle;
	}

	public boolean isUpsideDown() {
		return facingAngle > Math.PI && facingAngle < Math.PI * 2;
	}

	public int getSize() {
		return size;
	}

	public boolean isBodyVisible() {
		return bodyVisible;
	}

	public boolean areHandsVisible() {
		return handsVisible;
	}

	public void setBodyVisible(boolean value) {
		this.bodyVisible = value;
	}

	public void setHandsVisible(boolean value) {
		this.handsVisible = value;
	}
}