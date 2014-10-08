package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Represents a provider of event sources for an event binding.
 *
 * @author Frank Hardy
 */
public interface EventSourceProvider {

	/**
	 * Find a particular event source which has the given identifier and
	 * supports the given event type.
	 * 
	 * @param eventSourceId
	 *            the identifier of the event source.
	 * @param eventType
	 *            the type of event expected from the event source.
	 * 
	 * @return the event source or <code>null</code> if no such event source
	 *         exists.
	 */
	EventSource findEventSource(String eventSourceId, Class<?> eventType);

	/**
	 * Find a set of event sources which support events of the given type.
	 * 
	 * @param eventType
	 *            the type of the expected events.
	 * 
	 * @return a set of event sources or an emtpy set if none are found.
	 */
	Set<EventSource> findEventSourcesByType(Class<?> eventType);
}
