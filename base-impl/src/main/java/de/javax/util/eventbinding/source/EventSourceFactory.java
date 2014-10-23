package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * TODO 
 * 
 * @author Frank Hardy
 */
public interface EventSourceFactory {
    
    /**
     * TODO
     * 
     * @param eventSourceId
     * @param eventSourceProvider
     * 
     * @return
     */
    de.javax.util.eventbinding.spi.EventSource createEventSource(
            EventSourceId eventSourceId, Object eventSourceProvider);
}
