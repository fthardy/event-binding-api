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
     * @return a simple textual description of the receiving target.
     */
    String getDescription();

    /**
     * @return the event type class which the receiving event target can handle.
     */
    Class<?> getEventClass();

    /**
     * @return the event dispatcher used to dispatch the events from the event source to the event target.
     */
    EventDispatcher getEventDispatcher();

    /**
     * Add a bound event source to the receiving event target.
     * 
     * @param source
     *            the bound event source.
     */
    void addBoundSource(EventSource source);

    /**
     * @return the set of the bound event sources. The set is empty when no sources are bound to the receiving event
     *         target.
     */
    Set<EventSource> getBoundSources();

    /**
     * Check if the receiving event target is bound to at least one event source.
     * 
     * @return <code>true</code> if the receiving event target is bound to an event source. Otherwise <code>false</code>
     *         .
     */
    boolean isBound();

    /**
     * Unbind the receiving event target from all bound event sources.
     */
    void unbindFromSources();

    /**
     * Removes a bound event source from this event target.
     * 
     * @param eventSource
     *            The bound event source.
     */
    void removeBoundSource(EventSource eventSource);

    /**
     * Returns if this given EventSource accepts this EventTarget in terms of the defined id for event source or event
     * target. It does <b>not</b> check if the event target can be bound to the event source regarding the event type or
     * the mechanism to register the event type.
     * 
     * @return <code>true</code> if the given event source is accepted by this event target, <code>false</code>
     *         otherwise.
     */
    boolean accepts(EventSource eventSource);

}
