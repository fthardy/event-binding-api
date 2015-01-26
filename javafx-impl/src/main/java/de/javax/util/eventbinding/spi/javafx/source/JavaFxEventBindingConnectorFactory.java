package de.javax.util.eventbinding.spi.javafx.source;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.NotPredicate;
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
public class JavaFxEventBindingConnectorFactory extends AbstractMethodBasedEventBindingConnectorFactory {

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
                "addEventHandler");
        // find method for unregistering event listeners of the given type
        Method unregisterEventBindingMethod = findRegisterUnregisterEventHandlerMethod(eventSourceType, eventType,
                "removeEventHandler");
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
     * @param methodNameRegEx
     *            The name of the method to find. (e.g. &quot;add.+Listener&quot; can be used to find regular methods
     *            adding listener instances)
     * @return A Method object if there is exactly one method matching all criteria otherwise <code>null</code> is
     *         returned.
     */
    private Method findRegisterUnregisterEventHandlerMethod(Class<?> eventSourceType, Class<?> eventType,
            String methodNameRegEx) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(eventSourceType.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(2)).filter(new MethodNamePredicate(methodNameRegEx))
                .filter(new MethodParametersPredicate(EventType.class, EventHandler.class));
        if (filter.getElements().size() == 1) {
            return filter.getElements().iterator().next();
        }
        return null;
    }
}
