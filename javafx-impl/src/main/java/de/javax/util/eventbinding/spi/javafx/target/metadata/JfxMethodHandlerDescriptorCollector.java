package de.javax.util.eventbinding.spi.javafx.target.metadata;

import java.lang.reflect.Method;

import javafx.event.Event;
import de.javax.util.eventbinding.spi.impl.target.metadata.AbstractAnnotationBasedHandlerMethodDescriptorCollector;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptor;
import de.javax.util.eventbinding.target.HandleJfxEvent;

/**
 * A JavaFx specific descriptor collector implementation which looks for a
 * {@link HandleJfxEvent} annotation at the handler method.
 *
 * @author Frank Hardy
 */
public class JfxMethodHandlerDescriptorCollector extends AbstractAnnotationBasedHandlerMethodDescriptorCollector<HandleJfxEvent> {

	public JfxMethodHandlerDescriptorCollector() {
		super(HandleJfxEvent.class);
	}
	
	@Override
	protected boolean isHandlerMethod(Method method) {
		return super.isHandlerMethod(method) && Event.class.isAssignableFrom(method.getParameterTypes()[0]);
	}

	@Override
	protected HandlerMethodDescriptor createHandlerMethodDescriptor(Method method, HandleJfxEvent annotation) {
		return new JfxHandlerMethodDescriptor(method, annotation.from(), annotation.eventType());
	}
}
