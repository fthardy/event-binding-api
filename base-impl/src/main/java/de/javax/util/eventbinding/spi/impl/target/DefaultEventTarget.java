package de.javax.util.eventbinding.spi.impl.target;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * The default implementation for the event target.
 *
 * @author Frank Hardy
 */
public class DefaultEventTarget implements EventTarget {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Class<?> eventClass;
    private final EventSourceIdSelector sourceIdSelector;
    private final EventDispatcher eventDispatcher;

    private final Set<EventSource> boundEventSources = new HashSet<EventSource>();

    public DefaultEventTarget(EventSourceIdSelector sourceIdSelector, Class<?> eventClass, EventDispatcher dispatcher) {
        if (sourceIdSelector == null) {
            throw new NullPointerException("Undefined event source identifier pattern!");
        }
        this.sourceIdSelector = sourceIdSelector;

        if (eventClass == null) {
            throw new NullPointerException("Undefined event class!");
        }
        this.eventClass = eventClass;

        if (dispatcher == null) {
            throw new NullPointerException("Undefined event dispatcher!");
        }
        this.eventDispatcher = dispatcher;
    }

    @Override
    public String getDescription() {
        return this.eventDispatcher.toString();
    }

    @Override
    public Class<?> getEventClass() {
        return this.eventClass;
    }

    public EventSourceIdSelector getEventSourceIdSelector() {
        return this.sourceIdSelector;
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        return this.eventDispatcher;
    }

    @Override
    public void addBoundSource(EventSource eventSource) {
        this.boundEventSources.add(eventSource);
    }

    @Override
    public Set<EventSource> getBoundSources() {
        return Collections.unmodifiableSet(this.boundEventSources);
    }

    @Override
    public boolean isBound() {
        return !this.boundEventSources.isEmpty();
    }

    @Override
    public void unbindFromSources() {
        if (this.boundEventSources.isEmpty()) {
            throw new IllegalStateException("Event target is not bound to any event source!");
        }
        for (EventSource boundSource : this.boundEventSources) {
            boundSource.unbindFrom(this);
        }
    }

    @Override
    public void removeBoundSource(EventSource eventSource) {
        this.boundEventSources.remove(eventSource);
    }

    @Override
    public boolean accepts(EventSource eventSource) {
        if (!getEventSourceIdSelector().matches(eventSource.getId())) {
            return false;
        }
        if (boundEventSources.contains(eventSource)) {
            throw new IllegalStateException("The event target is already bound to this source!");
        }
        return true;
    }
}
