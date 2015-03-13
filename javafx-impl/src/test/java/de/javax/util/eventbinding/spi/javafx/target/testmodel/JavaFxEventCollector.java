package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import java.util.LinkedList;

public class JavaFxEventCollector {

    public static class Entry {
        String id;
        Object event;

        public String getId() {
            return id;
        }

        public Object getEvent() {
            return event;
        }
    }

    private static LinkedList<Entry> events = new LinkedList<Entry>();

    public static void addEvent(String id, Object event) {
        Entry e = new Entry();
        e.event = event;
        e.id = id;
        events.add(e);
    }

    public static boolean hasMoreElements() {
        synchronized (JavaFxEventCollector.class) {
            return !events.isEmpty();
        }
    }

    public static void clear() {
        events.clear();
    }

    public static Entry getElement() {
        if (events.isEmpty()) {
            return null;
        } else {
            return events.poll();
        }
    }
}
