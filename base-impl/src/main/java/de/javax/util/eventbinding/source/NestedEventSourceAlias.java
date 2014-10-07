package de.javax.util.eventbinding.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO Documentation
 *
 * @author Frank Hardy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface NestedEventSourceAlias {

	/**
	 * TODO
	 *
	 * @return
	 */
	String eventSourceId();

	/**
	 * TODO
	 *
	 * @return
	 */
	String alias();
}
