package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterTypeHasEventMethodForTypePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParametersPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.NotPredicate;

/**
 * Default implementation of EventListenerProvider assuming that there are methods called
 * addxxxListener and removexxxListener for registering/unregistering event listeners for event types.
 * @author Matthias Hanisch
 *
 */
public class DefaultEventListenerProvider implements EventListenerProvider {

    @Override
    public EventListenerAdapter createEventListenerAdapter(Object eventSource,
            Class<?> eventType) {
        Class<?> eventSourceType = eventSource.getClass();
        // find method for registering event listeners of the given type
        Method addMethod = findListenerMethod(eventSourceType, eventType, "add.+Listener");
        // find  method for unregistering event listeners of the given type
        Method removeMethod = findListenerMethod(eventSourceType, eventType, "remove.+Listener");
        // there should be one addxxListener and one removexxxListener method
        if(addMethod!=null&&removeMethod!=null) {
            Class<?> listenerTypeAdd = addMethod.getParameterTypes()[0];
            Class<?> listenerTypeRemove = removeMethod.getParameterTypes()[0];
            // the event listener types of both methods should match
            if(listenerTypeAdd.equals(listenerTypeRemove)) {
                Method eventMethod = findEventMethod(listenerTypeAdd, eventType);
                // create default event listener adapter using these methods
                return new DefaultEventListenerAdapter(eventSource, addMethod, removeMethod, eventMethod, listenerTypeAdd);
            } 
        }
        // if this provider cannot provide a EventListenerAdapter return null so that the next can proceed
        return null;
    }

    private Method findListenerMethod(Class<?> eventSourceType, Class<?> eventType, String methodNameRegEx) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(eventSourceType.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(1)).filter(new MethodNamePredicate(methodNameRegEx))
                .filter(new MethodParameterTypeHasEventMethodForTypePredicate(eventType));
        if(filter.getElements().size()==1) {
            return filter.getElements().iterator().next();
        }
        return null;
    }
    
    private Method findEventMethod(Class<?> listenerType, Class<?> eventType) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(listenerType.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(1))
                .filter(new MethodParametersPredicate(eventType));
        if(filter.getElements().size()==1) {
            return filter.getElements().iterator().next();
        }
        return null;
    }

}
