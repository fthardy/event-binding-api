package de.javax.util.eventbinding.spi.impl.target;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * An event target implementation.
 *
 * @author Frank Hardy
 */
public class DefaultEventTarget implements EventTarget {

	private final Class<?> eventType;
	private final String sourceId;
	
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
	 * @param sourceId
	 *            the identifier of the source to bind this target to. May be
	 *            <code>null</code>.
	 * @param eventType
	 *            the type of the event which this target wants to handle.
	 * @param dispatcher
	 *            the event dispatcher.
	 */
	public DefaultEventTarget(String sourceId, Class<?> eventType, EventDispatcher dispatcher) {
		this.sourceId = sourceId;
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
	public boolean bindToSourcesOf(EventSourceProvider sourceProvider) {
		if (this.boundEventSources != null) {
			throw new IllegalStateException("Event target is already bound to event sources!");
		}
		
		Set<EventSource> eventSources = this.determineSources(sourceProvider);
		if (!eventSources.isEmpty()) {
			for (EventSource source : eventSources) {
				source.register(this.eventDispatcher);
			}
			this.boundEventSources = eventSources;
		}
		
		return this.boundEventSources != null ? !this.boundEventSources.isEmpty() : false;
	}

	@Override
	public void release() {
		if (this.boundEventSources == null) {
			throw new IllegalStateException("Target is not bound to any source!");
		}
		for (EventSource boundSource : this.boundEventSources) {
			boundSource.unregisterEventDispatcher();
		}
		this.boundEventSources = null;
	}
	
	/**
	 * Determine the event sources relevant for this event target from the give
	 * event source provider.
	 * 
	 * @param sourceProvider
	 *            the source provider instance.
	 * 
	 * @return the set of event sources or an empty set. Never <code>null</code>
	 *         .
	 */
	protected Set<EventSource> determineSources(EventSourceProvider sourceProvider) {
		@SuppressWarnings("unchecked")
		Set<EventSource> eventSources = Collections.EMPTY_SET;
		if (this.sourceId == null) {
			eventSources = sourceProvider.findEventSourcesByType(this.eventType);
		} else {
			EventSource source = sourceProvider.findEventSource(this.sourceId, this.eventType);
			if (source != null) {
				eventSources = new HashSet<EventSource>();
				eventSources.add(source);
			}
		}
		return eventSources;
	}
}
