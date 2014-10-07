package de.javax.util.eventbinding.spi;

/**
 * Represents a target for events in a event binding.
 * 
 * @author Frank Hardy
 * 
 * @see EventTargetCollector
 */
public interface EventTarget {

	/**
	 * Bind the receiving event target to a given event source.
	 * 
	 * @param sourceProvider
	 *            the event source provider.
	 * 
	 * @return <code>true</code> when the receiving event target has been bound
	 *         to at least one event source.
	 */
	boolean bindToSourcesOf(EventSourceProvider sourceProvider);

	/**
	 * Release all source bindings of the receiving event target.
	 */
	void release();
}
