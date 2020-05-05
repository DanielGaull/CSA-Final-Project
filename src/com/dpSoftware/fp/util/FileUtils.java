package com.dpSoftware.fp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}

	public static String createPath(String... directories) {
		String sep = System.getProperty("file.separator");
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < directories.length - 1; i++) {
			path.append(directories[i]).append(sep);
		}
		// We exclude the last directory from the for loop
		// and add it here so that the separator isn't included after
		// the last directory
		path.append(directories[directories.length - 1]);
		return path.toString();
	}

	public static void writeToFile(String contents, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.write(contents);
			writer.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	public static String readAllText(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			builder.append(line + "\n");
		}
		br.close();
		return builder.toString();
	}
	
	public static ArrayList<File> getAllSubFiles(File directory) {
		ArrayList<File> files = new ArrayList<>();
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isFile()) {
					files.add(fList[i]);
				} else if (fList[i].isDirectory()) {
					files.addAll(getAllSubFiles(fList[i]));
				}
			}
		}
		return files;
	}

}
