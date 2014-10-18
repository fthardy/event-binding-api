package de.javax.util.eventbinding.spi.impl;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;

/**
 * Creates instances of {@link DefaultEventSourceIdSelector}.
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceIdSelectorFactory implements EventSourceIdSelectorFactory {

    @Override
    public EventSourceIdSelector createEventSourceIdSelector(String expression) {
        return new DefaultEventSourceIdSelector(expression);
    }
}
