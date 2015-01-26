package de.javax.util.eventbinding.spi.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;

/**
 * The default implementation of the event source identifier selector.<br/>
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceIdSelector implements EventSourceIdSelector {

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
    public DefaultEventSourceIdSelector(String expression) {
        this.parts = Collections.unmodifiableList(splitExpression(expression));
    }

    @Override
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
}
