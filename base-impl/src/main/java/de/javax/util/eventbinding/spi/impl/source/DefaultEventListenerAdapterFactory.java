package de.javax.util.eventbinding.spi.impl.source;

import java.util.Iterator;
import java.util.ServiceLoader;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.source.EventListenerAdapterFactory;
import de.javax.util.eventbinding.source.EventListenerProvider;

/**
 * TODO
 * 
 * @author Frank Hardy
 */
public class DefaultEventListenerAdapterFactory implements EventListenerAdapterFactory {

    private ServiceLoader<EventListenerProvider> serviceLoader;
    
    @Override
    public EventListenerAdapter createEventListenerAdapter(Object eventSource, Class<?> eventType) {
        if (this.serviceLoader == null) {
            this.serviceLoader = ServiceLoader.load(EventListenerProvider.class);
        }
        
        Iterator<EventListenerProvider> it = this.serviceLoader.iterator();
        while (it.hasNext()) {
            EventListenerAdapter adapter = it.next().createEventListenerAdapter(eventSource, eventType);
            if (adapter != null) {
                return adapter;
            }
        }
        return null;
    }
}
