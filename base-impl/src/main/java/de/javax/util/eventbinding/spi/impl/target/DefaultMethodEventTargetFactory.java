package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Creates instances of {@link DefaultEventTarget}.
 * 
 * @author Frank Hardy
 */
public class DefaultMethodEventTargetFactory implements MethodEventTargetFactory {

    @Override
    public EventTarget createEventTarget(
            Object targetProvider, Method eventHandlerMethod, EventSourceIdSelector sourceIdSelector) {
        return new DefaultEventTarget(
                sourceIdSelector,
                eventHandlerMethod.getParameterTypes()[0],
                new MethodAdaptingEventDispatcher(eventHandlerMethod, targetProvider));
    }
}
