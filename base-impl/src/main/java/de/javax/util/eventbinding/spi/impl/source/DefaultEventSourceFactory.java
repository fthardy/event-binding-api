package de.javax.util.eventbinding.spi.impl.source;

import de.javax.util.eventbinding.source.EventListenerAdapterFactory;
import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * TODO
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceFactory implements EventSourceFactory {
    
    private final EventListenerAdapterFactory listenerAdapterFactory;
    
    /**
     * Creates a new instance of this factory.
     * 
     * @param listenerAdapterFactory
     *            the listener adapter factory.
     */
    public DefaultEventSourceFactory(EventListenerAdapterFactory listenerAdapterFactory) {
        if (listenerAdapterFactory == null) {
            throw new NullPointerException("Undefined listener adapter factory!");
        }
        this.listenerAdapterFactory = listenerAdapterFactory;
    }

    @Override
    public EventSource createEventSource(EventSourceId eventSourceId, Object eventSourceProvider) {
        return new DefaultEventSource(eventSourceId, eventSourceProvider, this.listenerAdapterFactory);
    }
}
