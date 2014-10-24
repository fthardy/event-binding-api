package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Collects for a given event target all matching event sources from a given
 * event source provdier object and binds them to the event target.
 * 
 * @author Frank Hardy
 */
public interface EventSourceCollector {

    /**
     * Collect the event sources from a given event source provider.
     * 
     * @param eventSourceProvider
     *            the event source provider object.
     * 
     * @return a set of event sources. If no event sources are found an empty
     *         set has to be returned.
     */
    Set<EventSource> collectEventSourcesFrom(Object eventSourceProvider);
}
