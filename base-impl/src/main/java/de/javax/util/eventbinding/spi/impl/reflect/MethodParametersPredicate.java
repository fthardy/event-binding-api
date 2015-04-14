package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;

import com.google.common.base.Predicate;

/**
 * A predicate methods with matching method parameter types will apply to.
 * 
 * @author Matthias Hanisch
 */
public class MethodParametersPredicate implements Predicate<Method> {
    private Class<?>[] parameterTypes;

    public MethodParametersPredicate(Class<?>... parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public boolean apply(Method element) {
        Class<?>[] methodParameterTypes = element.getParameterTypes();
        if (parameterTypes.length != methodParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].equals(methodParameterTypes[i])) {
                return false;
            }
        }
        return true;
    }
}