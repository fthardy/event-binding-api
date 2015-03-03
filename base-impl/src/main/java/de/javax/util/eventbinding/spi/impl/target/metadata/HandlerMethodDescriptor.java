package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;

/**
 * Encapsulates the meta data of a handler method.
 *
 * @author Frank Hardy
 */
public class HandlerMethodDescriptor {
	
	private final Method handlerMethod;
	private final String idSelectorExpression;
	
	public HandlerMethodDescriptor(Method method, String idSelectorExpression) {
		this.handlerMethod = method;
		String expression = idSelectorExpression.trim();
		this.idSelectorExpression = expression.isEmpty() ? EventSourceIdSelector.WILDCARD : expression;
	}
	
	public Method getHandlerMethod() {
		return handlerMethod;
	}
	
	public String getIdSelectorExpression() {
		return idSelectorExpression;
	}
}