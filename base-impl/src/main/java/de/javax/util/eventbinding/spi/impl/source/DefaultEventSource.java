package de.javax.util.eventbinding.spi.impl.source;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Default implementation of EventSource delegating 
 * the register and unregister calls to the 
 * EventListenerAdapter which knows how to handle
 * the register/unregister process for a certain event source.
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSource implements EventSource {

    private final EventListenerAdapter adapter;

    public DefaultEventSource(EventListenerAdapter adapter) {
        if(adapter==null) {
            throw new NullPointerException("no EventListenerAdapter provided");
        }
        this.adapter = adapter;
    }
    
    @Override
    public void unbindFrom(EventTarget target) {
        this.adapter.unregisterEventListener();
    }
}
