package de.javax.util.eventbinding.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies an event source in an event source provider object.<br/>
 * An event source is identified by its identifier which has to conform to the
 * naming rules of Java identifiers.
 * 
 * @author Frank Hardy
 * 
 * @see EventSourceProvider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface EventSource {

    /**
     * @return the identifier of the event source.
     */
    String value() default "";
}
