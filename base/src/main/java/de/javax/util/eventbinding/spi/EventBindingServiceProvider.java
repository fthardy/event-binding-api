package de.javax.util.eventbinding.spi;

import java.util.Set;

import de.javax.util.eventbinding.EventBinding;

/**
 * The service provider interface for the event binding.
 *
 * @author Frank Hardy
 */
public interface EventBindingServiceProvider {

	/**
	 * @return the event target collector instance.
	 */
	EventTargetCollector getEventTargetCollector();

	/**
	 * Creates a new event source provider for a given source object.
	 * 
	 * @param source
	 *            the object representing a source of events.
	 * 
	 * @return a new event source provider instance.
	 */
	EventSourceProvider createEventSourceProvider();
	
	/**
	 * Create a new event binding context instance.
	 * 
	 * @param source
	 *            the source object.
	 * @param target
	 *            the target object.
	 * @param boundEventTargets
	 *            the set of bound event targets.
	 * 
	 * @return the new event binding context instance.
	 */
	EventBinding createEventBinding(Object source, Object target, Set<EventTarget> boundEventTargets);
}
