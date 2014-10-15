package de.javax.util.eventbinding.impl.source;

import de.javax.util.eventbinding.source.EventSource;

public class NestedTestEventSourceProvider {
    @EventSource("firstEventSource")
    private TestEventSource firstEventSource = new TestEventSource();

    @EventSource("secondEventSource")
    private TestEventSource secondEventSource = new TestEventSource();

}
