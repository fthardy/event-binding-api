package de.javax.util.eventbinding.source;

/**
 * Creates instances of {@link EventBindingConnector}.
 * 
 * @author Frank Hardy
 */
public interface EventBindingConnectorFactory {

	/**
	 * Creates a new instance of a connector.
	 * 
	 * @param eventSource
	 *            the event source object.
	 * @param eventType
	 *            the type of event to listen for.
	 * 
	 * @return a new instance of an event listener adapter or <code>null</code>
	 *         if none can be provided for the given event source and event
	 *         type.
	 */
	EventBindingConnector createConnector(Object eventSource, Class<?> eventType);
}
