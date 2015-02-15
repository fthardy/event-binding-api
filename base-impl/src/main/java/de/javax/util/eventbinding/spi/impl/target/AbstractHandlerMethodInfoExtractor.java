package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;

/**
 * Abstract base class for a {@link HandlerMethodInfoExtractor}.
 *
 * @author Frank Hardy
 */
public abstract class AbstractHandlerMethodInfoExtractor<T extends Annotation> implements HandlerMethodInfoExtractor {
	
	private final Class<T> annotationTypeClass;
	private final EventSourceIdSelectorFactory idSelectorFactory;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param idSelectorFactory
	 *            the ID selector factory.
	 */
	public AbstractHandlerMethodInfoExtractor(Class<T> annotationTypeClass, EventSourceIdSelectorFactory idSelectorFactory) {
		if (annotationTypeClass == null) {
			throw new NullPointerException("Undefined annotation type class!");
		}
		this.annotationTypeClass = annotationTypeClass;
		if (idSelectorFactory == null) {
			throw new NullPointerException("Undefined ID selector factory!");
		}
		this.idSelectorFactory= idSelectorFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HandlerMethodInfo extractHandlerMethodInfo(Method method) {
		Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
		for (Annotation annotation : parameterAnnotations) {
			if (annotation.annotationType() == this.annotationTypeClass) {
				String selectorExpression = this.getIdSelectorExpressionFromAnnotation((T) annotation);
				if (selectorExpression == null || selectorExpression.isEmpty()) {
					selectorExpression = EventSourceIdSelector.WILDCARD;
				}
				return new HandlerMethodInfo(
						method,
						this.idSelectorFactory.createEventSourceIdSelector(selectorExpression),
						this.getMetaDataFromAnnotation((T) annotation));
			}
		}
		return null;
	}
	
	/**
	 * Get the ID selector expression from the annotation.
	 * 
	 * @param annotation
	 *            the handler method annotation.
	 * 
	 * @return the selector expression or <code>null</code>.
	 */
	protected abstract String getIdSelectorExpressionFromAnnotation(T annotation);
	
	/**
	 * Extract the meta data from the given annotation.
	 * 
	 * @param annotation
	 *            the parameter annotation of the event handler method.
	 * 
	 * @return the meta data object or <code>null</code> if there is none.
	 */
	protected abstract Object getMetaDataFromAnnotation(T annotation);
}