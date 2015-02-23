package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaFxEventCollector {
    private static List<Object> events = new ArrayList<Object>();

    public static void addEvent(Object event) {
        events.add(event);
    }

    public static List<Object> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void clear() {
        this.clear();
    }
}
