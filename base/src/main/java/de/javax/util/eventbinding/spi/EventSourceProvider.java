package de.javax.util.eventbinding.spi;


/**
 * Represents a provider of event sources for an event binding.
 * 
 * @author Frank Hardy
 */
public interface EventSourceProvider {

    /**
     * Bind the given target to the event sources of the receiving event source
     * provider.
     * @param source 
     * 
     * @param eventTarget
     *            the event target to bind to the event sources of the receiving
     *            event source provider.
     *            
     * @return <code>true</code> if at least one event source has been bound to
     *         the given event target.
     */
    boolean bindTargetToSources(Object source, EventTarget eventTarget);
}
