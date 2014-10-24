package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Analyses an object to find targets for an event binding.
 * 
 * @author Frank Hardy
 * 
 * @see EventTarget
 */
public interface EventTargetCollector {

    /**
     * Analyses the given object to find targets for an event binding.
     * 
     * @param eventTargetProvider
     *            the event target provider object.
     * 
     * @return a set with the found event targets. If no event targets are found
     *         an emtpy set has to be returned.
     */
    Set<EventTarget> collectEventTargetsFrom(Object eventTargetProvider);
}
