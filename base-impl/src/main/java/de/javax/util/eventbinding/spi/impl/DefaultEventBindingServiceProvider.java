package de.javax.util.eventbinding.spi.impl;

import java.lang.reflect.Method;
import java.util.Set;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceProvider;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.MethodAdaptingEventDispatcher;
import de.javax.util.eventbinding.spi.impl.target.MethodEventTargetFactory;

/**
 * The default implementation of the event binding service.
 *
 * @author Frank Hardy
 */
public class DefaultEventBindingServiceProvider implements EventBindingServiceProvider {
	
	private final EventTargetCollector eventTargetCollector = new DefaultEventTargetCollector(
	        new MethodEventTargetFactory() {
	            @Override
	            public EventTarget createEventTarget(
	                    Object targetProvider, Method eventHandlerMethod, EventSourceIdSelector pattern) {
	                return new DefaultEventTarget(
	                        pattern,
	                        eventHandlerMethod.getParameterTypes()[0],
	                        new MethodAdaptingEventDispatcher(eventHandlerMethod, targetProvider));
	            }
	        },
	        new EventSourceIdSelectorFactory() {
                @Override
                public EventSourceIdSelector createEventSourceIdSelector(String expression) {
                    return new DefaultEventSourceIdSelector(expression);
                }
            });

	@Override
	public EventTargetCollector getEventTargetCollector() {
		return this.eventTargetCollector;
	}

	@Override
	public EventSourceProvider createEventSourceProvider(Object sourceProvider) {
		return new DefaultEventSourceProvider(sourceProvider);
	}

	@Override
	public EventBinding createEventBinding(Object source, Object target, Set<EventTarget> boundEventTargets) {
		return new DefaultEventBinding(source, target, boundEventTargets);
	}
}
