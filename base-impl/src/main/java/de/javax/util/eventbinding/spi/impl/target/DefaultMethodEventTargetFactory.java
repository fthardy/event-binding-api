package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Creates instances of {@link DefaultEventTarget}.
 * 
 * @author Frank Hardy
 */
public class DefaultMethodEventTargetFactory implements MethodEventTargetFactory {

    @Override
    public EventTarget createEventTarget(EventSourceIdSelector sourceIdSelector, Class<?> eventClass, Object metaData, EventDispatcher dispatcher) {
        return new DefaultEventTarget(sourceIdSelector, eventClass, dispatcher);
    }
}
