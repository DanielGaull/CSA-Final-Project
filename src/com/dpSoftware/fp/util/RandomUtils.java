package com.dpSoftware.fp.util;

import java.util.Random;

public class RandomUtils {

	public static int randomIntBetween(Random rand, int min, int max) {
		max++; // Includes the upper bound
		return rand.nextInt(max - min) + min;
	}
	public static double randomDoubleBetween(Random rand, double min, double max) {
		return rand.nextDouble() * (max - min) + min;
	}
	
	public static boolean doesChanceSucceed(Random rand, double chance) {
		int decimalPlaces = getDecimalPlaces(chance);
		int numberMult = (int) Math.pow(10, decimalPlaces);
		double denominator = Math.pow(10, decimalPlaces + 2);
		double numerator = Math.floor(chance * numberMult);
		return numerator > randomIntBetween(rand, 0, (int) denominator - 1);
	}
	
	private static int getDecimalPlaces(double num) {
		String text = Double.toString(Math.abs(num));
		int integerPlaces = text.indexOf('.');
		return text.length() - integerPlaces - 1;
	}
}
