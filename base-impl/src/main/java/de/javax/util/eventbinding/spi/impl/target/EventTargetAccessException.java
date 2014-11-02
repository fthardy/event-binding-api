package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.EventBindingException;

/**
 * Will be thrown by
 * {@link DefaultEventTargetCollector#collectEventTargetsFrom(Object)} when
 * the access to the target provider object is not allowed.
 * 
 * @author Frank Hardy
 */
public class EventTargetAccessException extends EventBindingException {

    private static final long serialVersionUID = 3137888076154365395L;
    
    public EventTargetAccessException(String message) {
        super(message);
    }

    public EventTargetAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}