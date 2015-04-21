package de.javax.util.eventbinding.impl;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.EventBindingException;
import de.javax.util.eventbinding.UnboundEventTargetsException;
import de.javax.util.eventbinding.spi.EventBindingFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;

/**
 * The default implementation of an event binder.<br/>
 * This implementation defines the top level event binding process. The concrete implementation is based on the
 * interfaces of the SPI.
 * 
 * @author Frank Hardy
 */
public class DefaultEventBinder implements EventBinder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EventSourceCollector sourceCollector;
    private final EventTargetCollector targetCollector;
    private final EventBindingFactory bindingFactory;

    private boolean strictBindingMode;

    @Inject
    public DefaultEventBinder(
    		EventSourceCollector sourceCollector, EventTargetCollector targetCollector, EventBindingFactory eventBindingFactory) {
        this.sourceCollector = sourceCollector;
        this.targetCollector = targetCollector;
        this.bindingFactory = eventBindingFactory;
    }

    @Override
    public EventBinding bind(Object sourceProvider, Object targetProvider) throws EventBindingException {
        logger.debug("Binding sources of source provider [{}] with targets of target provider [{}]", sourceProvider, targetProvider);
        
        Set<EventSource> foundSources = this.sourceCollector.collectEventSourcesFrom(
                sourceProvider);
        if (foundSources.isEmpty()) {
            throw new EventBindingException("No event sources found!");
        }

        Set<EventTarget> foundTargets = this.targetCollector.collectEventTargetsFrom(
                targetProvider);
        if (foundTargets.isEmpty()) {
            throw new EventBindingException("No event targets found!");
        }

        return this.bindingFactory.createEventBinding(this, sourceProvider, targetProvider,
                this.bindTargetsToSources(foundTargets, foundSources));
    }

    @Override
    public boolean isInStrictBindingMode() {
        return this.strictBindingMode;
    }

    @Override
    public void setInStrictBindingMode(boolean strictBinding) {
        this.strictBindingMode = strictBinding;
    }

    private Set<EventTarget> bindTargetsToSources(Set<EventTarget> eventTargets, Set<EventSource> eventSources) {
    	logger.debug("Trying to bind {} sources with {} targets.", eventSources.size(), eventTargets.size());
    	
        Set<EventTarget> boundTargets = new HashSet<EventTarget>();
        Set<EventTarget> unboundTargets = new HashSet<EventTarget>();

        for (EventTarget eventTarget : eventTargets) {
            for (EventSource eventSource : eventSources) {
                eventSource.bindTo(eventTarget);
            }

            if (eventTarget.isBound()) {
                boundTargets.add(eventTarget);
            } else if (this.strictBindingMode) {
                unboundTargets.add(eventTarget);
            } else {
                logger.warn("Target [{}] was not bound!", eventTarget);
            }
        }

        if (boundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(eventTargets);
        }
        if (this.strictBindingMode && !unboundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(unboundTargets);
        }
        return boundTargets;
    }
}
