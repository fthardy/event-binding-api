package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;
import de.javax.util.eventbinding.target.HandleEvent;

public class DefaultHandlerMethodInfoExtractor implements HandlerMethodInfoExtractor {
	
	private final EventSourceIdSelectorFactory idSelectorFactory;
	
	public DefaultHandlerMethodInfoExtractor(EventSourceIdSelectorFactory idSelectorFactory) {
		this.idSelectorFactory= idSelectorFactory;
	}
	
	@Override
	public HandlerMethodInfo extractHandlerMethodInfo(Method method) {
		Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
		for (Annotation annotation : parameterAnnotations) {
			if (annotation.annotationType() == HandleEvent.class) {
				HandleEvent handleEventAnnotation = (HandleEvent) annotation;
				String selectorExpression = handleEventAnnotation.from().trim();
				if (selectorExpression.isEmpty()) {
					selectorExpression = EventSourceIdSelector.WILDCARD;
				}
				return new HandlerMethodInfo(
						method,
						this.idSelectorFactory.createEventSourceIdSelector(selectorExpression),
						this.getMetaDataFromHandleEventAnnotation(handleEventAnnotation));
			}
		}
		return null;
	}
	
	/**
	 * Extract the meta data from the given annotation.
	 * 
	 * @param annotation
	 *            the parameter annotation of the event handler method.
	 * 
	 * @return the meta data object or <code>null</code> if there is none.
	 */
	protected Object getMetaDataFromHandleEventAnnotation(HandleEvent annotation) {
		return null;
	}
}