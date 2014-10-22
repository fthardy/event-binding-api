package de.javax.util.eventbinding.spi.impl.target;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

	private final Class<?> eventType;
	private final EventSourceIdSelector sourceIdSelector;
	private final EventDispatcher eventDispatcher;
	
	private final Set<EventSource> boundEventSources = new HashSet<EventSource>();
	
	/**
	 * Creates a new instance of this event target implementation. If source
	 * identifier is set to a non-null value the created target instance is
	 * intended to be bound to exactly one event souce which sends events of the
	 * given event type. If the source identifier is <code>null</code> then the
	 * created target instance is intended to be bound to one or more event
	 * sources which send events of the given event type.
	 * 
	 * @param sourceIdSelector
	 *            the selector for the source identifiers.
	 * @param eventType
	 *            the type of the event which this target wants to handle.
	 * @param dispatcher
	 *            the event dispatcher.
	 */
	public DefaultEventTarget(EventSourceIdSelector sourceIdSelector, Class<?> eventType, EventDispatcher dispatcher) {
        if (sourceIdSelector == null) {
            throw new NullPointerException("Undefined event source identifier pattern!");
        }
		this.sourceIdSelector = sourceIdSelector;
		if (eventType == null) {
			throw new NullPointerException("Undefined event type!");
		}
		this.eventType = eventType;
		if (dispatcher == null) {
			throw new NullPointerException("Undefined event dispatcher!");
		}
		this.eventDispatcher = dispatcher;
	}
	
	@Override
	public String toString() {
		return this.eventDispatcher.toString();
	}
	
	@Override
	public Class<?> getEventType() {
	    return this.eventType;
	}
	
	@Override
	public EventSourceIdSelector getEventSourceIdSelector() {
	    return this.sourceIdSelector;
	}
	
	@Override
	public EventDispatcher getEventDispatcher() {
	    return this.eventDispatcher;
	}
	
	@Override
	public void addBoundSource(EventSource source) {
	    this.boundEventSources.add(source);
	}
	
	@Override
	public Set<EventSource> getBoundSources() {
	    return Collections.unmodifiableSet(this.boundEventSources);
	}

	@Override
	public void unbindFromSources() {
		if (this.boundEventSources.isEmpty()) {
			throw new IllegalStateException("Event target is not bound to any event source!");
		}
		for (EventSource boundSource : this.boundEventSources) {
			boundSource.unbindFrom(this);
		}
		this.boundEventSources.clear();
	}
}
