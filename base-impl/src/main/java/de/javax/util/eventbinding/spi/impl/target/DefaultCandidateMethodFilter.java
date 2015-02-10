package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This default filter implementation accepts all non-abstract, public void
 * methods with one parameter.
 *
 * @author Frank Hardy
 */
public class DefaultCandidateMethodFilter implements CandidateMethodFilter {

	@Override
	public boolean acceptMethod(Method method) {
		int modifiers = method.getModifiers();
		return Modifier.isPublic(modifiers)
				&& !Modifier.isAbstract(modifiers)
				&& method.getParameterTypes().length == 1
				&& method.getReturnType() == Void.TYPE;
	}
}