package de.javax.util.eventbinding.spi.javafx.target;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javafx.event.Event;

import org.junit.Test;

public class JfxCandidateMethodFilterTest {
	
	public static final List<String> ACCEPTED_METHOD_NAMES = Arrays.asList("handleEvents");
	public static final List<String> DENIED_METHOD_NAMES = Arrays.asList("handleActionEvents", "abstractMethod");

	@Test
	public void shouldAcceptOnlyPublicVoidMethodsWithJavaFxEventAsOnlyParameter() {
		JfxCandidateMethodFilter filter = new JfxCandidateMethodFilter();
		
		int count = 0;
		for (Method method : TestTargetProvider.class.getMethods()) {
			String methodName = method.getName();
			if (ACCEPTED_METHOD_NAMES.contains(methodName) || DENIED_METHOD_NAMES.contains(methodName)) {
				count++;
				if (filter.acceptMethod(method)) {
					assertTrue(ACCEPTED_METHOD_NAMES.contains(methodName));
				} else {
					assertTrue(DENIED_METHOD_NAMES.contains(methodName));
				}
			}
		}
		assertEquals(ACCEPTED_METHOD_NAMES.size() + DENIED_METHOD_NAMES.size(), count);
	}

	public static abstract class TestTargetProvider {
		
		public void handleEvents(Event event) {
		}
		
		public void handleActionEvents(ActionEvent type) {
		}
		
		public abstract void abstractMethod(Event event);
	}
}
