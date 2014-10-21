package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.EventBindingException;

public class InternalEventBindingException extends EventBindingException {

    public InternalEventBindingException() {
    }

    public InternalEventBindingException(String message) {
        super(message);
    }

    public InternalEventBindingException(String message, Throwable cause) {
        super(message, cause);
    }

}
