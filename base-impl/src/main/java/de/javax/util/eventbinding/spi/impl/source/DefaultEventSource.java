package de.javax.util.eventbinding.spi.impl.source;

import java.util.HashMap;
import java.util.Map;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Default implementation of EventSource delegating the register and unregister
 * calls to the EventListenerAdapter which knows how to handle the
 * register/unregister process for a certain event source.
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSource implements EventSource {

    private final EventSourceId id;
    private final Object eventSource;
    private final Map<EventTarget, EventListenerAdapter> targetToListenerAdapterMapping = 
            new HashMap<EventTarget, EventListenerAdapter>();

    /**
     * Create a new instance of this event source.
     * 
     * @param id
     *            the identifier of this event source.
     * @param eventSource
     *            the real event source object represented (or proxied) by this
     *            instance.
     */
    public DefaultEventSource(EventSourceId id, Object eventSource) {
        if (id == null) {
            throw new NullPointerException("Undefined identifier!");
        }
        this.id = id;
        if (eventSource == null) {
            throw new NullPointerException("Undefined event source object!");
        }
        this.eventSource = eventSource;
    }

    @Override
    public EventSourceId getId() {
        return this.id;
    }

    @Override
    public boolean bindTo(EventTarget eventTarget) {
        if (this.targetToListenerAdapterMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is already bound to this source!");
        }
        
        boolean bound = false;
        EventListenerAdapter listenerAdapter = 
                EventListenerProviderFactory.createAdapter(this.eventSource, eventTarget.getEventType());
        if(listenerAdapter != null) {
            listenerAdapter.registerEventListener(eventTarget.getEventDispatcher());
            this.targetToListenerAdapterMapping.put(eventTarget, listenerAdapter);
            eventTarget.addBoundSource(this);
            bound = true;
        }
        return bound;
    }

    @Override
    public void unbindFrom(EventTarget eventTarget) {
        if (!this.targetToListenerAdapterMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is not bound to this source!");
        }
        this.targetToListenerAdapterMapping.get(eventTarget).unregisterEventListener();
        this.targetToListenerAdapterMapping.remove(eventTarget);
    }
}
