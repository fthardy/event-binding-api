package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A Predicate methods apply to if their single parameter type has an
 * event method with the given type.
 * @author Matthias Hanisch
 *
 */
public class MethodParameterTypeHasEventMethodForTypePredicate implements
    MethodPredicate {

  private final Class<?> eventType;

  public MethodParameterTypeHasEventMethodForTypePredicate(Class<?> eventType) {
    this.eventType = eventType;
  }

  @Override
  public boolean apply(Method element) {
    Class<?> parameterType = element.getParameterTypes()[0];
    Filter<Method> methods = 
        new Filter<Method>(new HashSet<Method>(Arrays.asList(parameterType.getMethods())))
        .filter(new PublicMethodPredicate())
        .filter(new NotPredicate<Method>(new StaticMethodPredicate()))
        .filter(new MethodParameterCountPredicate(1))
        .filter(new MethodReturnTypePredicate(Void.TYPE))
        .filter(new MethodParametersPredicate(eventType));
    return methods.getElements().size()==1;
  }

}
