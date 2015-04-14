package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * A Predicate methods apply to if their single parameter type has an event method with the given type.
 * 
 * @author Matthias Hanisch
 *
 */
@SuppressWarnings("unchecked")
public class MethodParameterTypeHasEventMethodForTypePredicate implements Predicate<Method> {

    private final Class<?> eventType;

    private Predicate<Method> fixedPredicates = Predicates.and(new MethodParameterCountPredicate(1),
            new MethodReturnTypePredicate(Void.TYPE), Predicates.not(new StaticMethodPredicate()),
            new PublicMethodPredicate());

    public MethodParameterTypeHasEventMethodForTypePredicate(Class<?> eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean apply(Method element) {
        Class<?> parameterType = element.getParameterTypes()[0];

        Predicate<Method> predicates = Predicates.and(new MethodParametersPredicate(eventType), fixedPredicates);
        Iterable<Method> methods = Iterables.filter(Arrays.asList(parameterType.getMethods()), predicates);
        return Iterables.size(methods) == 1;
    }

}
