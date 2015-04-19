package de.javax.util.eventbinding;

/**
 * The base class for all exceptions thrown by an {@link EventBinder}.<br/>
 * Indicates that the binding between a source provider and a target provider has failed for some reason.
 * 
 * @author Frank Hardy
 */
public class EventBindingException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
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

    protected EventBindingException() {
    	super();
    }
}
