package de.javax.util.eventbinding.impl.target;

import java.util.Date;

import de.javax.util.eventbinding.target.HandleEvent;

public class TestEventTargetProvider {
	
	static boolean handleEventStaticCalled;
	static boolean handleEventsStaticCalled;
	
	boolean handleEventCalled;
	boolean handleEventsCalled;

	public static void handleEventStatic(@HandleEvent(fromSource="stringEventSource") String stringEvent) {
		handleEventStaticCalled = true;
	}
	
	public static void handleEventsStatic(@HandleEvent Date dateEvent) {
		handleEventsStaticCalled = true;
	}
	
	public void handleEvent(@HandleEvent(fromSource="stringEventSource") String stringEvent) {
		handleEventCalled = true;
	}
	
	public void handleEvents(@HandleEvent Date dateEvent) {
		handleEventsCalled = true;
	}
}
