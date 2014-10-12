package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the event parameter of an event handler method at an event target
 * object.<br/>
 * An event handler method is a non-abstract, public method which can be either
 * static or non-static. It must have only one parameter and must have a return
 * value of type <code>void</code>.<br/>
 * The optional attribute <code>fromSource</code> defines an identifier of an
 * event source. When a source identifier is defined the event handler method
 * expects the events only from an event source which has the defined
 * identifier. If the source identifier is not defined the method expects the
 * events from any event source.
 * 
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface HandleEvent {

	/**
	 * @return the identifier of the event source.
	 */
	String fromSource() default "";
}
