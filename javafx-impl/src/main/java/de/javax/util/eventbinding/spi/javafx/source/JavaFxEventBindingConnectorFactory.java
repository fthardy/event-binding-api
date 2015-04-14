package de.javax.util.eventbinding.spi.javafx.source;

import java.lang.reflect.Method;
import java.util.Arrays;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.impl.reflect.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.source.AbstractMethodBasedEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.EventSourceAccessException;

/**
 * The JavaFxEventBindingConnectorFactory creates an EventBindingConnector using the methods
 * {@link Node#addEventHandler(EventType, EventHandler)}, {@link Node#removeEventHandler(EventType, EventHandler)} and
 * {@link EventHandler#handle(Event)}.
 * 
 * @author Matthias Hanisch
 *
 */
@SuppressWarnings("unchecked")
public class JavaFxEventBindingConnectorFactory extends AbstractMethodBasedEventBindingConnectorFactory {

    private Predicate<Method> predicatesAddEventHandler = Predicates.and(new MethodNamePredicate("addEventHandler"),
            new MethodParametersPredicate(EventType.class, EventHandler.class), new PublicMethodPredicate(),
            Predicates.not(new StaticMethodPredicate()));

    private Predicate<Method> predicatesRemoveEventHandler = Predicates.and(new MethodNamePredicate(
            "removeEventHandler"), new MethodParametersPredicate(EventType.class, EventHandler.class),
            new PublicMethodPredicate(), Predicates.not(new StaticMethodPredicate()));

    public JavaFxEventBindingConnectorFactory() {
    }

    @Override
    protected EventBindingConnector createConnector(Object eventSource, MethodBasedEventBindingConnectorFactoryInfo info) {
        return new JavaFxEventBindingConnector(eventSource, info.getRegisterEventBindingMethod(),
                info.getUnregisterEventBindingMethod(), info.getEventHandlerMethod(), info.getEventHandlerType(),
                info.getEventType());
    }

    @Override
    protected MethodBasedEventBindingConnectorFactoryInfo getInfo(Class<?> eventSourceType, Class<?> eventType) {
        // find method for registering event listeners of the given type
        Method registerEventBindingMethod = findRegisterUnregisterEventHandlerMethod(eventSourceType, eventType,
                predicatesAddEventHandler);
        // find method for unregistering event listeners of the given type
        Method unregisterEventBindingMethod = findRegisterUnregisterEventHandlerMethod(eventSourceType, eventType,
                predicatesRemoveEventHandler);
        // there should be one addxxListener and one removexxxListener method
        if (registerEventBindingMethod != null && unregisterEventBindingMethod != null) {
            // the event listener types of both methods should match
            try {
                Method eventHandlerMethod = EventHandler.class.getMethod("handle", Event.class);
                return new MethodBasedEventBindingConnectorFactoryInfo(registerEventBindingMethod,
                        unregisterEventBindingMethod, EventHandler.class, eventHandlerMethod, eventType);
            } catch (Exception e) {
                throw new EventSourceAccessException("Could not find JavaFX method EventHandler.handle(Event)");
            }
        }
        return null;
    }

    /**
     * Returns a Method of the given eventSourceType with a method name matching the given regular expression and having
     * a single method parameter of the given eventType.
     * 
     * @param eventSourceType
     *            The type of the event source object.
     * @param eventType
     *            The type of the event which should be method parameter type also.
     * @param predicates
     *            The predicates defining the method to find.
     * @return A Method object if there is exactly one method matching all criteria otherwise <code>null</code> is
     *         returned.
     */
    private Method findRegisterUnregisterEventHandlerMethod(Class<?> eventSourceType, Class<?> eventType,
            Predicate<Method> predicates) {
        Iterable<Method> methods = Iterables.filter(Arrays.asList(eventSourceType.getMethods()), predicates);
        if (Iterables.size(methods) == 1) {
            return methods.iterator().next();
        } else {
            return null;
        }
    }
}
