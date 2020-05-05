package com.dpSoftware.fp.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dpSoftware.fp.crafting.CraftingRecipe;

public class ResourceLoader {

	private static final String ALL_RESOURCES_FOLDER = "res";

	private static final String IMG_FOLDER = "images";
	private static final String RECIPES_FOLDER = "recipes";

	private static final String IMG_EXTENSION = "png";
	private static final String RECIPE_EXTENSION = "json";

	public static String getBaseDirectory() {
		return ALL_RESOURCES_FOLDER;
	}
	
	public static BufferedImage loadImage(String fileLoc) {
		return loadImageAbsolute(getImgFileLocation(fileLoc));
	}
	public static BufferedImage loadImageAbsolute(String path) {
		File file = new File(path);
		if (file.exists()) {
			try {
				return ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static String getImgFileLocation(String path) {
		return getImageDirectory() + "\\" + path + "." + IMG_EXTENSION;
	}
	public static String getImageDirectory() {
		return getBaseDirectory() + "\\" + IMG_FOLDER;
	}
	
	public static CraftingRecipe loadRecipe(String fileLoc) {
		return loadRecipeAbsolute(getRecipeFileLocation(fileLoc));
	}
	public static CraftingRecipe loadRecipeAbsolute(String path) {
		File file = new File(path);
		if (file.exists()) {
			try {
				return CraftingRecipe.fromJSON(FileUtils.readAllText(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static String getRecipeFileLocation(String path) {
		return getRecipeDirectory() + "\\" + path + "." + RECIPE_EXTENSION;
	}
	public static String getRecipeDirectory() {
		return getBaseDirectory() + "\\" + RECIPES_FOLDER;
	}
}
