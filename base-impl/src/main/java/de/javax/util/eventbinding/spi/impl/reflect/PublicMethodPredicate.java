package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.google.common.base.Predicate;

/**
 * A predicate public methods will apply to.
 * 
 * @author Matthias Hanisch
 */
public class PublicMethodPredicate implements Predicate<Method> {
    @Override
    public boolean apply(Method element) {
        return Modifier.isPublic(element.getModifiers());
    }
}