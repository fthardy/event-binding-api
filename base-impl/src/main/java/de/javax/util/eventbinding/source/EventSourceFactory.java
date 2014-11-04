package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * The interface definition for a factory which is responsible to create
 * instances of {@link EventSource}.
 * 
 * @author Frank Hardy
 */
public interface EventSourceFactory {

	/**
	 * Create a new instance of an event source.
	 * 
	 * @param eventSourceId
	 *            the identifier of the event source.
	 * @param eventSourceObject
	 *            the real event source object.
	 * 
	 * @return the new instance.
	 */
	de.javax.util.eventbinding.spi.EventSource createEventSource(
			EventSourceId eventSourceId, Object eventSourceObject);
}
