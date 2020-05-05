package com.dpSoftware.fp.util;

import java.util.Random;

public enum Directions {
	Up, 
	Down, 
	Left, 
	Right;

	public Directions opposite() {
		switch (this) {
			case Up:
				return Down;
			case Down:
				return Up;
			case Left:
				return Right;
			case Right:
				return Left;
		}
		return null;
	}
	public static Directions random(Random random) {
		return ArrayUtils.random(values(), random);
	}
}
