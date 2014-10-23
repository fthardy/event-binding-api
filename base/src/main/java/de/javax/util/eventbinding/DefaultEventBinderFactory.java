package de.javax.util.eventbinding;

import java.util.Iterator;
import java.util.ServiceLoader;

import de.javax.util.eventbinding.spi.EventBindingServiceProvider;

/**
 * The default implementation of an event binder factory.<br/>
 * This implementation is based on the service loader facility of the JDK. 
 *
 * @author Frank Hardy
 */
public class DefaultEventBinderFactory implements EventBinderFactory {

	@Override
	public EventBinder createEventBinder() {
		ServiceLoader<EventBindingServiceProvider> serviceLoader = ServiceLoader.load(EventBindingServiceProvider.class);
		Iterator<EventBindingServiceProvider> serviceIterator = serviceLoader.iterator();
		if (!serviceIterator.hasNext()) {
			throw new IllegalStateException("No implementation found for event binding service!");
		}
		
		return new DefaultEventBinder(serviceIterator.next());
	}
}
