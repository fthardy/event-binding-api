package de.javax.util.eventbinding.spi.impl;

/**
 * The interface definition for an event source identifier selector.
 * 
 * @author Frank Hardy
 */
public interface EventSourceIdSelector {
    
    /**
     * Check if a given event source identifier is matching this pattern.
     * 
     * @param sourceId
     *            the event source identifier to be checked.
     *            
     * @return <code>true</code> if the given identifier matches the pattern.
     */
    boolean matches(EventSourceId sourceId);
}