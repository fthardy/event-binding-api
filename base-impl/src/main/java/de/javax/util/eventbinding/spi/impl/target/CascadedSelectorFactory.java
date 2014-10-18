package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;


/**
 * This is a decorator implementation for an event source identifier factory.<br/>
 * This factory creates instances of {@link CascadedEventSourceIdSelector} with
 * a particular pre selector that is given on instance creation together with
 * the selector factory to decorate. The decorated selector factory is used to
 * create the post selector.
 * 
 * @author Frank Hardy
 */
public class CascadedSelectorFactory implements EventSourceIdSelectorFactory {

    private final EventSourceIdSelector preSelector;
    private final EventSourceIdSelectorFactory selectorFactory;

    /**
     * Creates a new instance of this factory.
     * 
     * @param factory
     *            the decorated factory.
     * @param preSelector
     *            the pre selector.
     */
    public CascadedSelectorFactory(EventSourceIdSelectorFactory factory, EventSourceIdSelector preSelector) {
        if (factory == null) {
            throw new NullPointerException("Undefined event source identifier selector factory!");
        }
        this.selectorFactory = factory;
        if (preSelector == null) {
            throw new NullPointerException("Undefined pre selector!");
        }
        this.preSelector = preSelector;
    }

    @Override
    public EventSourceIdSelector createEventSourceIdSelector(String expression) {
        return new CascadedEventSourceIdSelector(this.preSelector,
                this.selectorFactory.createEventSourceIdSelector(expression));
    }
}