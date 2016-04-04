package com.truRating.truSharedData.properties;

import com.truRating.truSharedData.common.TRException;

public interface IProperties {

	boolean containsKey(String key);

	String getValue(String key, String defaultValue);

	void setValue(String key, String value);

	void read(String filename) throws TRException;

	void write(String filename);

	/**
	 * Get the int value of a property
	 */
	int getIntValue(String key, int defaultValue);

	/**
	 * Get the bool value of a named property
	 */
	boolean getBoolValue(String key, boolean defaultValue);

}