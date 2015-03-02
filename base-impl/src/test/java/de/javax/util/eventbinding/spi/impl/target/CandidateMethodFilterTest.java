package de.javax.util.eventbinding.spi.impl.target;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CandidateMethodFilterTest {
	
	public static final List<String> ACCEPTED_METHOD_NAMES = Arrays.asList("handleEvents", "staticHandleEvents");
	public static final List<String> DENIED_METHOD_NAMES = Arrays.asList("withReturnType", "moreThanOneParam", "abstractMethod");

	@Test
	public void shouldAcceptOnlyPublicVoidMethodsWithJavaFxEventAsOnlyParameter() {
		DefaultCandidateMethodFilter filter = new DefaultCandidateMethodFilter();
		
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
		
		public void handleEvents(Object event) {
		}
		
		public int withReturnType(Object event) {
			return 0;
		}
		
		public void moreThanOneParam(Object bla, String event) {
		}
		
		public abstract void abstractMethod(Object event);
		
		public static void staticHandleEvents(Object event) {
		}
	}
}
