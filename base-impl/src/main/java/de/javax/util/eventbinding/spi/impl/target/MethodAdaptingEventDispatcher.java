package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * An event dispatcher implementation which dispatches events to an event
 * handler method.<br/>
 * Supports instance methods as well as static methods.
 * 
 * @author Frank Hardy
 */
public class MethodAdaptingEventDispatcher implements EventDispatcher {
	
	private final Method method;
	private final Object instance;

	/**
	 * Creates a new instance of this event dispatcher implementation.
	 * 
	 * @param method
	 *            the event handler method to be adapted.
	 * @param instance
	 *            the object instance at which the method will be invoked. If
	 *            <code>method</code> represents a static method then this
	 *            parameter will be ignored.
	 */
	public MethodAdaptingEventDispatcher(Method method, Object instance) {
		if (method == null) {
			throw new NullPointerException("Undefined method!");
		}
		this.method = method;
		if (Modifier.isStatic(method.getModifiers())) {
			this.instance = null;
		} else {
			if (instance != null) {
				assert this.method.getDeclaringClass().isInstance(instance) : 
					"The given method is not a method of the given object instance!";
			}
			this.instance = instance;
		}
	}
	
	@Override
	public String toString() {
		String className = this.instance == null ? 
				this.method.getDeclaringClass().getName() : this.instance.getClass().getName(); 
		
		StringBuilder builder = 
				new StringBuilder(this.method.toGenericString()).append(" @ ").append(className);
		if (this.instance != null) {
			builder.append(" (").append(System.identityHashCode(this.instance)).append(")");
		}
		return builder.toString();
	}

	/**
	 * Dispatches an event to the event receiver method.
	 * 
	 * @param event
	 *            the event to be dispatched to the target.
	 * 
	 * @throws EventDispatchingFailedException
	 *             when the invocation of the event receiver method fails.
	 */
	public void dispatchEvent(Object event) {
		try {
			this.method.invoke(this.instance, event);
		} catch (Exception e) {
			throw new EventDispatchingFailedException(e);
		}
	}

	/**
	 * Will be thrown by
	 * {@link MethodAdaptingEventDispatcher#dispatchEvent(Object)} when
	 * the invocation of the event receiver method fails.
	 * 
	 * @author Frank Hardy
	 */
	public static class EventDispatchingFailedException extends RuntimeException {
		
		private static final long serialVersionUID = -6186856400058014593L;

		/**
		 * Creates a new instance of this runtime exception.
		 * 
		 * @param t
		 *            the causing throwable.
		 */
		public EventDispatchingFailedException(Throwable t) {
			super(t);
		}
	}
}
