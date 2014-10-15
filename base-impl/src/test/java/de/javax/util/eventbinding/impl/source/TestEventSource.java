package de.javax.util.eventbinding.impl.source;

import java.util.ArrayList;
import java.util.List;

public class TestEventSource {

    private List<TestEventSourceListener> listeners = new ArrayList<TestEventSourceListener>();

    public void addTestEventSourceListener(TestEventSourceListener listener) {
        this.listeners.add(listener);
    }

    public void removeTestEventSourceListener(TestEventSourceListener listener) {
        this.listeners.remove(listener);
    }

    public void fireTestEvent(TestEvent event) {
        for(TestEventSourceListener listener:listeners) {
            listener.handle(event);
        }
    }

}
