package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Represents a target for events in a event binding.
 * 
 * @author Frank Hardy
 * 
 * @see EventTargetCollector
 */
public interface EventTarget {

    /**
     * @return the type of events supported by the receiving event target.
     */
    Class<?> getEventType();

    /**
     * @return the event source identifier selector which is used to select the
     *         event sources by their identifiers.
     */
    EventSourceIdSelector getEventSourceIdSelector();

    /**
     * @return the event dispatcher used to dispatch the events from the event
     *         source to the event target.
     */
    EventDispatcher getEventDispatcher();

    /**
     * @return the unmodifieable set of the bound event sources.
     */
    Set<EventSource> getBoundSources();

    /**
     * Set the event sources which have been bound to the receiving event
     * target.
     * 
     * @param boundSources
     *            the set of the bound sources.
     */
    void setBoundSources(Set<EventSource> boundSources);

    /**
     * Unbind the receiving event target from all bound event sources.
     */
    void unbindFromSources();
}
