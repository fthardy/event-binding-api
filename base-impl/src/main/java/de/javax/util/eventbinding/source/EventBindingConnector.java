package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * An event binding connector represents the bridge for binding an event source
 * to an event target.<br/>
 * Its purpose is to connect an event source and event target by creating an
 * event listener that adapts an {@link EventDispatcher} which is provided by
 * the {@link EventTarget}. Hence, for each event target one event binding
 * connector has to be created.
 * 
 * @author Matthias Hanisch
 * @author Frank Hardy
 */
public interface EventBindingConnector {

    /**
     * Establish the binding between an event source and event target.
     * 
     * @param eventDispatcher
     *            the event dispatcher provided by the {@link EventTarget}.
     */
    void connect(EventDispatcher eventDispatcher);

    /**
     * Disconnect the binding between the event source and event target.
     */
    void disconnect();
}
