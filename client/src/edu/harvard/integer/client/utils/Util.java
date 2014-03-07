package edu.harvard.integer.client.utils;

public class Util {
	public static final double generateValueWithinBoundary(int maxValue,
			int safeValue) {

		double value = Math.random() * maxValue;

		if (value < safeValue) {
			value = safeValue;
		}
		if (value > (maxValue - safeValue)) {
			value = maxValue - safeValue;
		}
		return value;

	}

	public static final int randomNumber(int value, double factor) {
		return (int) ((Math.random() * factor) * value);
	}

	/**
	 * @param a
	 * @param b
	 * @return a <= random number < b
	 */
	public static final int randomIntBetween(int a, int b) {
		return (int) (a + Math.random() * (b - a));
	}

	public static final double randomDoubleBetween(double a, double b) {
		return (a + Math.random() * (b - a));
	}

	public static final <T> T randomValue(T[] values) {
		return values[randomIntBetween(0, values.length)];
	}
}
