package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptor;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;
import de.javax.util.eventbinding.spi.impl.target.MethodAdaptingEventDispatcher;

/**
 * This default implementation creates instances of {@link DefaultEventTarget}.<br/>
 * The event dispatcher implementation created by
 * {@link #createEventDispatcher(Method, Object)} is
 * {@link MethodAdaptingEventDispatcher}.
 *
 * @author Frank Hardy
 * 
 * @see DefaultEventTarget
 * @see MethodAdaptingEventDispatcher
 */
public class DefaultMethodEventTargetFactory implements MethodEventTargetFactory {

	protected final EventSourceIdSelectorFactory idSelectorFactory;
	
	public DefaultMethodEventTargetFactory(EventSourceIdSelectorFactory idSelectorFactory) {
		this.idSelectorFactory = idSelectorFactory;
	}
	
	@Override
	public EventTarget createMethodEventTarget(Object handlerMethodOwner, String idSelectorPrefix, HandlerMethodDescriptor handlerMethodDescriptor) {
		return new DefaultEventTarget(
				this.idSelectorFactory.createEventSourceIdSelector(idSelectorPrefix + '.' + handlerMethodDescriptor.getIdSelectorExpression()),
				handlerMethodDescriptor.getHandlerMethod().getParameterTypes()[0],
				this.createEventDispatcher(handlerMethodDescriptor.getHandlerMethod(), handlerMethodOwner));
	}
	
	/**
	 * Creates an instance of an event dispatcher.
	 * 
	 * @param method
	 *            the method to dispatch the event to.
	 * @param methodOwner
	 *            the owner of the method.
	 * 
	 * @return a new event dispatcher.
	 */
	protected EventDispatcher createEventDispatcher(Method method, Object methodOwner) {
		return new MethodAdaptingEventDispatcher(method, methodOwner);
	}
}
