package de.javax.util.eventbinding.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an identifier for an event source object.<br/>
 * An event source identifier consist of one or more names. Each name has to
 * conform to the naming rules of Java identifiers.
 * 
 * @author Frank Hardy
 */
public class EventSourceId {
    
    private final List<String> names;

    /**
     * Creates a new event source identifier.
     * 
     * @param name
     *            the name for the new identifier.
     */
    public EventSourceId(String name) {
        this(Arrays.asList(new String[] { name }));
    }
    
    /**
     * Creates a new event source identifier.
     * 
     * @param names
     *            the names for the new instance.
     */
    public EventSourceId(List<String> names) {
        this.validateNames(names);
        this.names = Collections.unmodifiableList(new ArrayList<String>(names));
    }
    
    @Override
    public int hashCode() {
        return this.names.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.names.equals(other);
    }
    
    @Override
    public String toString() {
        return this.names.toString();
    }
    
    /**
     * @return the list of names.
     */
    public List<String> getNames() {
        return this.names;
    }
    
    private void validateNames(List<String> names) {
        if (names == null || names.size() == 0) {
            throw new IllegalArgumentException("Undefined identifier names!");
        }
        for (String name : names) {
            this.validateName(name);
        }
    }
    
    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Undefined identifier name!");
        }
        boolean valid = Character.isJavaIdentifierStart(name.charAt(0));
        if (valid && name.length() > 1) {
            for (int i = 1; i < name.length() && valid; i++) {
                valid = Character.isJavaIdentifierPart(name.charAt(i));
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Invalid event source identifier name: " + name);
        }
    }
 }
