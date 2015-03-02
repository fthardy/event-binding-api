package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.util.Set;

/**
 * A strategy interface used by {@link DefaultTargetProviderClassAnalyzer} to collect the
 * {@link HandlerMethodDescriptor} instances from a given event target provider
 * class.
 *
 * @author Frank Hardy
 * 
 * @see DefaultTargetProviderClassAnalyzer
 */
public interface HandlerMethodDescriptorCollector {

	/**
	 * Collect the handler method descriptor objects from a given class.
	 * 
	 * @param eventTargetProviderClass
	 *            the class of the event target provider object.
	 * 
	 * @return the set with the collected handler method descriptors.
	 */
	Set<HandlerMethodDescriptor> collectHandlerMethodDescriptorsFrom(Class<?> eventTargetProviderClass);
}