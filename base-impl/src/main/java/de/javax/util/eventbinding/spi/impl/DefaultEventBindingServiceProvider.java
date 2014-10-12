package de.javax.util.eventbinding.spi.impl;

import java.util.Set;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceProvider;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;

/**
 * The default implementation of the event binding service.
 *
 * @author Frank Hardy
 */
public class DefaultEventBindingServiceProvider implements EventBindingServiceProvider {
	
	private final EventTargetCollector eventTargetCollector = new DefaultEventTargetCollector(
	        new DefaultEventTargetCollector.DefaultEventTargetFactory());

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
