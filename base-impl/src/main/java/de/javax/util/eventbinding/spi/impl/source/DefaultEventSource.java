package de.javax.util.eventbinding.spi.impl.source;

import java.util.HashMap;
import java.util.Map;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.source.EventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Default implementation of an event source.<br/>
 * 
 * @author Matthias Hanisch
 * @author Frank Hardy
 */
public class DefaultEventSource implements EventSource {

    private final EventSourceId id;
    private final Object eventSource;
    private final EventBindingConnectorFactory connectorFactory;
    private final Map<EventTarget, EventBindingConnector> connectorMapping =
            new HashMap<EventTarget, EventBindingConnector>();

    /**
     * Create a new instance of this event source.
     * 
     * @param id
     *            the identifier of this event source.
     * @param eventSource
     *            the real event source object represented (or proxied) by this
     *            instance.
     * @param connectorFactory
     *            the connector factory.
     */
    public DefaultEventSource(EventSourceId id, Object eventSource, EventBindingConnectorFactory connectorFactory) {
        if (id == null) {
            throw new NullPointerException("Undefined identifier!");
        }
        this.id = id;
        if (eventSource == null) {
            throw new NullPointerException("Undefined event source object!");
        }
        this.eventSource = eventSource;
        if (connectorFactory == null) {
            throw new NullPointerException("Undefinded connector factory!");
        }
        this.connectorFactory = connectorFactory;
    }

    @Override
    public EventSourceId getId() {
        return this.id;
    }

    @Override
    public void bindTo(EventTarget eventTarget) {
        if (this.connectorMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is already bound to this source!");
        }

        EventBindingConnector connector = this.connectorFactory.createConnector(
                this.eventSource, eventTarget.getEventType());
        if (connector != null) {
            connector.connect(eventTarget.getEventDispatcher());
            this.connectorMapping.put(eventTarget, connector);
            eventTarget.addBoundSource(this);
        }
    }

    @Override
    public void unbindFrom(EventTarget eventTarget) {
        if (!this.connectorMapping.containsKey(eventTarget)) {
            throw new IllegalStateException("The event target is not bound to this source!");
        }
        this.connectorMapping.get(eventTarget).disconnect();
        this.connectorMapping.remove(eventTarget);
    }
}
