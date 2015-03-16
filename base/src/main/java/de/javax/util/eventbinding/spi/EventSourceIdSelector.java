package de.javax.util.eventbinding.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The interface definition for an event source identifier selector.
 * 
 * @author Frank Hardy
 */
public class EventSourceIdSelector {

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
    
    static List<String> splitExpression(String expression) {
        assert expression != null;

        boolean valid = true;
        List<String> parts = Arrays.asList(expression.split("\\" + SEPARATOR));
        for (int i = 0; i < (parts.size() - 1) && valid; i++) {
            valid = isValidPart(parts.get(i));
        }

        if (valid) {
            String lastPart = parts.get(parts.size() - 1);
            valid = lastPart.equals(WILDCARD) || isValidPart(lastPart);
        }

        if (!valid && !EventSourceId.ROOT.equals(expression)) {
            throw new IllegalArgumentException("Invalid event source identifier selector expression: " + expression);
        }

        return parts;
    }

    static boolean isValidPart(String part) {
        if (part.isEmpty()) {
            return false;
        }
        boolean valid = Character.isJavaIdentifierStart(part.charAt(0));
        if (valid && part.length() > 1) {
            for (int i = 1; i < part.length() && valid; i++) {
                valid = Character.isJavaIdentifierPart(part.charAt(i));
            }
        }
        return valid;
    }

    private final List<String> parts;

    /**
     * Create a new instance of this event source identifier selector.
     * 
     * @param expression
     *            the selector expression.
     */
    public EventSourceIdSelector(String expression) {
        this.parts = Collections.unmodifiableList(splitExpression(expression));
    }

    /**
     * Check if a given event source identifier is matching this pattern.
     * 
     * @param sourceId
     *            the event source identifier to be checked.
     * 
     * @return <code>true</code> if the given identifier matches the pattern.
     */
    public boolean matches(EventSourceId sourceId) {
        boolean match = false;
        if (this.parts.get(this.parts.size() - 1).equals(WILDCARD)) {
            if (this.parts.size() == 1) {
                match = true;
            } else {
                match = this.parts.subList(0, this.parts.size() - 1).equals(
                        sourceId.getNames().subList(0, sourceId.getNames().size() - 1));
            }
        } else {
            match = this.parts.equals(sourceId.getNames());
        }
        return match;
    }

    @Override
    public String toString() {
        return this.parts.toString();
    }
}