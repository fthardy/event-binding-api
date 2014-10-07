package de.javax.util.eventbinding.impl.target;

import java.util.Date;

import de.javax.util.eventbinding.target.EventHandler;
import de.javax.util.eventbinding.target.FromEventSource;

public class TestEventTargetProvider {
	
	static boolean handleEventStaticCalled;
	static boolean handleEventsStaticCalled;
	
	boolean handleEventCalled;
	boolean handleEventsCalled;

	@EventHandler public static void handleEventStatic(@FromEventSource("stringEventSource") String stringEvent) {
		handleEventStaticCalled = true;
	}
	
	@EventHandler public static void handleEventsStatic(Date dateEvent) {
		handleEventsStaticCalled = true;
	}
	
	@EventHandler public void handleEvent(@FromEventSource("stringEventSource") String stringEvent) {
		handleEventCalled = true;
	}
	
	@EventHandler public void handleEvents(Date dateEvent) {
		handleEventsCalled = true;
	}
}
