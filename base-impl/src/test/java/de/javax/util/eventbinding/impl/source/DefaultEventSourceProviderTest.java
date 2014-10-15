package de.javax.util.eventbinding.impl.source;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceProvider;

public class DefaultEventSourceProviderTest {

    private TestEventSourceProvider testEventSourceProvider;
    private EventSourceProvider eventSourceProvider;

    @Before
    public void prepare() throws Exception {
        testEventSourceProvider = new TestEventSourceProvider();
        eventSourceProvider = new DefaultEventSourceProvider(testEventSourceProvider);
    }

    @Test
    public void findEventSourceByType() throws Exception {
        Set<EventSource> eventSources = eventSourceProvider.findEventSourcesByType(TestEvent.class);
        Assert.assertEquals(4, eventSources.size());
    }

    @Test
    public void findEventSourceById() throws Exception {
        EventSource eventSource = eventSourceProvider.findEventSource("firstEventSource", TestEvent.class);
        Assert.assertNotNull(eventSource);
        eventSource = eventSourceProvider.findEventSource("secondEventSource", TestEvent.class);
        Assert.assertNotNull(eventSource);
        eventSource = eventSourceProvider.findEventSource("nestedProvider.firstEventSource", TestEvent.class);
        Assert.assertNotNull(eventSource);
        eventSource = eventSourceProvider.findEventSource("nestedProvider.secondEventSource", TestEvent.class);
        Assert.assertNotNull(eventSource);
        eventSource = eventSourceProvider.findEventSource("noEventSource", TestEvent.class);
        Assert.assertNull(eventSource);
    }

    @Test
    public void processEvent() throws Exception {
        EventSource eventSource = eventSourceProvider.findEventSource("firstEventSource", TestEvent.class);
        EventDispatcher dispatcher = new EventDispatcher() {
            
            @Override
            public void dispatchEvent(Object event) {
                System.out.println(event);
            }
        };
        eventSource.register(dispatcher);
        testEventSourceProvider.firstEventSource.fireTestEvent(new TestEvent());
        Thread.sleep(1000);
        eventSource.unregisterEventDispatcher();
        testEventSourceProvider.firstEventSource.fireTestEvent(new TestEvent());
        Thread.sleep(1000);
        
    }
}
