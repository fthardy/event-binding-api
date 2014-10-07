package de.javax.util.eventbinding.impl.source;

import de.javax.util.eventbinding.source.EventSource;
import de.javax.util.eventbinding.source.EventSourceProvider;
import de.javax.util.eventbinding.source.NestedEventSourceAlias;

public class TestEventSourceProvider {

	@EventSource("fieldEventSource") 
	private final Object eventSource;
	
	@NestedEventSourceAlias(eventSourceId="nestedProvider.someEventSource", alias="nestedEventSource")
	@EventSourceProvider("nestedProvider")
	private final Object nestedEventSourceProvider;
	
	public TestEventSourceProvider(Object eventSource, Object nestedEventSourceProvider) {
		this.eventSource = eventSource;
		this.nestedEventSourceProvider = nestedEventSourceProvider;
	}
}
