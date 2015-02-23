package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark JavaFx specific event handler methods.
 * 
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface HandleJfxEvent {

    /**
     * @return the expression that defines from which event source(s) the event
     *         is expected.
     */
    String from() default "*";
    
	/**
	 * @return the name of a static field which provides an event type. The
	 *         static field is expected to be defined at the class of the
	 *         accepted events or in any super class of that class.
	 */
    String eventType() default "ANY";
}
