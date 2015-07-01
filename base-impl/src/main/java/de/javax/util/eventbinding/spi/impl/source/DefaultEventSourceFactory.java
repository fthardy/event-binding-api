package de.javax.util.eventbinding.spi.impl.source;

import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * The default implementation of the {@link EventSourceFactory}.<br/>
 * Creates instances of {@link DefaultEventSource}.
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceFactory implements EventSourceFactory {

    /**
     * Creates a new instance of this factory.
     * 
     */
    public DefaultEventSourceFactory() {
    }

    @Override
    public EventSource createEventSource(EventSourceId eventSourceId, Object eventSourceProvider) {
        return new DefaultEventSource(eventSourceId, eventSourceProvider);
    }
}
