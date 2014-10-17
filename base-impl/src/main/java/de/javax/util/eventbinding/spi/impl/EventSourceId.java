package de.javax.util.eventbinding.spi.impl;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an identifier for an event source object.<br/>
 * An event source identifier consist of one or more names which qualify the
 * event source within a hierachical structure.
 * 
 * @author Frank Hardy
 */
public class EventSourceId {
    
    private final String[] names;

    /**
     * Creates a new event source identifier.
     * 
     * @param name
     *            the identifier name which becomes the first segment of this
     *            event source identifier.
     */
    public EventSourceId(String name) {
        this.validateName(name);
        this.names = new String[] { name };
    }
    
    /**
     * Only for internal use.
     * 
     * @param names
     *            the names for the new instance.
     */
    private EventSourceId(String[] names) {
        assert names != null && names.length > 0; 
        this.names = names;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.names);
    }

    @Override
    public boolean equals(Object other) {
        return Arrays.equals(this.names, ((EventSourceId) other).names);
    }
    
    @Override
    public String toString() {
        return Arrays.toString(this.names);
    }
    
    /**
     * @return an array of the names.
     */
    public List<String> getNames() {
        return Arrays.asList(this.names);
    }
    
    /**
     * @return an array of the names without the last name.
     */
    public List<String> getParentNames() {
        return Arrays.asList(Arrays.copyOf(this.names, this.names.length - 1));
    }

    /**
     * Extend this identifier by a new name segment.
     * 
     * @param name
     *            the new identifier name segment to extend this identifier with.
     * 
     * @return a new identifier with the given segment.
     */
    public EventSourceId extend(String name) {
        this.validateName(name);
        String[] newNames = Arrays.copyOf(this.names, this.names.length + 1);
        newNames[this.names.length] = name;
        return new EventSourceId(newNames);
    }
    
    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Undefined identifier name segment!");
        }
        boolean valid = Character.isJavaIdentifierStart(name.charAt(0));
        if (valid && name.length() > 1) {
            for (int i = 1; i < name.length() && valid; i++) {
                valid = Character.isJavaIdentifierPart(name.charAt(i));
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Invalid event source identifier name segment: " + name);
        }
    }
 }
