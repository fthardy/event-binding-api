package de.javax.util.eventbinding.spi;

import java.util.Set;

import de.javax.util.eventbinding.EventBinding;

/**
 * The interface definition of the service provider for the event binding.
 *
 * @author Frank Hardy
 */
public interface EventBindingServiceProvider {

    /**
     * @return the event target collector instance.
     */
    EventTargetCollector getEventTargetCollector();

    /**
     * @return the event source collector instance.
     */
    EventSourceCollector getEventSourceCollector();

    /**
     * Create a new event binding context instance.
     * 
     * @param sourceProvider
     *            the event source provider object.
     * @param targetProvider
     *            the event target provider object.
     * @param boundEventTargets
     *            the set of bound event targets.
     * 
     * @return the new event binding context instance.
     */
    EventBinding createEventBinding(Object sourceProvider, Object targetProvider, Set<EventTarget> boundEventTargets);
}
