package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptor;

/**
 * This factory creates event target instances.
 *
 * @author Frank Hardy
 */
public interface MethodEventTargetFactory {

    /**
     * Create a new instance of an event target.
     * 
     * @param handlerMethodOwner
     *            the event target provider instance.
     * @param idSelectorPrefix
     *            the ID-selector prefix expression.
     * @param handlerMethodDescriptor
     *            the handler method descriptor instance.
     * 
     * @return a new event target instance.
     */
    EventTarget createMethodEventTarget(Object handlerMethodOwner, String idSelectorPrefix,
            HandlerMethodDescriptor handlerMethodDescriptor);
}