package de.javax.util.eventbinding.spi;

/**
 * An event dispatcher is used to encapsulate a real event handler.
 * 
 * @author Frank Hardy
 */
public interface EventDispatcher {

	/**
	 * Dispatch a given event to the encapsulated event handler.
	 * 
	 * @param event
	 *            the event to be dispatched.
	 */
	void dispatchEvent(Object event);
}
