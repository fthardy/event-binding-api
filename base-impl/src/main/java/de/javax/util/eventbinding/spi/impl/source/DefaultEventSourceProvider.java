package de.javax.util.eventbinding.spi.impl.source;

import java.util.Set;

import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceProvider;

/**
 * TODO
 * 
 * @author Frank Hardy
 */
public class DefaultEventSourceProvider implements EventSourceProvider {

	private final Object sourceProvider;
	
	public DefaultEventSourceProvider(Object sourceProvider) {
		if (sourceProvider == null) {
			throw new NullPointerException("Undefined source provider object!");
		}
		this.sourceProvider = sourceProvider;
	}
	
	@Override
	public EventSource findEventSource(Object id, Class<?> eventType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EventSource> findEventSourcesByType(Class<?> eventType) {
		// TODO Auto-generated method stub
		return null;
	}
}
