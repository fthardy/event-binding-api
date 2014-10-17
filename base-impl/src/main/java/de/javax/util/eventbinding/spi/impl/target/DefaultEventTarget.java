package de.javax.util.eventbinding.spi.impl.target;

import java.util.Set;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * An event target implementation.
 *
 * @author Frank Hardy
 */
public class DefaultEventTarget implements EventTarget {

	private final Class<?> eventType;
	private final EventSourceIdSelector sourceIdSelector;
	private final EventDispatcher eventDispatcher;
	
	private Set<EventSource> boundEventSources;
	
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
	
	/**
	 * Creates a new instance of this event target implementation. The created
	 * target instance is intended to be bound to one or more event sources
	 * which send events of the given event type.
	 * 
	 * @param eventType
	 *            the type of the event which this target wants to handle.
	 * @param dispatcher
	 *            the event dispatcher.
	 */
	public DefaultEventTarget(Class<?> eventType, EventDispatcher dispatcher) {
		this(null, eventType, dispatcher);
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
	public void setBoundSources(Set<EventSource> boundSources) {
	    if (this.boundEventSources != null) {
	        throw new IllegalStateException("Event target is already bound to event sources!");
	    }
	    this.boundEventSources = boundSources;
	}

	@Override
	public void release() {
		if (this.boundEventSources == null) {
			throw new IllegalStateException("Target is not bound to any source!");
		}
		for (EventSource boundSource : this.boundEventSources) {
			boundSource.unbindFrom(this);
		}
		this.boundEventSources = null;
	}
}
