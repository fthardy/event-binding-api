package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;

import com.google.common.base.Predicate;

/**
 * A predicate methods with a certain return type will apply to.
 * 
 * @author Matthias Hanisch
 */
public class MethodReturnTypePredicate implements Predicate<Method> {

    private final Class<?> returnType;

    public MethodReturnTypePredicate(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean apply(Method element) {
        return returnType.equals(element.getReturnType());
    }

}