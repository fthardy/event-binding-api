package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * A generic implementation of a {@link EventBindingConnector}.<br/>
 * It uses a method to register an event listener and a method to unregister an event listener created as proxy and
 * calling {@link EventDispatcher#dispatchEvent(Object)} when receiving an event.
 * 
 * @author Matthias Hanisch
 */
public class GenericEventBindingConnector extends AbstractMethodBasedEventBindingConnector {

    public GenericEventBindingConnector(Object eventSource, Method registerEventBindingMethod,
            Method unregisterEventBindingMethod, Method eventMethod, Class<?> eventHandlerType, Class<?> eventType) {
        super(eventSource, registerEventBindingMethod, unregisterEventBindingMethod, eventMethod, eventHandlerType,
                eventType);
    }

    @Override
    protected void connect(Object eventSource, Method registerEventBindingMethod, Class<?> eventHandlerType,
            Object eventHandler, Class<?> eventType) {
        try {
            registerEventBindingMethod.invoke(eventSource, eventHandler);
        } catch (Exception e) {
            throw new EventSourceAccessException("registering event listener failed", e);
        }
    }

    @Override
    protected void disconnect(Object eventSource, Method unregisterEventBindingMethod, Class<?> eventHandlerType,
            Object eventHandler, Class<?> eventType) {
        try {
            unregisterEventBindingMethod.invoke(eventSource, eventHandler);
        } catch (Exception e) {
            throw new EventSourceAccessException("unregistering event listener failed", e);
        }

    }

}