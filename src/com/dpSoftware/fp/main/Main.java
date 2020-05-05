package com.dpSoftware.fp.main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.Timer;

import org.spongepowered.noise.Noise;
import org.spongepowered.noise.NoiseQuality;

import com.dpSoftware.fp.world.*;
import com.dpSoftware.fp.crafting.*;
import com.dpSoftware.fp.entity.*;
import com.dpSoftware.fp.items.*;
import com.dpSoftware.fp.ui.*;
import com.dpSoftware.fp.ui.Menu;
import com.dpSoftware.fp.ui.Point;
import com.dpSoftware.fp.util.*;

public class Main extends JPanel implements MoveListener, IMenuListener {

	private static final int WINDOW_WIDTH = 1200;
	private static final int WINDOW_HEIGHT = 800;

	private boolean running;
	private long lastTime;
	// Need to set a maximum value for FPS or else it will go to "infinite"
	// This is due to the update loop taking less than a millisecond, and the
	// game measures in milliseconds
	// rather than in nanoseconds
	// However, it can technically reach more than 1000 FPS since it will only
	// max out at 1000 measured in
	// milliseconds, meaning that in smaller units of time (ex measuring FPS
	// using nanoseconds) it will reveal
	// that the game is running slightly faster
	private static final int GOAL_FPS = 1000;

	private MainMenu mainMenu;
	private CreateWorldMenu createWorldMenu;
	private SelectWorldMenu selectWorldMenu;
	private PauseMenu pauseMenu;
	private RespawnMenu respawnMenu;

	private BufferedImage titleImg;
	private static final int TITLE_WIDTH = 500;
	private static final int TITLE_HEIGHT = 300;
	private static final int TITLE_X = WINDOW_WIDTH / 2 - TITLE_WIDTH / 2;
	private static final int TITLE_Y = 65;

	private boolean escPrevPressed;

	private Random random;
	private GameState gameState;

	private BufferedImage buffer;
	private Graphics2D bufferGraphics;

	private World world;
	private Camera camera;
	private PlayerEntity mainPlayer;
	private HUD hud;

	// These pan around in the background of menus
	private World bgWorld;
	private Camera bgCamera;
	private Vector bgCameraDirection;
	private static final double BG_CAMERA_SPD = 1;

	private static final int PLAYER_SIZE = 50;

	public Main() {
		super();
	}

	public void init() {
		Images.init();
		while (Images.hasNext()) {
			Images.initNext();
		}
		CraftingRecipes.init();
		while (CraftingRecipes.hasNext()) {
			CraftingRecipes.initNext();
		}

		random = new Random();

		mainMenu = new MainMenu(WINDOW_WIDTH, WINDOW_HEIGHT, this);
		createWorldMenu = new CreateWorldMenu(WINDOW_WIDTH, WINDOW_HEIGHT, this);
		selectWorldMenu = new SelectWorldMenu(WINDOW_WIDTH, WINDOW_HEIGHT, this);
		pauseMenu = new PauseMenu(WINDOW_WIDTH, WINDOW_HEIGHT, this);
		respawnMenu = new RespawnMenu(WINDOW_WIDTH, WINDOW_HEIGHT, this);
		gameState = GameState.MainMenu;

		bgCameraDirection = new Vector(RandomUtils.randomDoubleBetween(random, 0, Math.PI * 2));
		bgWorld = new World(WINDOW_WIDTH, WINDOW_HEIGHT, random.nextLong(), "");
		bgWorld.setHasEntities(false);
		bgCamera = new Camera(0, 0);

		hud = new HUD(WINDOW_WIDTH, WINDOW_HEIGHT);

		new Input().addListeners(this);
		Input.init(1);

		buffer = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = (Graphics2D) buffer.getGraphics();

		titleImg = Images.loadImage("ui\\title");

		setFocusable(true);
		requestFocus();
		setDoubleBuffered(true);
	}

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Eternity Game");
		// Game Beta-0.42.2020.X.W.74-w04c_7 (Yellow Edition) PC Ultra v2
		// Remastered Lite 3.0 VII 2k20 (Pro)
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.white);
		frame.setVisible(true);
		Main window = new Main();
		window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		frame.add(window);
		frame.pack();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				window.terminate();
			}
		});
		window.init();

		try {
			window.run();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		frame.dispose();
	}

	public void run() {
		running = true;
		lastTime = System.currentTimeMillis();
		while (running) {
			// Force the game to run at a maximum FPS. When we let it run
			// unrestrained, it gets near-infinite FPS,
			// meaning that less than one millisecond passes between frames,
			// which breaks the game
			while (System.currentTimeMillis() - lastTime < (1000.0 / GOAL_FPS)) {
			}
			update((System.currentTimeMillis() - lastTime));
			repaint();
			lastTime = System.currentTimeMillis();
		}
		terminate();
	}

	public void update(long passedTime) {
		Input.updateStart();

		if (gameState != GameState.Playing) {
			updateBgWorldPanning(passedTime);
		}

		switch (gameState) {
			case MainMenu:
				mainMenu.update(passedTime);
				break;
			case Playing:
				if (!hud.isInventoryOpen()) {
					if (mainPlayer.getHealth() <= 0) {
						respawnMenu.update(passedTime);
					} else {
						world.update(camera, passedTime, mainPlayer.getCenter());
						if (!escPrevPressed && Input.isKey(KeyEvent.VK_ESCAPE)) {
							gameState = GameState.Paused;
							escPrevPressed = true;
						} else if (!Input.isKey(KeyEvent.VK_ESCAPE)) {
							escPrevPressed = false;
						}
					}
				}
				if (mainPlayer.getHealth() > 0) {
					hud.update(passedTime);
				}
				break;
			case CreateWorld:
				createWorldMenu.update(passedTime);
				break;
			case SelectWorld:
				selectWorldMenu.update(passedTime);
				break;
			case Paused:
				pauseMenu.update(passedTime);
				if (!escPrevPressed && Input.isKey(KeyEvent.VK_ESCAPE)) {
					gameState = GameState.Playing;
					escPrevPressed = true;
				} else if (!Input.isKey(KeyEvent.VK_ESCAPE)) {
					escPrevPressed = false;
				}
				break;
		}

		Input.updateEnd();
	}

	private void updateBgWorldPanning(long passedTime) {
		double secondsPassed = passedTime / 1000.0;
		bgCamera.changeX(bgCameraDirection.getX() * BG_CAMERA_SPD * secondsPassed);
		bgCamera.changeY(bgCameraDirection.getY() * BG_CAMERA_SPD * secondsPassed);
		bgWorld.update(bgCamera, passedTime, null);
	}

	public void onMoveX(Entity entity, double x) {
		if (entity == mainPlayer) {
			updateCameraX();
		}
	}

	public void onMoveY(Entity entity, double y) {
		if (entity == mainPlayer) {
			updateCameraY();
		}
	}

	private void updateCameraX() {
		camera.setX(mainPlayer.getWorldX() - WINDOW_WIDTH / (2 * World.TILE_SIZE)
				- (mainPlayer.getWidth() * 1.0 / World.TILE_SIZE) / 2);
	}

	private void updateCameraY() {
		camera.setY(mainPlayer.getWorldY() - WINDOW_HEIGHT / (2 * World.TILE_SIZE)
				- (mainPlayer.getWidth() * 1.0 / World.TILE_SIZE) / 2);
	}

	public void paintComponent(Graphics g) {
		if (!running) {
			// Only draw if the program has been started
			return;
		}

		bufferGraphics.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		Camera bgCameraForFrame = bgCamera.clone();
		if (gameState != GameState.Playing) {
			bgWorld.draw(bufferGraphics, bgCameraForFrame);
		}

		switch (gameState) {
			case MainMenu:
				mainMenu.draw(bufferGraphics);
				bufferGraphics.drawImage(titleImg, TITLE_X, TITLE_Y, TITLE_WIDTH, TITLE_HEIGHT, null);

				/*bufferGraphics.setColor(Color.red);
				bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 25));
				bufferGraphics.drawString("Seed: " + bgWorld.getSeed(), 0, WINDOW_HEIGHT - 5);*/
				break;
			case CreateWorld:
				createWorldMenu.draw(bufferGraphics);
				break;
			case SelectWorld:
				selectWorldMenu.draw(bufferGraphics);
				break;
			case Playing:
			case Paused:
				Camera worldCameraForFrame = camera.clone();
				world.draw(bufferGraphics, worldCameraForFrame);
				hud.draw(bufferGraphics);
				if (mainPlayer.getHealth() <= 0) {
					respawnMenu.draw(bufferGraphics);
				}

				/*bufferGraphics.setColor(Color.red);
				bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 25));
				bufferGraphics.drawString(
						"(" + MathUtils.roundTo(mainPlayer.getWorldX(), 2) + ", "
								+ MathUtils.roundTo(mainPlayer.getWorldY(), 2) + ") - Seed: " + world.getSeed(),
						0, WINDOW_HEIGHT - 5);*/

				if (gameState == GameState.Paused) {
					pauseMenu.draw(bufferGraphics);
				}
				break;
		}

		// Push out the buffer to the screen
		g.drawImage(buffer, 0, 0, null);
	}

	public void onButtonClick(Menu menu, int argument) {
		if (menu == mainMenu) {
			switch (argument) {
				case MainMenu.NEW_WORLD:
					gameState = GameState.CreateWorld;
					break;
				case MainMenu.LOAD_WORLD:
					gameState = GameState.SelectWorld;
					break;
				case MainMenu.EXIT:
					running = false;
					break;
			}
		} else if (menu == createWorldMenu) {
			switch (argument) {
				case CreateWorldMenu.BACK:
					gameState = GameState.MainMenu;
					break;
				case CreateWorldMenu.CREATE_WORLD:
					long seed = createWorldMenu.getSeed();
					String name = createWorldMenu.getName();
					if (new File(WorldSave.getFileLocationFor(name)).exists()) {
						createWorldMenu.nameboxError("This a world with this name already exists.");
					} else {
						World newWorldObj = new World(WINDOW_WIDTH, WINDOW_HEIGHT, seed, name);
						Point spawn = newWorldObj.findSuitableSpawnpoint(new Point());
						WorldSave newWorld = new WorldSave(seed, name, spawn);
						// Each new player gets their tools
						newWorld.getPlayerData().setInventory(getStarterInventory());
						newWorld.getPlayerData().setX(spawn.getX());
						newWorld.getPlayerData().setY(spawn.getY());
						initWorld(newWorld);
						newWorld.saveToFile();
						selectWorldMenu.addWorld(newWorld);
						gameState = GameState.Playing;
					}
					break;
			}
		} else if (menu == selectWorldMenu) {
			switch (argument) {
				case SelectWorldMenu.BACK:
					gameState = GameState.MainMenu;
					break;
				case SelectWorldMenu.SELECT:
					initWorld(selectWorldMenu.getSelectedWorld());
					gameState = GameState.Playing;
					break;
			}
		} else if (menu == pauseMenu) {
			switch (argument) {
				case PauseMenu.RESUME:
					gameState = GameState.Playing;
					break;
				case PauseMenu.MAIN_MENU:
					saveCurrentWorld();
					world = null;
					mainPlayer = null;
					gameState = GameState.MainMenu;
					break;
			}
		} else if (menu == respawnMenu) {
			switch (argument) {
				case RespawnMenu.RESPAWN:
					mainPlayer.setHealth(mainPlayer.getMaxHealth());
					mainPlayer.setEnergy(mainPlayer.getMaxEnergy());
					mainPlayer.setInventory(getStarterInventory());
					mainPlayer.setCoins(0);
					Point spawn = world.getWorldSpawn();
					mainPlayer.setWorldX(spawn.getX());
					mainPlayer.setWorldY(spawn.getY());
					updateCameraX();
					updateCameraY();
					break;
			}
		}
	}

	private void initWorld(WorldSave worldSave) {
		world = new World(WINDOW_WIDTH, WINDOW_HEIGHT, worldSave.getSeed(), worldSave.getName());
		world.setChunkMods(worldSave.getChunkModifications());
		world.setWorldSpawn(worldSave.getWorldSpawn());
		camera = new Camera(0, 0);

		PlayerData playerData = worldSave.getPlayerData();
		mainPlayer = new PlayerEntity(playerData.getX(), playerData.getY(), PLAYER_SIZE, World.TILE_SIZE, world,
				random);
		mainPlayer.setMoveListener(this);
		mainPlayer.setPlayerData(playerData);
		// Snowy: -1806 ; 0
		// Forest: -150 ; 150
		// Plains: 0 ; 0
		// Desert: 0 ; 75
		world.addEntity(mainPlayer);

		hud.setPlayer(mainPlayer);

		camera.setX(mainPlayer.getWorldX() - WINDOW_WIDTH / (2 * World.TILE_SIZE)
				- (mainPlayer.getWidth() * 1.0 / World.TILE_SIZE) / 2);
		camera.setY(mainPlayer.getWorldY() - WINDOW_HEIGHT / (2 * World.TILE_SIZE)
				- (mainPlayer.getWidth() * 1.0 / World.TILE_SIZE) / 2);
	}

	private void terminate() {
		if (gameState == GameState.Playing) {
			// Need to save the world that is currently open
			saveCurrentWorld();
		}
	}

	private void saveCurrentWorld() {
		WorldSave currentWorld = new WorldSave(world.getSeed(), world.getName(), world.getChunkMods(),
				mainPlayer.getPlayerData(), world.getWorldSpawn());
		currentWorld.saveToFile();
		selectWorldMenu.setWorld(currentWorld.getName(), currentWorld);
	}

	private Inventory getStarterInventory() {
		Inventory inv = new Inventory();
		inv.addItem(new ItemStack(Items.BasicSword, 1));
		inv.addItem(new ItemStack(Items.Axe, 1));
		inv.addItem(new ItemStack(Items.Pickaxe, 1));
		inv.addItem(new ItemStack(Items.Shovel, 1));
		inv.addItem(new ItemStack(Items.Clippers, 1));
		return inv;
	}

}
