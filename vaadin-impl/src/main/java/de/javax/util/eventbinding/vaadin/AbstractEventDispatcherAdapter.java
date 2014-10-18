package de.javax.util.eventbinding.vaadin;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * 
 * @author Frank Hardy
 */
public class AbstractEventDispatcherAdapter {

    protected final EventDispatcher eventDispatcher;

    /**
     * Initialise a new instance of this adapter.
     * 
     * @param dispatcher
     *            the dispatcher to adapt.
     */
    public AbstractEventDispatcherAdapter(EventDispatcher dispatcher) {
        if (dispatcher == null) {
            throw new NullPointerException("Undefined event dispatcher!");
        }
        this.eventDispatcher = dispatcher;
    }

    /**
     * Dispatch the event.
     * 
     * @param event
     *            the event to be dispatched.
     */
    protected void dispatchEvent(Object event) {
        this.eventDispatcher.dispatchEvent(event);
    }
}