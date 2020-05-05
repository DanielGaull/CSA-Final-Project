package com.dpSoftware.fp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrayUtils {

	public static int[] toIntArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static boolean arrayContains(int[] array, int val) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == val) {
				return true;
			}
		}
		return false;
	}
	public static <T> boolean arrayContains(T[] array, T val) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(val)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> T random(T[] array, Random random) {
		return array[RandomUtils.randomIntBetween(random, 0, array.length - 1)];
	}
	public static <T> T random(ArrayList<T> list, Random random) {
		return list.get(RandomUtils.randomIntBetween(random, 0, list.size() - 1));
	}
	
	public static <T> String arrayToString(T[] array) {
		String str = "[";
		for (int i = 0; i < array.length; i++) {
			str += array[i].toString();
			if (i + 1 < array.length) {
				str += ", ";
			}
		}
		str += "]";
		return str;
	}
	public static String arrayToString(int[] array) {
		String str = "[";
		for (int i = 0; i < array.length; i++) {
			str += array[i];
			if (i + 1 < array.length) {
				str += ", ";
			}
		}
		str += "]";
		return str;
	}
	public static String arrayToString(byte[] array) {
		String str = "[";
		for (int i = 0; i < array.length; i++) {
			str += array[i];
			if (i + 1 < array.length) {
				str += ", ";
			}
		}
		str += "]";
		return str;
	}
	
	public static int max(int[] array) {
		if (array.length < 1) {
			return 0;
		}
		int max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	public static double max(double[] array) {
		if (array.length < 1) {
			return 0;
		}
		double max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	public static int min(int[] array) {
		if (array.length < 1) {
			return 0;
		}
		int min = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}
	public static double min(double[] array) {
		if (array.length < 1) {
			return 0;
		}
		double min = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}
	
	public static <T> void printArray(T[] array) {
		System.out.println(arrayToString(array));
	}
	public static void printArray(int[] array) {
		System.out.println(arrayToString(array));
	}
	public static void printArray(byte[] array) {
		System.out.println(arrayToString(array));
	}
	
	public static <T> void printArrayList(ArrayList<T> list) {
		System.out.print("[ ");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).toString() + " ");
		}
		System.out.println("]");
	}
	
	public static <T> ArrayList<T> clone(ArrayList<T> list) {
		ArrayList<T> newList = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			newList.add(list.get(i));
		}
		return newList;
	}
	public static <T> boolean contains(ArrayList<T> list, T element) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(element)) return true;
		}
		return false;
	}
	public static <T> T[] shuffle(T[] array, Random rand) {
		T[] shuffled = array.clone();
		for (int i = 0; i < shuffled.length; i++) {
			int indexToSwap = rand.nextInt(shuffled.length);
			T temp = shuffled[indexToSwap];
			shuffled[indexToSwap] = shuffled[i];
			shuffled[i] = temp;
		}
		return shuffled;
	}
	
	public static byte[] addElement(byte[] current, byte element) {
		byte[] concatenatedArray = new byte[current.length + 1];
		for (int i = 0; i < concatenatedArray.length; i++) {
			if (current.length > i) {
				concatenatedArray[i] = current[i];
			} else {
				concatenatedArray[i] = element;
			}
		}
		return concatenatedArray;
	}
	public static byte[] concat(byte[] arr1, byte[] arr2) {
		byte[] combined = new byte[arr1.length + arr2.length];
		for (int i = 0; i < arr1.length; i++) {
			combined[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			combined[i + arr1.length] = arr2[i];
		}
		return combined;
	}
	// endIndex is exclusive
	public static byte[] splice(byte[] array, int startIndex, int endIndex) {
		byte[] newArr = new byte[endIndex - startIndex];
		int newIndex = 0;
		for (int i = startIndex; i < endIndex; i++) {
			newArr[newIndex] = array[i];
			newIndex++;
		}
		return newArr;
	}
}
