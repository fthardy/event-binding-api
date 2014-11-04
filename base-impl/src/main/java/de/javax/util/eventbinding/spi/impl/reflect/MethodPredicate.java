package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A MethodPredicate specifies a predicate for checking {@link Method} elements.
 * It also provides a set of predefined predicates to be used.
 * 
 * @author Matthias Hanisch
 * 
 */
public interface MethodPredicate extends Predicate<Method> {

	/**
	 * A predicate public methods will apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class PublicMethodPredicate implements MethodPredicate {
		@Override
		public boolean apply(Method element) {
			return Modifier.isPublic(element.getModifiers());
		}
	}

	/**
	 * A predicate static methods will apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class StaticMethodPredicate implements MethodPredicate {
		@Override
		public boolean apply(Method element) {
			return Modifier.isStatic(element.getModifiers());
		}
	}

	/**
	 * A predicate methods with a certain number of method parameters will apply
	 * to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class MethodParameterCountPredicate implements
			MethodPredicate {

		private final int parameterCount;

		public MethodParameterCountPredicate(int parameterCount) {
			this.parameterCount = parameterCount;

		}

		@Override
		public boolean apply(Method element) {
			return element.getParameterTypes().length == parameterCount;
		}

	}

	/**
	 * A predicate methods with a certain return type will apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class MethodReturnTypePredicate implements MethodPredicate {

		private final Class<?> returnType;

		public MethodReturnTypePredicate(Class<?> returnType) {
			this.returnType = returnType;
		}

		@Override
		public boolean apply(Method element) {
			return returnType.equals(element.getReturnType());
		}

	}

	/**
	 * A predicate methods with matching method parameter types will apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class MethodParametersPredicate implements MethodPredicate {
		private Class<?>[] parameterTypes;

		public MethodParametersPredicate(Class<?>... parameterTypes) {
			this.parameterTypes = parameterTypes;
		}

		@Override
		public boolean apply(Method element) {
			Class<?>[] methodParameterTypes = element.getParameterTypes();
			if (parameterTypes.length != methodParameterTypes.length) {
				return false;
			}
			for (int i = 0; i < parameterTypes.length; i++) {
				if (!parameterTypes[i].equals(methodParameterTypes[i])) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * A predicate methods with a method name matching a regular expression will
	 * apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class MethodNamePredicate implements MethodPredicate {
		private final Pattern pattern;

		public MethodNamePredicate(String regex) {
			this.pattern = Pattern.compile(regex);
		}

		@Override
		public boolean apply(Method element) {
			Matcher matcher = pattern.matcher(element.getName());
			return matcher.matches();
		}
	}

	/**
	 * A predicate methods with a certain annotation will apply to.
	 * 
	 * @author Matthias Hanisch
	 */
	public static class MethodWithAnnotationPredicate implements
			MethodPredicate {
		private final Class<? extends Annotation> annotationType;

		public MethodWithAnnotationPredicate(
				Class<? extends Annotation> annotationType) {
			this.annotationType = annotationType;
		}

		@Override
		public boolean apply(Method element) {
			return element.getAnnotation(annotationType) != null;
		}
	}
}