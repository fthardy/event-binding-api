package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.google.common.base.Predicate;

/**
 * A predicate methods with a certain annotation will apply to.
 * 
 * @author Matthias Hanisch
 */
public class MethodWithAnnotationPredicate implements Predicate<Method> {
    private final Class<? extends Annotation> annotationType;

    public MethodWithAnnotationPredicate(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public boolean apply(Method element) {
        return element.getAnnotation(annotationType) != null;
    }
}