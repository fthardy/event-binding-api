package de.javax.util.eventbinding.spi.impl.source;

import java.util.HashMap;
import java.util.Map;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.source.EventListenerAdapterFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Default implementation of an event source.<br/>
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSource implements EventSource {

    private final EventSourceId id;
    private final Object eventSource;
    private final EventListenerAdapterFactory listenerAdapterFactory;
    private final Map<EventTarget, EventListenerAdapter> adapterMapping = 
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
    public DefaultEventSource(
            EventSourceId id, Object eventSource, EventListenerAdapterFactory listenerAdapterFactory) {
        if (id == null) {
            throw new NullPointerException("Undefined identifier!");
        }
        this.id = id;
        if (eventSource == null) {
            throw new NullPointerException("Undefined event source object!");
        }
        this.eventSource = eventSource;
        if (listenerAdapterFactory == null) {
            throw new NullPointerException("Undefinded listener adapter factory!");
        }
        this.listenerAdapterFactory = listenerAdapterFactory;
    }

    @Override
    public EventSourceId getId() {
        return this.id;
    }

    @Override
    public boolean bindTo(EventTarget eventTarget) {
        if (this.adapterMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is already bound to this source!");
        }
        
        boolean bound = false;
        EventListenerAdapter listenerAdapter = this.listenerAdapterFactory.createEventListenerAdapter(
                this.eventSource, eventTarget.getEventType());
        if(listenerAdapter != null) {
            listenerAdapter.registerEventListener(eventTarget.getEventDispatcher());
            this.adapterMapping.put(eventTarget, listenerAdapter);
            eventTarget.addBoundSource(this);
            bound = true;
        }
        return bound;
    }

    @Override
    public void unbindFrom(EventTarget eventTarget) {
        if (!this.adapterMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is not bound to this source!");
        }
        this.adapterMapping.get(eventTarget).unregisterEventListener();
        this.adapterMapping.remove(eventTarget);
    }
}
