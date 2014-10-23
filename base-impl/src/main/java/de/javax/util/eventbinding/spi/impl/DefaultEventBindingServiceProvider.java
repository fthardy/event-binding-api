package de.javax.util.eventbinding.spi.impl;

import java.util.Set;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventListenerAdapterFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory;

/**
 * The default implementation of the event binding service.
 *
 * @author Frank Hardy
 */
public class DefaultEventBindingServiceProvider implements EventBindingServiceProvider {
	
	private final EventTargetCollector eventTargetCollector = new DefaultEventTargetCollector(
	        new DefaultMethodEventTargetFactory(), new DefaultEventSourceIdSelectorFactory() );
	
	private final EventSourceCollector eventSourceCollector = new DefaultEventSourceCollector(
	        new DefaultEventSourceFactory(new DefaultEventListenerAdapterFactory()));

	@Override
	public EventTargetCollector getEventTargetCollector() {
		return this.eventTargetCollector;
	}

	@Override
	public EventSourceCollector getEventSourceCollector() {
		return this.eventSourceCollector;
	}

	@Override
	public EventBinding createEventBinding(Object source, Object target, Set<EventTarget> boundEventTargets) {
		return new DefaultEventBinding(source, target, boundEventTargets);
	}
}
