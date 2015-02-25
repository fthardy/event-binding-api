package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;

/**
 * The interface for an extractor which analyzes a given method for informations.
 * If informations are found it returns them as a {@link HandlerMethodInfo} object.
 *
 * @author Frank Hardy
 * 
 * @see DefaultHandlerMethodInfoCollector
 */
public interface HandlerMethodInfoExtractor {
	
	/**
	 * Extract the handler method informations from the given method.
	 * 
	 * @param method
	 *            the method to analyze.
	 * 
	 * @return the handler method info object or <code>null</code> when the
	 *         given method is not an event handler method.
	 */
	HandlerMethodInfo extractHandlerMethodInfo(Method method);
}