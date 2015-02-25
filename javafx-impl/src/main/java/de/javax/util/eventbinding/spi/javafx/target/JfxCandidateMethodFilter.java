package de.javax.util.eventbinding.spi.javafx.target;

import java.lang.reflect.Method;

import javafx.event.Event;
import de.javax.util.eventbinding.spi.impl.target.DefaultCandidateMethodFilter;

/**
 * This extension of the default candidate method filter accepts only methods
 * where the parameter is assignable from {@link Event}.
 *
 * @author Frank Hardy
 */
public class JfxCandidateMethodFilter extends DefaultCandidateMethodFilter {

	@Override
	public boolean acceptMethod(Method method) {
		boolean methodAccepted = super.acceptMethod(method);
		if (methodAccepted) {
			methodAccepted = Event.class.isAssignableFrom(method.getParameters()[0].getType());
		}
		return methodAccepted;
	}
}
