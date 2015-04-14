package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;

import com.google.common.base.Predicate;

/**
 * A predicate methods with a certain number of method parameters will apply to.
 * 
 * @author Matthias Hanisch
 */
public class MethodParameterCountPredicate implements Predicate<Method> {

    private final int parameterCount;

    public MethodParameterCountPredicate(int parameterCount) {
        this.parameterCount = parameterCount;

    }

    @Override
    public boolean apply(Method element) {
        return element.getParameterTypes().length == parameterCount;
    }

}