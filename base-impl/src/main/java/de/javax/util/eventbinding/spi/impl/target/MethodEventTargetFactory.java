package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

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
     * @param sourceId
     *            the identifier of the event source or null or an empty string.
     * 
     * @return the new event target instance.
     */
    EventTarget createEventTarget(Object targetProvider, Method eventHandlerMethod, String sourceId);
}
