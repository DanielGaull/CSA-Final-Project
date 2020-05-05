package com.dpSoftware.fp.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Images {

	// This class essentially "caches" images as they're loaded. This way, once an image has been loaded
	// a single time, it won't be loaded again, but can simply be grabbed from the "cache"
	private static HashMap<String, BufferedImage> imgDict;
	private static boolean doneInitializing;
	private static ArrayList<File> imgFiles;
	private static int initIndex;
	
	public static void init() {
		imgDict = new HashMap<>();
		
		File imgDir = new File(ResourceLoader.getImageDirectory());
		imgFiles = FileUtils.getAllSubFiles(imgDir);
		initIndex = 0;
	}
	public static void initNext() {
		String path = imgFiles.get(initIndex).getPath();
		imgDict.put(path, ResourceLoader.loadImageAbsolute(path));
		initIndex++;
		if (!hasNext()) {
			doneInitializing = true;
		}
	}
	public static boolean hasNext() {
		return (initIndex < imgFiles.size());
	}
	public static double getProgress() {
		return 1.0 * initIndex / imgFiles.size();
	}
	
	public static BufferedImage loadImage(String folder) {
		return imgDict.get(ResourceLoader.getImgFileLocation(folder));
	}

}
