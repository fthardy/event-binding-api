package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.target.EventHandler;
import de.javax.util.eventbinding.target.FromEventSource;

/**
 * This implementation of an event target collector is based on event handler methods
 * which are annotated with {@link EventHandler}.
 *
 * @author Frank Hardy
 */
public class DefaultEventTargetCollector implements EventTargetCollector {

	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object targetProvider) {
		Set<Method> eventHandlerMethods = this.collectEventHandlerMethods(targetProvider.getClass());
		
		@SuppressWarnings("unchecked")
		Set<EventTarget> targets = eventHandlerMethods.isEmpty() ? Collections.EMPTY_SET : new HashSet<EventTarget>();
		for (Method targetMethod : eventHandlerMethods) {
			targets.add(this.createEventTarget(targetMethod, targetProvider));
		}
		return targets;
	}
	
	/**
	 * Collect all event handler methods from the given target provider class.
	 * 
	 * @param targetProviderClass
	 *            the class of the target provider object.
	 *            
	 * @return the set of all event handler methods.
	 */
	protected Set<Method> collectEventHandlerMethods(Class<?> targetProviderClass) {
		Set<Method> collectedMethods = new HashSet<Method>();
		for (Method method : targetProviderClass.getMethods()) {
			if (method.getAnnotation(EventHandler.class) != null) {
				if (this.isValidEventHandlerMethod(method)) {
					collectedMethods.add(method);
				} // TODO Maybe log an information here
			}
		}
		return collectedMethods;
	}

	/**
	 * Create a new instance of an event target.
	 * 
	 * @param eventHandlerMethod
	 *            the event handler method.
	 * @param targetProvider
	 *            the target provider object.
	 *            
	 * @return the new instance of the event target.
	 */
	protected EventTarget createEventTarget(Method eventHandlerMethod, Object targetProvider) {
		return new DefaultEventTarget(
				this.getSourceId(eventHandlerMethod),
				eventHandlerMethod.getParameterTypes()[0],
				new MethodAdaptingEventDispatcher(eventHandlerMethod, targetProvider));
	}
	
	/**
	 * Check if the methods parameter is annotated with {@link FromEventSource}.
	 * If so get the source identifier from the annotation.
	 * 
	 * @param eventHandlerMethod
	 *            the event handler method.
	 *            
	 * @return the source identifier from the annotation or <code>null</code> if
	 *         the parameter was not annotated.
	 */
	protected Object getSourceId(Method eventHandlerMethod) {
		Object sourceId = null;
		Annotation[][] parameterAnnotations = eventHandlerMethod.getParameterAnnotations();
		if (parameterAnnotations[0].length > 0) {
			for(Annotation annotation : parameterAnnotations[0]) {
				if (annotation instanceof FromEventSource) {
					sourceId = ((FromEventSource) annotation).value();
				}
			}
		}
		return sourceId;
	}
	
	private boolean isValidEventHandlerMethod(Method method) {
		boolean accepted = false;
		if (method.getReturnType() == Void.TYPE) {
			if (method.getParameterTypes().length == 1) {
				accepted = true;
			}
		}
		return accepted;
	}
}
