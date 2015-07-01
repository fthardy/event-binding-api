package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

import javafx.event.EventType;

import org.junit.Assert;

import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;

class EventTargetInfo {
    private Class<?> eventClass;
    private String eventSourceId;
    private EventType<?> eventType;

    public EventTargetInfo(Class<?> eventClass, EventType<?> eventType, String eventSourceId) {
        this.eventClass = eventClass;
        this.eventType = eventType;
        this.eventSourceId = eventSourceId;
    }

    static EventTargetInfo create(Class<?> eventClass, EventType<?> eventType, String eventSourceId) {
        return new EventTargetInfo(eventClass, eventType, eventSourceId);
    }

    public void assertEqualsOneTarget(Set<EventTarget> eventTargets) {
        for (EventTarget eventTarget : eventTargets) {
            Assert.assertTrue(eventTarget instanceof JfxEventTarget);
            JfxEventTarget jfxEventTarget = (JfxEventTarget) eventTarget;

            if (jfxEventTarget.getEventClass().equals(eventClass)
                    && ((DefaultEventTarget) jfxEventTarget).getEventSourceIdSelector().toString()
                            .equals(this.eventSourceId) && jfxEventTarget.getEventType().equals(this.eventType)) {
                return;
            }
        }
        Assert.fail("no event target matches");
    }
}