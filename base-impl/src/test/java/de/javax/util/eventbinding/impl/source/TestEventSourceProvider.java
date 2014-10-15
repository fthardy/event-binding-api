package de.javax.util.eventbinding.impl.source;

import de.javax.util.eventbinding.source.EventSource;
import de.javax.util.eventbinding.source.EventSourceProvider;

public class TestEventSourceProvider {

    @EventSource("firstEventSource")
    TestEventSource firstEventSource = new TestEventSource();

    @EventSource("secondEventSource")
    private TestEventSource secondEventSource = new TestEventSource();

    @EventSourceProvider("nestedProvider")
    private NestedTestEventSourceProvider nestedProvider = new NestedTestEventSourceProvider();
}
