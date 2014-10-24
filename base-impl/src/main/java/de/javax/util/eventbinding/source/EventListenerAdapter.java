package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * An event listener adapter represents the bridge for binding an event source
 * to an event target.<br/>
 * Its purpose is to adapt the {@link EventDispatcher} which is provided by an
 * {@link EventTarget} to a particular event listener. The listener adapter is
 * responsible to register and unregister the event listener at an event source.
 * For each event target one listener adapter exists.
 * 
 * @author Matthias Hanisch
 * @author Frank Hardy
 */
public interface EventListenerAdapter { // TODO this is not really a GoF-Adapter, maybe rename it in connector or s.e.

    /**
     * Register the receiving listener adapter at its event source.
     * 
     * @param eventDispatcher
     *            the event dispatcher which is used to dispatch an event to the
     *            {@link EventTarget}.
     */
    void registerEventListener(EventDispatcher eventDispatcher);

    /**
     * Unregister the receiving listener adapter from its event source.
     */
    void unregisterEventListener();
}
