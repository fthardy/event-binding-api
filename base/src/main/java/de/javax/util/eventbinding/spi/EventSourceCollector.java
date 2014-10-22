package de.javax.util.eventbinding.spi;

/**
 * Collects for a given event target all matching event sources from a given
 * event source provdier object and binds them to the event target.
 * 
 * @author Frank Hardy
 */
public interface EventSourceCollector {

    /**
     * Bind the given target to the event sources of the given event source
     * provider object.
     * 
     * @param eventTarget
     *            the event target to bind to the event sources of the receiving
     *            event source provider.
     * @param eventSourceProvider
     *            the event source provider object.
     * 
     * @return <code>true</code> if at least one event source has been bound to
     *         the given event target.
     */
    boolean bindTargetToSources(EventTarget eventTarget, Object eventSourceProvider);
}
