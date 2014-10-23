package de.javax.util.eventbinding.source;


/**
 * A EventListenerAdapterProvider provides EventListenerAdapter to register
 * an EventListener of a certain event on a given object.
 * @author Matthias Hanisch
 *
 */
public interface EventListenerProvider {

    /**
     * Returns an EventListenerAdapter which can handle events of the given event type for
     * the given event source.
     * @param eventSource
     * @param eventType
     * @return An instance of EventListenerAdapter or <code>null</code> if the provider
     * does not support event type or event source.
     */
    EventListenerAdapter createEventListenerAdapter(Object eventSource, Class<?> eventType);
}
