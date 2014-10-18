package de.javax.util.eventbinding.spi;

/**
 * Creates new instances of {@link EventSourceIdSelector}s.
 * 
 * @author Frank Hardy
 */
public interface EventSourceIdSelectorFactory {

    /**
     * Create a new selector.
     * 
     * @param expression
     *            the selector expression.
     * 
     * @return a new selector instance.
     */
    EventSourceIdSelector createEventSourceIdSelector(String expression);
}
