package de.javax.util.eventbinding.spi.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of a ClassInfoCache using a HashMap to store information about classes.
 * 
 * @author Matthias Hanisch
 */
public class SimpleClassInfoCache<T> implements ClassInfoCache<T> {

    private final Map<Class<?>, T> cache = new HashMap<Class<?>, T>();

    @Override
    public void put(Class<?> clazz, T info) {
        this.cache.put(clazz, info);
    }

    @Override
    public T get(Class<?> clazz) {
        return this.cache.get(clazz);
    }

    @Override
    public boolean hasKey(Class<? extends Object> key) {
    	return this.cache.containsKey(key);
    }
    
    @Override
    public boolean hasNotKey(Class<?> key) {
    	return !this.cache.containsKey(key);
    }
}
