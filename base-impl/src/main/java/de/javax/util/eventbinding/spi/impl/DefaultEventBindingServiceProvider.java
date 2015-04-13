package de.javax.util.eventbinding.spi.impl;

import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory;
import de.javax.util.eventbinding.spi.impl.target.metadata.DefaultHandlerMethodDescriptorCollector;
import de.javax.util.eventbinding.spi.impl.target.metadata.DefaultTargetProviderClassAnalyzer;

/**
 * The default implementation of the event binding service.
 *
 * @author Frank Hardy
 */
public class DefaultEventBindingServiceProvider implements EventBindingServiceProvider {

    private final EventTargetCollector eventTargetCollector;

    private final EventSourceCollector eventSourceCollector = new DefaultEventSourceCollector(
            new DefaultEventSourceFactory(new DefaultEventBindingConnectorFactory()));
    
    public DefaultEventBindingServiceProvider() {
		this.eventTargetCollector = new DefaultEventTargetCollector(
	            new DefaultTargetProviderClassAnalyzer(new DefaultHandlerMethodDescriptorCollector()),
	            new DefaultMethodEventTargetFactory());
	}

    @Override
    public EventTargetCollector getEventTargetCollector() {
        return this.eventTargetCollector;
    }

    @Override
    public EventSourceCollector getEventSourceCollector() {
        return this.eventSourceCollector;
    }

    @Override
    public EventBinding createEventBinding(
            EventBinder binder, Object source, Object target, Set<EventTarget> boundEventTargets) {
        return new DefaultEventBinding(binder, source, target, boundEventTargets);
    }
}
