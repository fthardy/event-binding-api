package de.javax.util.eventbinding.spi.impl.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * Create a new instance of this event source.
     * 
     * @param id
     *            the identifier of this event source.
     * @param eventSource
     *            the real event source object represented (or proxied) by this instance.
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
    public String getDescription() {
        return String.format("[DefaultEventSource id=%s", id.toString());
    }

    @Override
    public void bindTo(EventTarget eventTarget) {
        if (eventTarget.canHandle(this)) {
            eventTarget.addBoundSource(this);
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
