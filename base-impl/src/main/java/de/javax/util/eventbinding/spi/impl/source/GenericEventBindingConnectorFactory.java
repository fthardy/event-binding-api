package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterTypeHasEventMethodForTypePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.NotPredicate;

/**
 * This factory implementation creates instances {@link GenericEventBindingConnector}.
 * 
 * @author Matthias Hanisch
 */
public class GenericEventBindingConnectorFactory extends AbstractMethodBasedEventBindingConnectorFactory {

    public GenericEventBindingConnectorFactory() {
    }

    @Override
    protected EventBindingConnector createConnector(Object eventSource, MethodBasedEventBindingConnectorFactoryInfo info) {
        return new GenericEventBindingConnector(eventSource, info.getRegisterEventBindingMethod(),
                info.getUnregisterEventBindingMethod(), info.getEventHandlerMethod(), info.getEventHandlerType(),
                info.getEventType());
    }

    @Override
    protected MethodBasedEventBindingConnectorFactoryInfo getInfo(Class<?> eventSourceType, Class<?> eventType) {
        // find method for registering event listeners of the given type
        Method registerEventBindingMethod = findListenerMethod(eventSourceType, eventType, "add.+Listener");
        // find method for unregistering event listeners of the given type
        Method unregisterEventBindingMethod = findListenerMethod(eventSourceType, eventType, "remove.+Listener");
        // there should be one addxxListener and one removexxxListener method
        if (registerEventBindingMethod != null && unregisterEventBindingMethod != null) {
            Class<?> registerEventBindingMethodParameterType = registerEventBindingMethod.getParameterTypes()[0];
            Class<?> unregisterEventBindingMethodParameterType = unregisterEventBindingMethod.getParameterTypes()[0];
            // the event listener types of both methods should match
            if (registerEventBindingMethodParameterType.equals(unregisterEventBindingMethodParameterType)) {
                Method eventHandlerMethod = findEventMethod(registerEventBindingMethodParameterType, eventType);
                return new MethodBasedEventBindingConnectorFactoryInfo(registerEventBindingMethod,
                        unregisterEventBindingMethod, registerEventBindingMethodParameterType, eventHandlerMethod,
                        eventType);
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
    private Method findListenerMethod(Class<?> eventSourceType, Class<?> eventType, String methodNameRegEx) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(eventSourceType.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(1)).filter(new MethodNamePredicate(methodNameRegEx))
                .filter(new MethodParameterTypeHasEventMethodForTypePredicate(eventType));
        if (filter.getElements().size() == 1) {
            return filter.getElements().iterator().next();
        }
        return null;
    }

    /**
     * Returns a Method of the given listenerType with single method parameter of the given eventType.
     * 
     * @param listenerType
     *            The type of the event listener.
     * @param eventType
     *            The type of the event which should be method parameter type also.
     * @return A Method object if there is exactly one method matching all criteria otherwise <code>null</code> is
     *         returned.
     */
    private Method findEventMethod(Class<?> listenerType, Class<?> eventType) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(listenerType.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(1)).filter(new MethodParametersPredicate(eventType));
        if (filter.getElements().size() == 1) {
            return filter.getElements().iterator().next();
        }
        return null;
    }
}
