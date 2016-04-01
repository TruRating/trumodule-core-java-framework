package com.truRating.truSharedData.common;

public interface IProperties {

	public abstract boolean containsKey(String key);

	public abstract String getValue(String key, String defaultValue);

	public abstract void setValue(String key, String value);

	public abstract void read(String filename) throws TRException;

	public abstract void write(String filename);

	/**
	 * Get the int value of a property
	 * 
	 * @param string
	 * @return the int value, or -1 if not present
	 */
	public abstract int getIntValue(String key, int defaultValue);

	/**
	 * Get the bool value of a named property
	 * 
	 * @param string
	 * @return the bool value or false if not present
	 */
	public abstract boolean getBoolValue(String key, boolean defaultValue);

}