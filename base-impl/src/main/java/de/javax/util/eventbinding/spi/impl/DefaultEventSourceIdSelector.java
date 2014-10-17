package de.javax.util.eventbinding.spi.impl;

import java.util.Arrays;
import java.util.List;

/**
 * The default implementation of the event source identifier selector.<br/>
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceIdSelector implements EventSourceIdSelector {

    private static List<String> splitExpressionIntoSegments(String expression) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Undefined event source identifier pattern expression!");
        }
        return Arrays.asList(expression.split("."));
    }

    private final List<String> names;

    /**
     * Create a new event source identifier reference.
     * 
     * @param expression
     *            the expression of the source identifier.
     */
    public DefaultEventSourceIdSelector(String expression) {
        this(splitExpressionIntoSegments(expression));
    }

    private DefaultEventSourceIdSelector(List<String> names) {
        this.names = names;
    }

    @Override
    public boolean matches(EventSourceId sourceId) {
        if (this.refersToEventSourceProvider()) {
            return this.names.equals(sourceId.getNames().subList(0, sourceId.getNames().size() - 1));
        } else {
            return this.names.equals(sourceId.getNames());
        }
    }
    
    private boolean refersToEventSourceProvider() {
        return "*".equals(this.names.get(this.names.size()- 1));
    }
}
