package de.javax.util.eventbinding.spi.impl.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final EventSourceId id;
    private final Object eventSource;
    private final EventBindingConnectorFactory connectorFactory;

    /**
     * Create a new instance of this event source.
     * 
     * @param id
     *            the identifier of this event source.
     * @param eventSource
     *            the real event source object represented (or proxied) by this instance.
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
            throw new NullPointerException("Undefined connectorFactory object!");
        }
        this.connectorFactory = connectorFactory;
    }

    @Override
    public EventSourceId getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return String.format("[DefaultEventSource id=%s", id.toString());
    }

    @Override
    public void bindTo(EventTarget eventTarget) {
        if (eventTarget.accepts(this)) {
            EventBindingConnector connector = this.connectorFactory.createConnector(getSource(),
                    eventTarget.getEventClass());
            if (connector != null) {
                connector.connect(eventTarget.getEventDispatcher());
                eventTarget.addBoundSource(this);
                logger.debug("connect source={} with target {}", getId().toString(), eventTarget);
            }
        }
    }

    @Override
    public void unbindFrom(EventTarget eventTarget) {
        eventTarget.removeBoundSource(this);
    }

    @Override
    public Object getSource() {
        return this.eventSource;
    }
}
