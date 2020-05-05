package com.dpSoftware.fp.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.dpSoftware.fp.ui.Point;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final short NUM_KEYS = 256;
	private static final short NUM_BUTTONS = 5;

	private static boolean[] keys = new boolean[NUM_KEYS];
	private static boolean[] keysLast = new boolean[NUM_KEYS];

	private static boolean[] buttons = new boolean[NUM_BUTTONS];
	private static boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	private static int mouseX, mouseY;
	private static double scroll;
	private static double prevScroll;

	private static int scale = 1;

	private static boolean isCtrlPressed = false;
	private static boolean isShiftPressed = false;
	private static boolean isAltPressed = false;
	private static boolean isEscapePressed = false;
	private static boolean isWheelDown = false;
	private static boolean isRightDown = false;
	private static boolean isLeftDown = false;

	public static void init(int scale) {
		mouseX = 0;
		mouseY = 0;
		scroll = 0;

		Input.scale = scale;
		if (scale < 0)
			scale = 1;
	}

	public void addListeners(Component c) {
		c.addKeyListener(this);
		c.addMouseMotionListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
	}

	// Update in each game loop
	public static void updateStart() {
		System.arraycopy(keys, 0, keysLast, 0, NUM_KEYS);
		System.arraycopy(buttons, 0, buttonsLast, 0, NUM_BUTTONS);
	}
	public static void updateEnd() {
		scroll = 0;
	}

	public static boolean isKey(int keycode) {
		return keys[keycode];
	}

	public static boolean isKeyPressed(int keycode) {
		return keys[keycode];// && !keysLast[keycode];
	}

	public static boolean isKeyReleased(int keycode) {
		return !keys[keycode];// && keysLast[keycode];
	}

	public static boolean isButton(int button) {
		return buttons[button];
	}

	public static boolean isButtonPressed(int button) {
		return buttons[button] && !buttonsLast[button];
	}

	public static boolean isButtonReleased(int button) {
		return !buttons[button] && buttonsLast[button];
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		scroll = e.getWheelRotation();
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX() / scale;
		mouseY = e.getY() / scale;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX() / scale;
		mouseY = e.getY() / scale;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
		if (e.getButton() == MouseEvent.BUTTON1)
			isLeftDown = true;
		if (e.getButton() == MouseEvent.BUTTON2)
			isWheelDown = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			isRightDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
		if (e.getButton() == MouseEvent.BUTTON1)
			isLeftDown = false;
		if (e.getButton() == MouseEvent.BUTTON2)
			isWheelDown = false;
		if (e.getButton() == MouseEvent.BUTTON3)
			isRightDown = false;
	}

	public void keyPressed(KeyEvent e) {

		// Prevents ArrayIndexOutOfBoundsException
		if (e.getKeyCode() > keys.length) return;
		
		keys[e.getKeyCode()] = true;

		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			isShiftPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			isCtrlPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_ALT)
			isAltPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			isEscapePressed = true;
	}

	public void keyReleased(KeyEvent e) {

		// Prevents ArrayIndexOutOfBoundsException
		if (e.getKeyCode() > keys.length) return;
		
		keys[e.getKeyCode()] = false;

		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			isShiftPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			isCtrlPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_ALT)
			isAltPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			isEscapePressed = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public static int[] getPressedKeys() {
		ArrayList<Integer> pkeys = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i]) {
				pkeys.add(i);
			}
		}
		return ArrayUtils.toIntArray(pkeys);
	}

	public static int getMouseX() {
		return mouseX;
	}

	public static int getMouseY() {
		return mouseY;
	}
	
	public static Point getMouseLoc() {
		return new Point(mouseX, mouseY);
	}

	public static double getScroll() {
		return scroll;
	}

	public static boolean isCtrlPressed() {
		return isCtrlPressed;
	}

	public static boolean isShiftPressed() {
		return isShiftPressed;
	}

	public static boolean isAltPressed() {
		return isAltPressed;
	}

	public static boolean isEscapePressed() {
		return isEscapePressed;
	}

	public static boolean isWheelDown() {
		return isWheelDown;
	}

	public static boolean isRightDown() {
		return isRightDown;
	}

	public static boolean isLeftDown() {
		return isLeftDown;
	}
}
