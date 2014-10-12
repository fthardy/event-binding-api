package de.javax.util.eventbinding.spi.impl;

/**
 * The definition of common constants.
 * 
 * @author Frank Hardy
 */
public class EventBindingSpiUtils {

    /**
     * The definition of the separator for source identifiers.
     */
    public static final String SOURCE_ID_SEPARATOR = ".";
    
    /**
     * Extend a source id with a new name segment.<br/>
     * Left and right part will be separated by {@value #SOURCE_ID_SEPARATOR}.
     * 
     * @param left
     *            the left part of the identifier.
     * @param right
     *            the right part of the identifier.
     *            
     * @return the new extended identifier.
     */
    public static String extendSourceId(String left, String right) {
        String extendedSourceId = null;
        if (left == null || left.isEmpty()) {
            extendedSourceId = right;
        } else if (right == null || right.isEmpty()) {
            extendedSourceId = left;
        } else {
            extendedSourceId = 
                    new StringBuilder(left).append(SOURCE_ID_SEPARATOR).append(right).toString();
        }
        return extendedSourceId;
    }
    
    /** No instance creation allowed. */
    private EventBindingSpiUtils() {
        // intentionally emtpy
    }
}
