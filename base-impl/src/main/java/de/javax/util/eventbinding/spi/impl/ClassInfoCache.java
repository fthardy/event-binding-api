package de.javax.util.eventbinding.spi.impl;

/**
 * This ClassInfoCache defines an interface for storing informations about a
 * class into a cache.
 * 
 * @author Matthias Hanisch
 *
 * @param <T>
 *            The type of information about a class stored in the cache.
 */
public interface ClassInfoCache<T> {

	void put(Class<?> clazz, T info);

	T get(Class<?> clazz);
}
