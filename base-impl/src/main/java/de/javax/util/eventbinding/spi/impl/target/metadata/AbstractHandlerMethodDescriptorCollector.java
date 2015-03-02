package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract base class for handler method meta data collector implementations.
 *
 * @author Frank Hardy
 */
public abstract class AbstractHandlerMethodDescriptorCollector implements HandlerMethodDescriptorCollector {

	@Override
	public Set<HandlerMethodDescriptor> collectHandlerMethodDescriptorsFrom(Class<?> eventTargetProviderClass) {
		Set<HandlerMethodDescriptor> descriptors = new HashSet<HandlerMethodDescriptor>();
		for (Method method : eventTargetProviderClass.getMethods()) {
			if (this.isHandlerMethod(method)) {
				HandlerMethodDescriptor descriptor = this.getHandlerMethodDescriptor(method);
				if (descriptor != null) {
					descriptors.add(descriptor);
				}
			}
		}
		return descriptors;
	}
	
	/**
	 * Checks if the given method is a potential handler method.
	 * 
	 * @param method
	 *            the method to check.
	 * 
	 * @return <code>true</code> if the method is a potential handler method.
	 */
	protected boolean isHandlerMethod(Method method) {
		int modifiers = method.getModifiers();
		return !Modifier.isAbstract(modifiers) && method.getReturnType() == Void.TYPE && method.getParameterTypes().length == 1;
	}
	
	/**
	 * Get the handler method descriptor with the meta data.
	 * 
	 * @param method
	 *            the method object representing the handler method.
	 * 
	 * @return the descriptor instance or <code>null</code> if the given method
	 *         is not a handler method.
	 */
	protected abstract HandlerMethodDescriptor getHandlerMethodDescriptor(Method method);
}