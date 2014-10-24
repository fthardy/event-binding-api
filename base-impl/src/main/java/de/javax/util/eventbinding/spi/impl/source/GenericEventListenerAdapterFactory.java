package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.source.EventListenerAdapterFactory;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterTypeHasEventMethodForTypePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.NotPredicate;

/**
 * Default implementation of EventListenerProvider assuming that there are
 * methods called addxxxListener and removexxxListener for
 * registering/unregistering event listeners for event types.
 * 
 * @author Matthias Hanisch
 */
public class GenericEventListenerAdapterFactory implements EventListenerAdapterFactory {

    @Override
    public EventListenerAdapter createEventListenerAdapter(Object eventSource, Class<?> eventType) {
        Class<?> eventSourceType = eventSource.getClass();
        // find method for registering event listeners of the given type
        Method addMethod = findListenerMethod(eventSourceType, eventType, "add.+Listener");
        // find method for unregistering event listeners of the given type
        Method removeMethod = findListenerMethod(eventSourceType, eventType, "remove.+Listener");
        // there should be one addxxListener and one removexxxListener method
        if (addMethod != null && removeMethod != null) {
            Class<?> listenerTypeAdd = addMethod.getParameterTypes()[0];
            Class<?> listenerTypeRemove = removeMethod.getParameterTypes()[0];
            // the event listener types of both methods should match
            if (listenerTypeAdd.equals(listenerTypeRemove)) {
                Method eventMethod = findEventMethod(listenerTypeAdd, eventType);
                // create default event listener adapter using these methods
                return new GenericEventListenerAdapter(eventSource, addMethod, removeMethod, eventMethod,
                        listenerTypeAdd);
            }
        }
        // if this provider cannot provide a EventListenerAdapter return null so
        // that the next can proceed
        return null;
    }

    /**
     * Returns a Method of the given eventSourceType with a method name matching
     * the given regular expression and having a single method parameter of the
     * given eventType.
     * 
     * @param eventSourceType
     *            The type of the event source object.
     * @param eventType
     *            The type of the event which should be method parameter type
     *            also.
     * @param methodNameRegEx
     *            The name of the method to find. (e.g.
     *            &quot;add.+Listener&quot; can be used to find regular methods
     *            adding listener instances)
     * @return A Method object if there is exactly one method matching all
     *         criteria otherwise <code>null</code> is returned.
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
     * Returns a Method of the given listenerType with single method parameter
     * of the given eventType.
     * 
     * @param listenerType
     *            The type of the event listener.
     * @param eventType
     *            The type of the event which should be method parameter type
     *            also.
     * @return A Method object if there is exactly one method matching all
     *         criteria otherwise <code>null</code> is returned.
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
