package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

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
     * @param targetProvider
     *            the target provider object to which the method belongs to.
     * @param eventHandlerMethod
     *            the event handler method.
     * @param sourceIdSelector
     *            the source identifier selector.
     * 
     * @return the new event target instance.
     */
    EventTarget createEventTarget(
            Object targetProvider, Method eventHandlerMethod, EventSourceIdSelector sourceIdSelector);
}
