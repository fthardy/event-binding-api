package de.javax.util.eventbinding.source;

/**
 * Creates instances of event listener adapters.
 * 
 * @author Frank Hardy
 */
public interface EventListenerAdapterFactory {

    /**
     * Creates a new instance of an event listener adapter.
     * 
     * @param eventSource
     *            the event source object.
     * @param eventType
     *            the type of event to listen for.
     *            
     * @return a new instance of an event listener adapter.
     */
    EventListenerAdapter createEventListenerAdapter(Object eventSource, Class<?> eventType);
}
