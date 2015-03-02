package de.javax.util.eventbinding.spi.impl;

/**
 * Interface for a cache storing information about classes using the class
 * itself as key.
 * 
 * @author Matthias Hanisch
 *
 * @param <T>
 *            The type of information to be stored in the cache.
 */
public interface ClassInfoCache<T> {

	/**
	 * Stores information about a class into the cache.
	 * 
	 * @param clazz
	 *            The class to store information about.
	 * @param info
	 *            The information to be stored.
	 */
	void put(Class<?> clazz, T info);

	/**
	 * Returns information about a class from this cache.
	 * 
	 * @param clazz
	 *            The class to return information about.
	 * @return The information stored or <code>null</code> if no information
	 *         exists about the given class.
	 */
	T get(Class<?> clazz);

	/**
	 * Check if the cache has an object for a given key.
	 * 
	 * @param key
	 *            the key object to look for.
	 *            
	 * @return <code>true</code> if there is an object in the cache for the
	 *         given key.
	 */
	boolean hasKey(Class<? extends Object> key);
	
	/**
	 * Check if the receiving cache instance has no value for the given key.
	 * 
	 * @param key
	 *            the key to check for.
	 * 
	 * @return <code>true</code> if no value exists in the receiving cache for
	 *         the given key.
	 */
	boolean hasNotKey(Class<?> key);
}
