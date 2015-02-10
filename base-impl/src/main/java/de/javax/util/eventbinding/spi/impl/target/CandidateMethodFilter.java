package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

/**
 * The interface for a filter which decides whether a given method is an
 * accepted candidate for an event handler method.
 *
 * @author Frank Hardy
 * 
 * @see DefaultHandlerMethodInfoCollector
 */
public interface CandidateMethodFilter {

	/**
	 * Check if the given method is accepted as handler method candidate by the
	 * receiving filter implementation.
	 * 
	 * @param method
	 *            the method to be checked.
	 * 
	 * @return <code>true</code> when the method is accepted as a handler method
	 *         candidate.
	 */
	boolean acceptMethod(Method method);
}