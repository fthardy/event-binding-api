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
	 * @return a prefix which is used for all ID-selector expressions for the event targets of
	 *         the annotated event target provider.
	 */
    String from() default "";
}
