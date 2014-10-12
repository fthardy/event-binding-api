package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the object of the annotated field is a target provider.
 * 
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface EventTargetProvider {
	
	/**
	 * Defines a prefix for the event source identifiers of the underlying event handlers.
	 * Usually this is used to set the identifier name of a nested event source provider
	 * of the sources to be bound.
	 * 
	 * @return the identifier prefix.
	 */
	String sourceIdPrefix() default "";
}
