package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * An abstract base class for handler method meta data collector implementations which are based on annotations.<br/>
 *
 * @author Frank Hardy
 */
public abstract class AbstractAnnotationBasedHandlerMethodDescriptorCollector<T extends Annotation> extends AbstractHandlerMethodDescriptorCollector {
	
	private final Class<T> annotationTypeClass;

	public AbstractAnnotationBasedHandlerMethodDescriptorCollector(Class<T> annotationTypeClass) {
		this.annotationTypeClass = annotationTypeClass;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected HandlerMethodDescriptor getHandlerMethodDescriptor(Method method) {
		Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
		for (Annotation annotation : parameterAnnotations) {
			if (annotation.annotationType() == this.annotationTypeClass) {
				return this.createHandlerMethodDescriptor(method, (T) annotation);
			}
		}
		return null;
	}
	
	/**
	 * Creates a new descriptor instance.<br/>
	 * Is called by {@link #getHandlerMethodDescriptor(Method)} when the given
	 * method is annotated with the required annotation.
	 * 
	 * @param method
	 *            the handler method.
	 * @param annotation
	 *            the annotation containing the meta data.
	 * 
	 * @return the descriptor instance.
	 */
	protected abstract HandlerMethodDescriptor createHandlerMethodDescriptor(Method method, T annotation);
}
