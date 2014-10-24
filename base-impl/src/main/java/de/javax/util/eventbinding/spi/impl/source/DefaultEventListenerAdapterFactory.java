package de.javax.util.eventbinding.spi.impl.source;

import java.util.Iterator;
import java.util.ServiceLoader;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.source.EventListenerAdapterFactory;

/**
 * The default implementation of an {@link EventListenerAdapterFactory}.<br/>
 * This implementation is an aggregator which uses the {@link ServiceLoader}
 * facility of the JDK to load all available implementations of
 * {@link EventListenerAdapterFactory}. Calls to
 * {@link #createEventListenerAdapter(Object, Class)} will be delegated to each
 * of the found factory implementations. The first {@link EventListenerAdapter}
 * will be returned by this factory.
 * 
 * @author Matthias Hanish
 * @author Frank Hardy
 */
public class DefaultEventListenerAdapterFactory implements EventListenerAdapterFactory {

    private ServiceLoader<EventListenerAdapterFactory> serviceLoader;

    @Override
    public EventListenerAdapter createEventListenerAdapter(Object eventSource, Class<?> eventType) {
        if (this.serviceLoader == null) {
            this.serviceLoader = ServiceLoader.load(EventListenerAdapterFactory.class);
        }

        Iterator<EventListenerAdapterFactory> it = this.serviceLoader.iterator();
        while (it.hasNext()) {
            EventListenerAdapter adapter = it.next().createEventListenerAdapter(eventSource, eventType);
            if (adapter != null) {
                return adapter;
            }
        }
        return null;
    }
}
