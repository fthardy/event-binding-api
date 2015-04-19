package de.javax.util.eventbinding.spi;

/**
 * Represents a source of events in an event binding.
 * 
 * @author Frank Hardy
 */
public interface EventSource {

    /**
     * @return the identifier of the receiving event source.
     */
    EventSourceId getId();

    /**
     * @return a textual description of the receiving event source.
     */
	String getDescription();

    /**
     * Bind a given event target to the receiving event source.
     * 
     * @param eventTarget
     *            the event target to be bound to the source.
     *            
     * @return <code>true</code> when the receiving event source was bound to the given event target.
     */
    void bindTo(EventTarget eventTarget);

    /**
     * Unbind the given event target from the receiving event source.
     * 
     * @param target
     *            the event target to be unbound from the receiving event
     *            source.
     */
    void unbindFrom(EventTarget target);
}
