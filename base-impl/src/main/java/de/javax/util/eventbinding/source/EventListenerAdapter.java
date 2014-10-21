package de.javax.util.eventbinding.source;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * A EventListenerAdapter is providing functionality to
 * register and unregister a certain event listener. A EventListenerAdapter
 * is bound to a certain event source always.
 * @author Matthias Hanisch
 *
 */
public interface EventListenerAdapter {

    void registerEventListener(EventDispatcher eventDispatcher);
    void unregisterEventListener();
}
