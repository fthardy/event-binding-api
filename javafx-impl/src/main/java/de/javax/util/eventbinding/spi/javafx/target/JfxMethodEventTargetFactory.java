package de.javax.util.eventbinding.spi.javafx.target;

import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptor;
import de.javax.util.eventbinding.spi.javafx.target.metadata.JfxHandlerMethodDescriptor;

/**
 * Creates instances of {@link JfxEventTargetImpl}.
 *
 * @author Frank Hardy
 */
public class JfxMethodEventTargetFactory extends DefaultMethodEventTargetFactory {

	public JfxMethodEventTargetFactory(EventSourceIdSelectorFactory idSelectorFactory) {
		super(idSelectorFactory);
	}
	
	@Override
	public EventTarget createMethodEventTarget(Object handlerMethodOwner, String idSelectorPrefix, HandlerMethodDescriptor handlerMethodDescriptor) {
		return new JfxEventTargetImpl(
				idSelectorFactory.createEventSourceIdSelector(
						this.buildIdSelectorExpression(idSelectorPrefix, handlerMethodDescriptor.getIdSelectorExpression())),
				handlerMethodDescriptor.getHandlerMethod().getParameterTypes()[0],
				((JfxHandlerMethodDescriptor) handlerMethodDescriptor).getEventType(),
				this.createEventDispatcher(handlerMethodDescriptor.getHandlerMethod(), handlerMethodOwner));
	}
	
	private String buildIdSelectorExpression(String prefix, String selectorExpression) {
		return prefix + (prefix.isEmpty() ? "" : ".") + selectorExpression;
	}
}
