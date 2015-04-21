package de.javax.util.eventbinding;

import java.util.Iterator;
import java.util.ServiceLoader;

import de.javax.util.eventbinding.spi.EventBinderFactory;

/**
 * Creates instances of {@link EventBinder}. 
 *
 * @author Frank Hardy
 */
public class EventBinderInstanceFactory {

	/**
	 * Create a new instance of an event binder.
	 * 
	 * @return the new instance.
	 */
	public static EventBinder newEventBinderInstance() {
		ServiceLoader<EventBinderFactory> serviceLoader = ServiceLoader.load(EventBinderFactory.class);
		Iterator<EventBinderFactory> serviceIterator = serviceLoader.iterator();
		if (!serviceIterator.hasNext()) {
			throw new IllegalStateException("No implementation found for the event binding factory!");
		}
		return serviceIterator.next().createEventBinder();
	}
	
	private EventBinderInstanceFactory() {
		// No instance creation
	}
}
