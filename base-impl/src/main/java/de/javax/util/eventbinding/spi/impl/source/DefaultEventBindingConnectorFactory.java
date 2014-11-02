package de.javax.util.eventbinding.spi.impl.source;

import java.util.Iterator;
import java.util.ServiceLoader;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.source.EventBindingConnectorFactory;

/**
 * The default implementation of an {@link EventBindingConnectorFactory}.<br/>
 * This implementation is an aggregator which uses the {@link ServiceLoader}
 * facility of the JDK to load all available implementations of
 * {@link EventBindingConnectorFactory}. Calls to
 * {@link #createConnector(Object, Class)} will be delegated to each of the
 * found factory implementations. The first {@link EventBindingConnector}
 * instance returned by one of the factory implementations will be returned by
 * this factory.
 * 
 * @author Matthias Hanish
 * @author Frank Hardy
 */
public class DefaultEventBindingConnectorFactory implements EventBindingConnectorFactory {

    private ServiceLoader<EventBindingConnectorFactory> serviceLoader;

    @Override
    public EventBindingConnector createConnector(Object eventSource, Class<?> eventType) {
        if (this.serviceLoader == null) {
            this.serviceLoader = ServiceLoader.load(EventBindingConnectorFactory.class);
        }

        Iterator<EventBindingConnectorFactory> it = this.serviceLoader.iterator();
        while (it.hasNext()) {
            EventBindingConnector adapter = it.next().createConnector(eventSource, eventType);
            if (adapter != null) {
                return adapter;
            }
        }
        return null;
    }
}
