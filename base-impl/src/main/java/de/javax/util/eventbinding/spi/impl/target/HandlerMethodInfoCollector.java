package de.javax.util.eventbinding.spi.impl.target;

import java.util.Set;

import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;

/**
 * A strategy interface used by {@link DefaultEventTargetCollector} to collect
 * the {@link HandlerMethodInfo} objects from a given event target provider
 * class.
 *
 * @author Frank Hardy
 * 
 * @see DefaultEventTargetCollector
 */
public interface HandlerMethodInfoCollector {

	/**
	 * Collect the handler method info objects from a given class.
	 * 
	 * @param eventTargetProviderClass
	 *            the class of the event target provider object.
	 * 
	 * @return the set with the collected handler method info objects.
	 */
	Set<HandlerMethodInfo> collectHandlerMethodInfosFrom(Class<?> eventTargetProviderClass);
}