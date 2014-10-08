package de.javax.util.eventbinding.impl.source;

import de.javax.util.eventbinding.source.EventSource;
import de.javax.util.eventbinding.source.EventSourceProvider;

public class TestEventSourceProvider {
  
  @EventSource("firstEventSource")
  private TestEventSource firstEventSource;

  @EventSource("secondEventSource")
  private TestEventSource secondEventSource;
  
  @EventSourceProvider("nestedProvider")
  private NestedTestEventSourceProvider nestedProvider;
  
  
//	@EventSource("fieldEventSource") 
//	private final Object eventSource;
//	
//	@NestedEventSourceAlias(eventSourceId="nestedProvider.someEventSource", alias="nestedEventSource")
//	@EventSourceProvider("nestedProvider")
//	private final Object nestedEventSourceProvider;
//	
//	public TestEventSourceProvider(Object eventSource, Object nestedEventSourceProvider) {
//		this.eventSource = eventSource;
//		this.nestedEventSourceProvider = nestedEventSourceProvider;
//	}
}
