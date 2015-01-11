package de.javax.util.eventbinding.spi.javafx.source;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import de.javax.util.eventbinding.spi.impl.source.AbstractMethodBasedEventBindingConnector;
import de.javax.util.eventbinding.spi.impl.source.EventSourceAccessException;

/**
 * The JavaFxEventBindingConnector uses the methods {@link Node#addEventHandler(EventType, javafx.event.EventHandler)}
 * and {@link Node#removeEventHandler(EventType, javafx.event.EventHandler)} to register/unregister event handlers.
 * 
 * @author Matthias Hanisch
 *
 */
public class JavaFxEventBindingConnector extends AbstractMethodBasedEventBindingConnector {

    public JavaFxEventBindingConnector(Object eventSource, Method registerEventBindingMethod,
            Method unregisterEventBindingMethod, Method eventMethod, Class<?> eventHandlerType, Class<?> eventType) {
        super(eventSource, registerEventBindingMethod, unregisterEventBindingMethod, eventMethod, eventHandlerType,
                eventType);
    }

    @Override
    protected void disconnect(Object eventSource, Method unregisterEventBindingMethod, Class<?> eventHandlerType,
            Object eventHandler, Class<?> eventType) {
        try {
            unregisterEventBindingMethod.invoke(eventSource, eventType, eventHandler);
        } catch (Exception e) {
            throw new EventSourceAccessException("unregistering event listener failed", e);
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void connect(Object eventSource, Method registerEventBindingMethod, Class<?> eventHandlerType,
            Object eventHandler, Class<?> eventType) {
        try {
            EventType genericEventType = retrieveParameterizedEventType(eventType);
            if (genericEventType == null) {
                throw new EventSourceAccessException("event type not registered " + eventType);
            }
            registerEventBindingMethod.invoke(eventSource, genericEventType, eventHandler);
        } catch (Exception e) {
            throw new EventSourceAccessException("registering event listener failed", e);
        }
    }

    /**
     * Retrieves the parameterized instance of {@link EventType} for the given type of an {@link Event}. Using the
     * convention that there is a field ANY holding this instance for each sub class of {@link Event}.
     * 
     * @param eventType
     *            The type of the {@link Event}.
     * @return The instance of {@link EventType} for receiving all instances of the given event.
     */
    @SuppressWarnings("rawtypes")
    private EventType retrieveParameterizedEventType(Class<?> eventType) {
        try {
            Field field = eventType.getField("ANY");
            return (EventType) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
