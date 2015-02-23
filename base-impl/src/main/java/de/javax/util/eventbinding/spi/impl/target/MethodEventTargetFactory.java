package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Creates new event targets.
 * 
 * @author Frank Hardy
 */
public interface MethodEventTargetFactory {

    /**
     * Creates a new event target from an event handler method.
     * 
     * @param sourceIdSelector
     *            the source identifier selector.
     * @param targetClass
     *            the supported event class.
     * @param dispatcher
     *            the event dispatcher.
     * 
     * @return the new event target instance.
     */
    EventTarget createEventTarget(EventSourceIdSelector sourceIdSelector, Class<?> eventClass, Object metaData, EventDispatcher dispatcher);
}
