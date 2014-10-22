package de.javax.util.eventbinding.spi;

/**
 * Represents a source of events in an event binding.
 * 
 * @author Frank Hardy
 */
public interface EventSource {

    /**
     * Unbind the given event target from the receiving event source.
     * 
     * @param target
     *            the event target to be unbound from the receiving event
     *            source.
     */
    void unbindFrom(EventTarget target);
}
