package de.javax.util.eventbinding.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the object of the annotated field is a provider of event
 * sources.<br/>
 * The value defines an identifier for the provider which is used as a prefix
 * for the identifiers of the event sources from the provider. A period
 * character is used as a separator for the identifiers of the provider and its
 * sources. Example: <code>SomeEventSourceProvider.SomeEventSource</code><br/>
 * The identifiers have to follow the naming rules of java identifiers.
 * 
 * @author Frank Hardy
 * 
 * @see EventSource
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface EventSourceProvider {

	/**
	 * @return the identifier of the event source provider which serves as a
	 *         prefix for the identifiers of the provided event sources.
	 */
	String value() default "";
}
