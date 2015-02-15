package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;

/**
 * This default candidate method collector implementation gets all public
 * methods from a given class. The methods are filtered by a
 * {@link CandidateMethodFilter} and a {@link HandlerMethodInfoExtractor} is
 * used to get the handler method info objects from the methods that where
 * accepted by the filter.
 *
 * @author Frank Hardy
 */
public class DefaultHandlerMethodInfoCollector implements HandlerMethodInfoCollector {
	
	private final CandidateMethodFilter candidateMethodFilter;
	private final HandlerMethodInfoExtractor handlerMethodInfoExtractor;
	
	/**
	 * Creates a new instance of this implementation with the default
	 * implementations for the candidate method filter and handler method info
	 * extractor.
	 * 
	 * @param idSelectorFactory
	 *            the ID selector factory.
	 */
	public DefaultHandlerMethodInfoCollector(EventSourceIdSelectorFactory idSelectorFactory) {
		this(new DefaultCandidateMethodFilter(), new DefaultHandlerMethodInfoExtractor(idSelectorFactory));
	}
	
	/**
	 * Creates a new instance of this implementation.
	 * 
	 * @param candidateMethodFilter
	 *            the method filter implementation to be used by this collector.
	 * @param handlerMethodInfoExtractor
	 *            the extractor implementation to be used by this collector.
	 */
	public DefaultHandlerMethodInfoCollector(
			CandidateMethodFilter candidateMethodFilter, HandlerMethodInfoExtractor handlerMethodInfoExtractor) {
		if (candidateMethodFilter == null) {
			throw new NullPointerException("Undefined candidate method filter!");
		}
		if (handlerMethodInfoExtractor == null) {
			throw new NullPointerException("Undefined handler method info extractor!");
		}
		this.candidateMethodFilter = candidateMethodFilter;
		this.handlerMethodInfoExtractor = handlerMethodInfoExtractor;
	}
	
	@Override
	public Set<HandlerMethodInfo> collectHandlerMethodInfosFrom(Class<?> eventTargetProviderClass) {
		Set<HandlerMethodInfo> infos = new HashSet<HandlerMethodInfo>();
		for (Method method : eventTargetProviderClass.getMethods()) {
			if (this.candidateMethodFilter.acceptMethod(method)) {
				HandlerMethodInfo handlerMethodInfo = this.handlerMethodInfoExtractor.extractHandlerMethodInfo(method);
				if (handlerMethodInfo != null) {
					infos.add(handlerMethodInfo);
				}
			}
		}
		return infos;
	}
}