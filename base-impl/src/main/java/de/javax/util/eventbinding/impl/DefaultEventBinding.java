package de.javax.util.eventbinding.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.EventBindingException;
import de.javax.util.eventbinding.UnboundEventTargetsException;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;

/**
 * The default implementation of the event binding.
 *
 * @author Frank Hardy
 */
public class DefaultEventBinding implements EventBinding {
    
	private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EventSourceCollector sourceCollector;
    private final EventTargetCollector targetCollector;
    
    private final boolean strictBindingMode;
    
    private EventSourceAndTargetProvider eventSourceAndTargetProvider;
    private Set<EventTarget> boundTargets;

    public DefaultEventBinding(
    		EventSourceCollector sourceCollector, EventTargetCollector targetCollector, 
    		EventSourceAndTargetProvider providerHolder, boolean strict) {
    	this.sourceCollector = sourceCollector;
    	this.targetCollector = targetCollector;
    	this.eventSourceAndTargetProvider = providerHolder;
    	strictBindingMode = strict;
    	
    	buildBindings();
    }
	
	@Override
	public Object getSourceProvider() {
		verifyState();
		return eventSourceAndTargetProvider.sourceProvider;
	}

	@Override
	public Object getTargetProvider() {
		verifyState();
		return eventSourceAndTargetProvider.targetProvider;
	}

	@Override
	public void release() {
		verifyState();
		for (EventTarget target : boundTargets) {
			target.unbindFromSources();
		}
		boundTargets.clear();
		boundTargets = null;
	}

	@Override
	public boolean isReleased() {
		return eventSourceAndTargetProvider == null;
	}
	
	@Override
	public void rebuild() {
        verifyState();
        release();
        buildBindings();
    }
	
	public Set<EventTarget> getBoundTargets() {
		return boundTargets;
	}

	void buildBindings() {
        logger.debug("Binding sources of event source provider [{}] with targets of event target provider [{}]", 
        		eventSourceAndTargetProvider.sourceProvider, eventSourceAndTargetProvider.targetProvider);
        
        Set<EventSource> foundSources = sourceCollector.collectEventSourcesFrom(eventSourceAndTargetProvider.sourceProvider);
        if (foundSources.isEmpty()) {
            throw new EventBindingException("No event sources found!");
        }

        Set<EventTarget> foundTargets = targetCollector.collectEventTargetsFrom(eventSourceAndTargetProvider.targetProvider);
        if (foundTargets.isEmpty()) {
            throw new EventBindingException("No event targets found!");
        }

        boundTargets = bindTargetsToSources(foundTargets, foundSources);
        logger.debug("{} targets have been bound to sources.", boundTargets.size());
	}
	
    private Set<EventTarget> bindTargetsToSources(Set<EventTarget> eventTargets, Set<EventSource> eventSources) {
    	logger.debug("Trying to bind {} event sources with {} event targets.", eventSources.size(), eventTargets.size());
    	
        Set<EventTarget> boundTargets = new HashSet<EventTarget>();
        Set<EventTarget> unboundTargets = new HashSet<EventTarget>();

        for (EventTarget eventTarget : eventTargets) {
            for (EventSource eventSource : eventSources) {
                eventSource.bindTo(eventTarget);
            }

            if (eventTarget.isBound()) {
                boundTargets.add(eventTarget);
            } else if (strictBindingMode) {
                unboundTargets.add(eventTarget);
            } else {
                logger.warn("Unbound event target: {}", eventTarget);
            }
        }

        if (boundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(eventTargets);
        }
        if (strictBindingMode && !unboundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(unboundTargets);
        }
        
        return boundTargets;
    }
    
    private void verifyState() {
		if (isReleased()) {
			throw new IllegalStateException("The binding has been released!");
		}
    }
}
