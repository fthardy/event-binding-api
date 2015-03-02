package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Field;

/**
 * Encapsulates the meta data of the fields of a target provider which refer to a nested target provider.
 *
 * @author Frank Hardy
 */
public class TargetProviderFieldDescriptor {
	
	private final Field field;
	private final String prefix;
	
	public TargetProviderFieldDescriptor(Field field, String prefix) {
		this.field = field;
		this.prefix = prefix;
	}
	
	public Field getField() {
		return field;
	}
	
	public String getPrefix() {
		return prefix;
	}
}