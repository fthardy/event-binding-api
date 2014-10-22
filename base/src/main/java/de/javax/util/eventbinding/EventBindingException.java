package de.javax.util.eventbinding;

/**
 * The base class of all exceptions thrown by the {@link DefaultEventBinder}.
 * 
 * @author Frank Hardy
 */
public abstract class EventBindingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EventBindingException() {
    }
    
    /**
     * Initialise this exception with a message.
     * 
     * @param message
     *            the message.
     */
    public EventBindingException(String message) {
        super(message);
    }

    /**
     * Initialises this exception with a message and a cause.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the cause.
     */
    public EventBindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
