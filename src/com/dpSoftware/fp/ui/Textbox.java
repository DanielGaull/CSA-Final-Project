package com.dpSoftware.fp.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import com.dpSoftware.fp.util.ArrayUtils;
import com.dpSoftware.fp.util.Clipboard;
import com.dpSoftware.fp.util.Input;
import com.dpSoftware.fp.util.Timer;

public class Textbox extends MenuElement {

	private String text;
	private String hint;
	//private boolean hintInFront;
	private int maxLength;
	private Font font;
	private Color normalBorder;
	private Color selectedBorder;
	private Color errorBorder;
	private Color highlight;
	private ArrayList<Character> notAllowedCharacters;
	
	private boolean spammingKey;
	private boolean arrowPrevDown;
	private boolean shortcutPrevDown;
	private boolean gotoPrevDown;
	private boolean selected;
	private int highlightPos1;
	private int highlightPos2;
	private boolean highlighting;
	
	private boolean numbersOnly;
	private boolean allowDecimals; // Only if numbersOnly is true
	private boolean allowNegatives; // Only if numbersOnly is true
	
	private boolean hasError; 
	private String errorText;
	private static final int ERROR_TEXT_SPACING = 25;
	
	private Rectangle cursorRect;
	private static final int CURSOR_WIDTH = 2;
	private int cursorPosition;
	private boolean arrowKeyDown;
    private Timer cursorBlinkTimer;
    private boolean showingCursor;
	
	private int[] pressedKeys;
	private int[] lastKeys;
	private int key;
	private int lastKey;
	private Timer typeTimerShort;
	private Timer typeTimerLong;
	
	private static final int TEXT_EDGE_SPACING_X = 4;
	private static final int TEXT_EDGE_SPACING_Y = 5;
	private static final int FONT_SIZE_OFFSET = 15;
    private static final int CURSOR_SPACING = 7;
    
    private ITextChangeListener textChangeListener;
	
	public Textbox(int x, int y, int width, int height, Color fillColor, Color borderColor, Color selectedBorderColor, Color errorBorderColor, Color highlightColor,
			int maxLength, Font font) {
		super(x, y, width, height, fillColor, borderColor);
		
		this.maxLength = maxLength;
		
		normalBorder = borderColor;
		selectedBorder = selectedBorderColor;
		errorBorder = errorBorderColor;
		highlight = highlightColor;
		text = "";
		
		notAllowedCharacters = new ArrayList<>();
		
		cursorBlinkTimer = new Timer(500);
		cursorBlinkTimer.start();
		
		this.font = font;
		
		typeTimerLong = new Timer(750);
		typeTimerShort = new Timer(75);
		
		int cursorHeight = height - (TEXT_EDGE_SPACING_Y * 2);
		cursorRect = new Rectangle(x + TEXT_EDGE_SPACING_X, y + height / 2 - cursorHeight / 2,
				CURSOR_WIDTH, cursorHeight);
		
		selected = false;
	}
	public Textbox(int x, int y, int width, int height, int maxLength, Font font) {
		this(x, y, width, height, InterfaceTheme.TBOX_FILL, InterfaceTheme.TBOX_BORDER, InterfaceTheme.TBOX_SELECTED, InterfaceTheme.TBOX_ERROR,
				InterfaceTheme.TBOX_HIGHLIGHT, maxLength, font);
	}
	public Textbox(int x, int y, int width, int height, int maxLength) {
		this(x, y, width, height, maxLength, InterfaceTheme.BIG_FONT);
	}

	public void update(long passedTime) {
		super.update(passedTime);
		
		if (selected) {
			borderColor = selectedBorder;
			
			cursorBlinkTimer.update(passedTime);
			if (cursorBlinkTimer.query()) {
				showingCursor = !showingCursor;
				cursorBlinkTimer.reset();
			}
			
			pressedKeys = Input.getPressedKeys();
			key = 0;
			
			// This if statement gets the key that is being pressed
			if (pressedKeys.length > 0) {
				
				ArrayList<Integer> keys = new ArrayList<>();
				for (int i = 0; i < pressedKeys.length; i++) {
					if (!(pressedKeys[i] == KeyEvent.VK_SHIFT || pressedKeys[i] == KeyEvent.VK_CONTROL || pressedKeys[i] == KeyEvent.VK_ALT || 
							ArrayUtils.arrayContains(lastKeys, pressedKeys[i]))) {
						keys.add(pressedKeys[i]);
					}
				}
				if (keys.size() > 0) {
					key = keys.get(0);
				} else if (ArrayUtils.arrayContains(pressedKeys, lastKey)) {
					// Only allow spamming if no new key is pressed
					key = lastKey;
				}
			}
			
			if (key != 0) {
				if (!spammingKey) {
					// Check to see if a single key has been held down for enough time
					if (ArrayUtils.arrayContains(lastKeys, key)) {
						if (!typeTimerLong.isRunning()) {
							typeTimerLong.start();
						} else {
							typeTimerLong.update(passedTime);
							if (typeTimerLong.query()) {
								spammingKey = true;
								handleKey(key);
							}
						}
					} else {
						// Stopped holding down the same key, so we reset the timer for when a key is held down again
						typeTimerLong.reset();
						handleKey(key);
					}
				} else {
					// Spamming a key
					if (ArrayUtils.arrayContains(lastKeys, key)) {
						if (!typeTimerShort.isRunning()) {
							typeTimerShort.start();
						} else {
							typeTimerShort.update(passedTime);
							if (typeTimerShort.query()) {
								// Pressing and holding a key so we can "spam" it
								handleKey(key);
							}
						}
						
					} else {
						spammingKey = false;
					}
				}
			}
			lastKey = key;
			lastKeys = pressedKeys;
			
			// Arrow keys
			if (Input.isKeyPressed(KeyEvent.VK_LEFT) && !arrowPrevDown) {
				arrowPrevDown = true;
				showingCursor = true;
				if (cursorPosition > 0) {
					if (Input.isShiftPressed()) {
						if (!highlighting) {
							// Not yet highlighting, so we need to start it
							highlightPos1 = cursorPosition - 1;
							highlightPos2 = cursorPosition;
							highlighting = true;
						} else if (cursorPosition == highlightPos1) {
							// Already highlighting, so we need to continue it
							highlightPos1--;
						} else if (cursorPosition == highlightPos2) {
							highlightPos2--;
						}
					} else {
						highlighting = false;
					}
					cursorPosition--;
				}
				
			} else if (Input.isKeyPressed(KeyEvent.VK_RIGHT) && !arrowPrevDown) {
				arrowPrevDown = true;
				showingCursor = true;
				if (cursorPosition < text.length()) {
					if (Input.isShiftPressed()) {
						if (!highlighting) {
							// Not yet highlighting, so we need to start it
							highlightPos1 = cursorPosition;
							highlightPos2 = cursorPosition + 1;
							highlighting = true;
						} else if (cursorPosition == highlightPos2) {
							// Already highlighting, so we need to continue it
							highlightPos2++;
						} else if (cursorPosition == highlightPos1) {
							highlightPos1++;
						}
					} else {
						highlighting = false;
					}
					cursorPosition++;
				}
				
			} else if (!Input.isKeyPressed(KeyEvent.VK_LEFT) && !Input.isKeyPressed(KeyEvent.VK_RIGHT)) {
				arrowPrevDown = false;
			}
			
			if (Input.isKeyPressed(KeyEvent.VK_HOME) && !gotoPrevDown) {
				if (Input.isShiftPressed()) {
					if (!highlighting) {
						highlighting = true;
					}
					highlightPos1 = 0;
					highlightPos2 = cursorPosition;
				} else {
					highlighting = false;
				}
				cursorPosition = 0;
				gotoPrevDown = true;
			} else if (Input.isKeyPressed(KeyEvent.VK_END) && !gotoPrevDown) {
				if (Input.isShiftPressed()) {
					if (!highlighting) {
						highlighting = true;
					}
					highlightPos1 = cursorPosition;
					highlightPos2 = text.length();
				} else {
					highlighting = false;
				}
				cursorPosition = text.length();
				gotoPrevDown = true;
			} else if (!Input.isKeyPressed(KeyEvent.VK_HOME) && !Input.isKeyPressed(KeyEvent.VK_END)) {
				gotoPrevDown = false;
			}
			
			// Ctrl+Copy, Ctrl+Cut, and Ctrl+Paste support
			if (Input.isCtrlPressed() && Input.isKeyPressed(KeyEvent.VK_V) && !shortcutPrevDown) {
				String cpText = Clipboard.getText();
				if (text.length() + cpText.length() <= maxLength && fitsSetCriteria(cpText)) {
					StringBuilder sb = new StringBuilder(text);
					if (highlighting) {
						sb.delete(highlightPos1, highlightPos2);
						cursorPosition = highlightPos1;
						highlighting = false;
					}
					sb.insert(cursorPosition, cpText);
					setText(sb.toString());
					cursorPosition += cpText.length();
				}
				shortcutPrevDown = true;
			} else if (Input.isCtrlPressed() && Input.isKeyPressed(KeyEvent.VK_X) && !shortcutPrevDown) {
				if (highlighting) {
					String highlightedText = text.substring(highlightPos1, highlightPos2);
					Clipboard.setClipboardText(highlightedText);
					StringBuilder sb = new StringBuilder(text);
					sb.delete(highlightPos1, highlightPos2);
					cursorPosition = highlightPos1;
					setText(sb.toString());
					highlighting = false;
				}
				
			} else if  (Input.isCtrlPressed() && Input.isKeyPressed(KeyEvent.VK_C) && !shortcutPrevDown) {
				if (highlighting) {
					String highlightedText = text.substring(highlightPos1, highlightPos2);
					Clipboard.setClipboardText(highlightedText);
				}
				
			} else if (!(Input.isCtrlPressed() && (Input.isKeyPressed(KeyEvent.VK_V) || Input.isKeyPressed(KeyEvent.VK_C) || Input.isKeyPressed(KeyEvent.VK_X)))) {
				shortcutPrevDown = false;
			}
			
		} else {
			borderColor = normalBorder;
		}
		
		if (hasError) {
			borderColor = errorBorder;
		}
	}
	public void draw(Graphics2D g) {
		super.draw(g);
		FontMetrics metrics = g.getFontMetrics(font);
		g.setFont(font);
		Point textPos = new Point(getX() + TEXT_EDGE_SPACING_X, getY() + getHeight() / 2 + metrics.getHeight() / 2 - TEXT_EDGE_SPACING_Y);
		if (highlighting) {
			g.setColor(highlight);
			g.fillRect((int) textPos.getX() + metrics.stringWidth(text.substring(0, highlightPos1)), (int) rect.getY() + TEXT_EDGE_SPACING_Y, 
					metrics.stringWidth(text.substring(highlightPos1, highlightPos2)), (int) rect.getHeight() - TEXT_EDGE_SPACING_Y * 2);
			
			g.setColor(Color.white);
			g.drawString(text.substring(0, highlightPos1), (int) textPos.getX(), (int) textPos.getY());
			g.setColor(Color.black);
			g.drawString(text.substring(highlightPos1, highlightPos2), (int) textPos.getX() + metrics.stringWidth(text.substring(0, highlightPos1)), (int) textPos.getY());
			g.setColor(Color.white);
			g.drawString(text.substring(highlightPos2), (int) textPos.getX() + metrics.stringWidth(text.substring(0, highlightPos2)), (int) textPos.getY());
		} else {
			g.setColor(Color.white);
			g.drawString(text, (int) textPos.getX(), (int) textPos.getY());
		}
		if ((text == null || text.length() <= 0) && hint != null && hint.length() > 0) {
			g.setColor(new Color(127, 127, 127));
			g.drawString(hint, (int) textPos.getX(), (int) textPos.getY());
		}
		
		if (hasError && errorText != null && errorText.length() > 0) {
			g.setColor(errorBorder);
			g.drawString(errorText, (int) textPos.getX(), getY() + getHeight() + ERROR_TEXT_SPACING);
		}
		
		if (selected && showingCursor) {
			cursorRect.setX(textPos.getX() + metrics.stringWidth(text.substring(0, cursorPosition)));
			g.setColor(Color.white);
			g.fillRect((int) cursorRect.getX(), (int) cursorRect.getY(), (int) cursorRect.getWidth(), (int) cursorRect.getHeight());
		}
	}
	
	public void setTextChangeListener(ITextChangeListener listener) {
		this.textChangeListener = listener;
	}
	
	public void addBlockedCharacter(char c) {
		notAllowedCharacters.add(c);
	}
	
	public String getHint() {
		return hint;
	}
	public void setHint(String hintText) {
		hint = hintText;
	}
	
	public void startError() {
		hasError = true;
		errorText = "";
	}
	public void startError(String text) {
		startError();
		errorText = text;
	}
	public void endError() {
		hasError = false;
	}
	public boolean hasError() {
		return hasError;
	}
	
	public void resetText() {
		text = "";
		cursorPosition = 0;
	}
	public void setText(String value) {
		String newText = "";
		for (int i = 0; i < value.length(); i++) {
			if (!notAllowedCharacters.contains(value.charAt(i))) {
				newText += value.charAt(i);
			} else {
				cursorPosition--;
			}
		}
		if (textChangeListener != null && !text.equals(newText)) {
			this.text = newText;
			textChangeListener.onTextChanged(this, text);
		} else {
			this.text = newText;
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	private void handleKey(int key) {
		String textToAdd = "";
		if (key > 0) {
			if (key == KeyEvent.VK_SPACE) {
				textToAdd = " ";
			} else if (key == KeyEvent.VK_BACK_SPACE && text.length() > 0 && (cursorPosition > 0 || highlighting)) {
				StringBuilder sb = new StringBuilder(text);
				if (highlighting) {
					sb.delete(highlightPos1, highlightPos2);
					highlighting = false;
					cursorPosition = highlightPos1;
				} else {
					cursorPosition--;
					sb.deleteCharAt(cursorPosition);
				}
				setText(sb.toString());
			} else if (key == KeyEvent.VK_DELETE && text.length() > 0 && (cursorPosition < text.length() || highlighting)) {
				StringBuilder sb = new StringBuilder(text);
				if (highlighting) {
					sb.delete(highlightPos1, highlightPos2);
					highlighting = false;
					cursorPosition = highlightPos1;
				} else {
					sb.deleteCharAt(cursorPosition);
				}
				setText(sb.toString());
			} else if (key == KeyEvent.VK_ENTER) {
				// TODO: do something about pressing enter?
			} else if (!Input.isCtrlPressed()) {
				if (Input.isShiftPressed()) {
					textToAdd = handleShift(key);
				} else {
					textToAdd = handleNormalKey(key);
				} 
			} 
		}
		
		if (textToAdd.length() > 0 && text.length() + textToAdd.length() <= maxLength) {
			if ((numbersOnly && fitsSetCriteria(textToAdd)) || !numbersOnly) {
				
				StringBuilder sb = new StringBuilder(text);
				if (highlighting) {
					sb.delete(highlightPos1, highlightPos2);
					cursorPosition = highlightPos1;
					highlighting = false;
				}
				sb.insert(cursorPosition, textToAdd);
				setText(sb.toString());
				cursorPosition += textToAdd.length();
			}
		}
	}
	private String handleNormalKey(int key) {
		switch (key) {
			case KeyEvent.VK_A:
				return "a";
			case KeyEvent.VK_B:
				return "b";
			case KeyEvent.VK_C:
				return "c";
			case KeyEvent.VK_D:
				return "d";
			case KeyEvent.VK_E:
				return "e";
			case KeyEvent.VK_F:
				return "f";
			case KeyEvent.VK_G:
				return "g";
			case KeyEvent.VK_H:
				return "h";
			case KeyEvent.VK_I:
				return "i";
			case KeyEvent.VK_J:
				return "j";
			case KeyEvent.VK_K:
				return "k";
			case KeyEvent.VK_L:
				return "l";
			case KeyEvent.VK_M:
				return "m";
			case KeyEvent.VK_N:
				return "n";
			case KeyEvent.VK_O:
				return "o";
			case KeyEvent.VK_P:
				return "p";
			case KeyEvent.VK_Q:
				return "q";
			case KeyEvent.VK_R:
				return "r";
			case KeyEvent.VK_S:
				return "s";
			case KeyEvent.VK_T:
				return "t";
			case KeyEvent.VK_U:
				return "u";
			case KeyEvent.VK_V:
				return "v";
			case KeyEvent.VK_W:
				return "w";
			case KeyEvent.VK_X:
				return "x";
			case KeyEvent.VK_Y:
				return "y";
			case KeyEvent.VK_Z:
				return "z";
			case KeyEvent.VK_0:
			case KeyEvent.VK_NUMPAD0:
				return "0";
			case KeyEvent.VK_1:
			case KeyEvent.VK_NUMPAD1:
				return "1";
			case KeyEvent.VK_2:
			case KeyEvent.VK_NUMPAD2:
				return "2";
			case KeyEvent.VK_3:
			case KeyEvent.VK_NUMPAD3:
				return "3";
			case KeyEvent.VK_4:
			case KeyEvent.VK_NUMPAD4:
				return "4";
			case KeyEvent.VK_5:
			case KeyEvent.VK_NUMPAD5:
				return "5";
			case KeyEvent.VK_6:
			case KeyEvent.VK_NUMPAD6:
				return "6";
			case KeyEvent.VK_7:
			case KeyEvent.VK_NUMPAD7:
				return "7";
			case KeyEvent.VK_8:
			case KeyEvent.VK_NUMPAD8:
				return "8";
			case KeyEvent.VK_9:
			case KeyEvent.VK_NUMPAD9:
				return "9";
			case KeyEvent.VK_BACK_QUOTE:
				return "`";
			case KeyEvent.VK_MINUS:
			case KeyEvent.VK_SUBTRACT:
				return "-";
			case KeyEvent.VK_EQUALS:
				return "=";
			case KeyEvent.VK_OPEN_BRACKET:
				return "[";
			case KeyEvent.VK_CLOSE_BRACKET:
				return "]";
			case KeyEvent.VK_BACK_SLASH:
				return "\\";
			case KeyEvent.VK_SEMICOLON:
				return ";";
			case KeyEvent.VK_QUOTE:
				return "'";
			case KeyEvent.VK_COMMA:
				return ",";
			case KeyEvent.VK_PERIOD:
			case KeyEvent.VK_DECIMAL:
				return ".";
			case KeyEvent.VK_SLASH:
			case KeyEvent.VK_DIVIDE:
				return "/";
			case KeyEvent.VK_ADD:
				return "+";
			case KeyEvent.VK_MULTIPLY:
				return "*";
		}
		return "";
	}
	private String handleShift(int key) {
		switch (key) {
		case KeyEvent.VK_A:
			return "A";
		case KeyEvent.VK_B:
			return "B";
		case KeyEvent.VK_C:
			return "C";
		case KeyEvent.VK_D:
			return "D";
		case KeyEvent.VK_E:
			return "E";
		case KeyEvent.VK_F:
			return "F";
		case KeyEvent.VK_G:
			return "G";
		case KeyEvent.VK_H:
			return "H";
		case KeyEvent.VK_I:
			return "I";
		case KeyEvent.VK_J:
			return "J";
		case KeyEvent.VK_K:
			return "K";
		case KeyEvent.VK_L:
			return "L";
		case KeyEvent.VK_M:
			return "M";
		case KeyEvent.VK_N:
			return "N";
		case KeyEvent.VK_O:
			return "O";
		case KeyEvent.VK_P:
			return "P";
		case KeyEvent.VK_Q:
			return "Q";
		case KeyEvent.VK_R:
			return "R";
		case KeyEvent.VK_S:
			return "S";
		case KeyEvent.VK_T:
			return "T";
		case KeyEvent.VK_U:
			return "U";
		case KeyEvent.VK_V:
			return "V";
		case KeyEvent.VK_W:
			return "W";
		case KeyEvent.VK_X:
			return "X";
		case KeyEvent.VK_Y:
			return "Y";
		case KeyEvent.VK_Z:
			return "Z";
		case KeyEvent.VK_0:
			return ")";
		case KeyEvent.VK_NUMPAD0:
			return "0";
		case KeyEvent.VK_1:
			return "!";
		case KeyEvent.VK_NUMPAD1:
			return "1";
		case KeyEvent.VK_2:
			return "@";
		case KeyEvent.VK_NUMPAD2:
			return "2";
		case KeyEvent.VK_3:
			return "#";
		case KeyEvent.VK_NUMPAD3:
			return "3";
		case KeyEvent.VK_4:
			return "$";
		case KeyEvent.VK_NUMPAD4:
			return "4";
		case KeyEvent.VK_5:
			return "%";
		case KeyEvent.VK_NUMPAD5:
			return "5";
		case KeyEvent.VK_6:
			return "^";
		case KeyEvent.VK_NUMPAD6:
			return "6";
		case KeyEvent.VK_7:
			return "&";
		case KeyEvent.VK_NUMPAD7:
			return "7";
		case KeyEvent.VK_8:
		case KeyEvent.VK_MULTIPLY:
			return "*";
		case KeyEvent.VK_NUMPAD8:
			return "8";
		case KeyEvent.VK_9:
			return "(";
		case KeyEvent.VK_NUMPAD9:
			return "9";
		case KeyEvent.VK_BACK_QUOTE:
			return "~";
		case KeyEvent.VK_MINUS:
			return "_";
		case KeyEvent.VK_SUBTRACT:
			return "-";
		case KeyEvent.VK_EQUALS:
		case KeyEvent.VK_ADD:
			return "+";
		case KeyEvent.VK_OPEN_BRACKET:
			return "{";
		case KeyEvent.VK_CLOSE_BRACKET:
			return "}";
		case KeyEvent.VK_BACK_SLASH:
			return "|";
		case KeyEvent.VK_SEMICOLON:
			return ":";
		case KeyEvent.VK_QUOTE:
			return "\"";
		case KeyEvent.VK_COMMA:
			return "<";
		case KeyEvent.VK_PERIOD:
			return ">";
		case KeyEvent.VK_DECIMAL:
			return ".";
		case KeyEvent.VK_SLASH:
			return "?";
		case KeyEvent.VK_DIVIDE:
			return "/";
		}
		return "";
	}
	private boolean fitsSetCriteria(String text) {
		if (numbersOnly) {
			for (int i = 0; i < text.length(); i++) {
				if (!((Character.isDigit(text.charAt(i)) || (allowNegatives && text.charAt(i) == '-') || 
						(allowDecimals && text.charAt(i) == '.')))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String getText() {
		return text;
	}
	public boolean isNumbersOnly() {
		return numbersOnly;
	}
	public void setNumbersOnly(boolean value) {
		numbersOnly = value;
	}
	public boolean allowsNegatives() {
		return allowNegatives;
	}
	public void setAllowNegatives(boolean value) {
		allowNegatives = value;
	}
	public boolean allowsDecimals() {
		return allowDecimals;
	}
	public void setAllowDecimals(boolean value) {
		allowDecimals = value;
	}
	
	protected void onClick() {
		selected = true;
	}
	protected void onClickOutside() {
		selected = false;
		highlighting = false;
		cursorPosition = text.length();
	}
}
