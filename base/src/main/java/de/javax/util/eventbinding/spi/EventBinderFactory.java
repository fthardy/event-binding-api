package de.javax.util.eventbinding.spi;

import de.javax.util.eventbinding.EventBinder;

/**
 * A factory for creating event binder instances.
 *
 * @author Frank Hardy
 */
public interface EventBinderFactory {

	/**
	 * Create a new event binder.
	 * 
	 * @return the new event binder instance.
	 */
	EventBinder createEventBinder();
}
