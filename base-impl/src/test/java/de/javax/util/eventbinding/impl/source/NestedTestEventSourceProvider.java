package de.javax.util.eventbinding.impl.source;

import de.javax.util.eventbinding.source.EventSource;

public class NestedTestEventSourceProvider {
  @EventSource("firstEventSource")
  private TestEventSource firstEventSource;

  @EventSource("secondEventSource")
  private TestEventSource secondEventSource;

}
