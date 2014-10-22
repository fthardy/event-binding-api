package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Analyses an object to find targets for an event binding.
 * 
 * @author Frank Hardy
 * 
 * @see EventTarget
 */
public interface EventTargetCollector {

	/**
	 * Analyses the given object to find targets for an event binding.
	 * 
	 * @param eventTargetProvider
	 *            the event target provider object.
	 * 
	 * @return a set with the found event targets. If no event targets are found
	 *         then <code>null</code> or an emtpy set can be returned.
	 */
	Set<EventTarget> collectEventTargetsFrom(Object eventTargetProvider);
}
