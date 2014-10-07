package de.javax.util.eventbinding.target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the identifier of the event source from which an event handler
 * method expects to receive the events.<br/>
 * This annotation has only an effect when set at the parameter of a method
 * which is annotated with {@link EventHandler}.
 * 
 * @author Frank Hardy
 * 
 * @see EventHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface FromEventSource {

	/**
	 * @return the identifier of the event source.
	 */
	String value();
}
