package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a field within an event target provider object which contains a
 * nested event target provider object.<br/>
 * 
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface EventTargetProvider {

    /**
     * @return the expression that defines from which event source(s) the events
     *         are delegated to the event target provider.
     */
    String from() default "*";
}
