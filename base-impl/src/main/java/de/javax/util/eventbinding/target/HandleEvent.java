package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the event parameter of an event handler method at an event target
 * provider object.<br/>
 * An event handler method is a non-abstract, public method which can be either
 * static or non-static. It must have only one parameter and must have a return
 * value of type <code>void</code>.<br/>
 * The optional attribute is used to define an expression that describes from
 * which event source(s) the events are expected by the event handler method.
 * 
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface HandleEvent {

    /**
     * @return the expression that defines from which event source(s) the event
     *         is expected.
     */
    String from() default "*";
}
