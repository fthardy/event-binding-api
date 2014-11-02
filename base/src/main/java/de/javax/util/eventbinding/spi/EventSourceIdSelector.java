package de.javax.util.eventbinding.spi;

/**
 * The interface definition for an event source identifier selector.
 * 
 * @author Frank Hardy
 */
public interface EventSourceIdSelector {

    /**
     * The definition for the separator sequence which is used in selector
     * expressions to separate the identifier names.
     */
    public static final String SEPARATOR = ".";

    /**
     * The definition for the wildcard sequence which is used in selector expressions.<br/>
     * It depends on the implementation of the selector where wildcards are allowed to be
     * used.
     */
    public static final String WILDCARD = "*";

    /**
     * Check if a given event source identifier is matching this pattern.
     * 
     * @param sourceId
     *            the event source identifier to be checked.
     * 
     * @return <code>true</code> if the given identifier matches the pattern.
     */
    boolean matches(EventSourceId sourceId);
}