package de.javax.util.eventbinding.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO Documentation
 *
 * @author Frank Hardy
 * 
 * @see EventSourceProvider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface EventSource {
	
	/**
	 * @return the identifier of the event source.
	 */
	String value();
}
