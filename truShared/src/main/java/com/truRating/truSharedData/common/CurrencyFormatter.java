package com.truRating.truSharedData.common;


public class CurrencyFormatter {

	private static int defaultCurrencyCode = 826;
	private static String defaultCurrencySymbol = "�";

	public static void configureFrom(IProperties properties) {
		defaultCurrencyCode = properties.getIntValue("defaultCurrencyCode", 826);
		defaultCurrencySymbol = properties.getValue("defaultCurrencySymbol", "�");
	}

	public static int getDefaultCurrencyCode() {
		return defaultCurrencyCode;
	}

	public static String getCurrencySymbol(int currencyCode) {
		switch (currencyCode) {
		case 826:
			return "�";
		case 978:
			return "�";
		case 840:
			return "$";
		default:
			return defaultCurrencySymbol;
		}
	}

	public static String getDefaultCurrencySymbol() {
		return defaultCurrencySymbol;
	}

	public static double toDouble(long value, int exponent) {
		double mult = Math.pow(10, exponent);
		double cost = (((double) value) / mult);
		return cost;
	}

	public static long toLong(double cost, int exponent) {
		double mult = Math.pow(10, exponent);
		long result = (long) (cost * mult);
		return result;
	}

	public static String toString(double cost, int exponent) {
		String amount = Double.toString(cost);
		if (exponent > 0) {
			amount += ".0";
			String[] amounts = StringUtilities.split(amount, ".");
			if (amounts[0].length() == 0)
				amounts[0] = "0";
			amounts[1] = (amounts[1] + "00000").substring(0, 2);
			amount = amounts[0] + "." + amounts[1];
		}
		return amount;
	}

	public static String toString(long value, int exponent) {
		return toString(toDouble(value, exponent), exponent);
	}
}
