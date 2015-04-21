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

	private EventBindingConnectorFactory instance;

	@Override
	public EventBindingConnector createConnector(Object eventSource, Class<?> eventType) {
		EventBindingConnector connector = null;
		if (this.instance == null) {
			ServiceLoader<EventBindingConnectorFactory> serviceLoader = ServiceLoader.load(EventBindingConnectorFactory.class);
			Iterator<EventBindingConnectorFactory> it = serviceLoader.iterator();
			while (it.hasNext()) {
				EventBindingConnectorFactory factoryImpl = it.next();
				connector = factoryImpl.createConnector(eventSource, eventType);
				if (connector != null) {
					this.instance = factoryImpl;
					break;
				}
			}
		} else {
			connector = this.instance.createConnector(eventSource, eventType);
		}
		return connector;
	}
}
