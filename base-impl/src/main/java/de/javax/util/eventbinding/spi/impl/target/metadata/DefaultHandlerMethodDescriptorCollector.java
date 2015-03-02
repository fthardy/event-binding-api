package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.target.HandleEvent;

/**
 * The default implementation of a method handler descriptor collector.
 *
 * @author Frank Hardy
 */
public class DefaultHandlerMethodDescriptorCollector extends AbstractAnnotationBasedHandlerMethodDescriptorCollector<HandleEvent> {

	public DefaultHandlerMethodDescriptorCollector() {
		super(HandleEvent.class);
	}

	@Override
	protected HandlerMethodDescriptor createHandlerMethodDescriptor(Method method, HandleEvent annotation) {
		// TODO Validate the ID-selector expression
		String idSelectorExpression = annotation.from();
		return new HandlerMethodDescriptor(method, idSelectorExpression);
	}
}
