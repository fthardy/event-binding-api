package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.impl.reflect.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterTypeHasEventMethodForTypePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.StaticMethodPredicate;

/**
 * This factory implementation creates instances {@link GenericEventBindingConnector}.
 * 
 * @author Matthias Hanisch
 */
@SuppressWarnings("unchecked")
public class GenericEventBindingConnectorFactory extends AbstractMethodBasedEventBindingConnectorFactory {

    private Predicate<Method> fixedPredicates = Predicates.and(new MethodParameterCountPredicate(1),
            Predicates.not(new StaticMethodPredicate()), new PublicMethodPredicate());

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

        Predicate<Method> predicates = Predicates.and(new MethodNamePredicate(methodNameRegEx),
                new MethodParameterTypeHasEventMethodForTypePredicate(eventType), fixedPredicates);
        Iterable<Method> methods = Iterables.filter(Arrays.asList(eventSourceType.getMethods()), predicates);
        if (Iterables.size(methods) == 1) {
            return methods.iterator().next();
        } else {
            return null;
        }
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

        Predicate<Method> predicates = Predicates.and(new MethodParametersPredicate(eventType), fixedPredicates);
        Iterable<Method> methods = Iterables.filter(Arrays.asList(listenerType.getMethods()), predicates);
        if (Iterables.size(methods) == 1) {
            return methods.iterator().next();
        } else {
            return null;
        }
    }
}
