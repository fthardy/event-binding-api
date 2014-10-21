package de.javax.util.eventbinding.spi.impl.source;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Factory providing all {@link EventListenerProvider} available.
 * @author Matthias Hanisch
 * @TODO: MATHAN move to DefaultEventSourceProvider to avoid static declarations
 */
public class EventListenerProviderFactory {

    /**
     * Retuns an Iterator of all available {@link EventListenerProvider}.
     * @return
     */
    public static Iterator<EventListenerProvider> getEventListenerAdapterProviders() {
        ServiceLoader<EventListenerProvider> serviceLoader = ServiceLoader.load(EventListenerProvider.class);
        return serviceLoader.iterator();
    }

    /**
     * Creates an EventListenerAdapter handling events of the given type for the given event source by
     * checking all {@link #getEventListenerAdapterProviders() providers}.
     * @param eventSource
     * @param eventType
     * @return An instance of EventListenerAdapter or <code>null</code> if none of the providers
     * available supports event type or event source-
     */
    public static EventListenerAdapter createAdapter(Object eventSource, Class<?> eventType) {
        Iterator<EventListenerProvider> it = getEventListenerAdapterProviders();
        while(it.hasNext()) {
            EventListenerAdapter adapter = it.next().createEventListenerAdapter(eventSource, eventType);
            if(adapter!=null) {
                // TODO cache?
                return adapter;
            }
        }
        return null;
    }
}
