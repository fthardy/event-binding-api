package de.javax.util.eventbinding.spi.impl.source;

import de.javax.util.eventbinding.EventBindingException;

public class EventSourceAccessException extends EventBindingException {

    private static final long serialVersionUID = -4667663270779236887L;

    public EventSourceAccessException() {
    }

    public EventSourceAccessException(String message) {
        super(message);
    }

    public EventSourceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
