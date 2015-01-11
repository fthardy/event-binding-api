package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * The AbstractMethodBasedEventBindingConnector is the base class for EventBindingConnector which use method calls to
 * {@link EventBindingConnector#connect(EventDispatcher) connect} and {@link EventBindingConnector#disconnect()
 * disconnect} event source and event target.
 * 
 * @author Matthias Hanisch
 *
 */
public abstract class AbstractMethodBasedEventBindingConnector implements EventBindingConnector {

    /**
     * The Method Object.hashCode() used to identify calls of .hashCode() on the proxy used by this
     * DefaultEventListenerAdapter.
     */
    private static Method hashCodeMethod;
    /**
     * The Method Object.equals() used to identify calls of .equals() on the proxy used by this
     * DefaultEventListenerAdapter.
     */
    private static Method equalsMethod;
    /**
     * The Method Object.toString() used to identify calls of .toString() on the proxy used by this
     * DefaultEventListenerAdapter.
     */
    private static Method toStringMethod;

    /**
     * The event source the listeners were registered/unregistered on.
     */
    private final Object eventSource;
    /**
     * The method to call to register an event listener on the event source.
     */
    private final Method registerEventBindingMethod;
    /**
     * The method to call to unregister an event listener from the event source.
     */
    private final Method unregisterEventBindingMethod;
    /**
     * The type of the event handler. It is used to create a proxy instance of the event handler delegating to the
     * EventDispatcher.
     */
    private final Class<?> eventHandlerType;
    /**
     * The event method to be proxied.
     */
    private Method eventMethod;

    private final Class<?> eventType;
    /**
     * The event handler if registered or <code>null</code> if no event handler was registered yet.
     */
    private Object eventHandler;

    /**
     * Identify Object.hashCode(), Object.equals() and Object.toString().
     */
    static {
        try {
            hashCodeMethod = Object.class.getMethod("hashCode");
            equalsMethod = Object.class.getMethod("equals", new Class[] { Object.class });
            toStringMethod = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        } catch (SecurityException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    public AbstractMethodBasedEventBindingConnector(Object eventSource, Method registerEventBindingMethod,
            Method unregisterEventBindingMethod, Method eventMethod, Class<?> eventHandlerType, Class<?> eventType) {
        this.eventSource = eventSource;
        this.registerEventBindingMethod = registerEventBindingMethod;
        this.unregisterEventBindingMethod = unregisterEventBindingMethod;
        this.eventMethod = eventMethod;
        this.eventHandlerType = eventHandlerType;
        this.eventType = eventType;
    }

    @Override
    public final void connect(EventDispatcher eventDispatcher) {
        this.eventHandler = createEventHandler(eventMethod, eventHandlerType, eventDispatcher);
        connect(eventSource, registerEventBindingMethod, eventHandlerType, eventHandler, eventType);
    }

    @Override
    public final void disconnect() {
        if (eventHandler != null) {
            disconnect(eventSource, unregisterEventBindingMethod, eventHandlerType, eventHandler, eventType);
            eventHandler = null;
        }
    }

    /**
     * Connect the event source with the event target.
     * 
     * @param eventSource
     *            The event source.
     * @param registerEventBindingMethod
     *            The method to use to register the event handler instance to the event source.
     * @param eventHandlerType
     *            The type of the event handler (or listener).
     * @param eventHandler
     *            The proxy instance of the event handler which is delegating the call to the {@link EventDispatcher}.
     * @param eventType
     *            The type of the event.
     */
    protected abstract void connect(Object eventSource, Method registerEventBindingMethod, Class<?> eventHandlerType,
            Object eventHandler, Class<?> eventType);

    /**
     * Disconnect the event source from the event target.
     * 
     * @param eventSource
     *            The event source.
     * @param unregisterEventBindingMethod
     *            The method to use to unregister the event handler instance from the event source.
     * @param eventHandlerType
     *            The type of the event handler (or listener).
     * @param eventHandler
     *            The proxy instance of the event handler which is delegating the call to the {@link EventDispatcher}.
     * @param eventType
     *            The type of the event.
     */
    protected abstract void disconnect(Object eventSource, Method unregisterEventBindingMethod,
            Class<?> eventHandlerType, Object eventHandler, Class<?> eventType);

    /**
     * Local implementation for retrieving hash code for an object by returning {@link System#identityHashCode(Object)}
     * for the given object.
     * 
     * @param proxy
     *            The proxy instance.
     * @return The hash code.
     */
    protected Integer proxyHashCode(Object proxy) {
        return new Integer(System.identityHashCode(proxy));
    }

    /**
     * Local implementation for determining equality between a proxy instance and a given object comparing object
     * identity.
     * 
     * @param proxy
     *            The proxy instance.
     * @param other
     *            The object to compare to.
     * @return Returns whether both objects are identical.
     */
    protected Boolean proxyEquals(Object proxy, Object other) {
        return (proxy == other ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Local implementation for returning a toString-value for a proxy instance using concatenation of class name and
     * hash code.
     * 
     * @param proxy
     *            The proxy instance.
     * @return Unique toString-value.
     */
    protected String proxyToString(Object proxy) {
        return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
    }

    private Object createEventHandler(final Method eventMethod, Class<?> eventHandlerType,
            final EventDispatcher eventDispatcher) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { eventHandlerType }, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class<?> declaringClass = method.getDeclaringClass();
                        /*
                         * don't proxy calls of .hashCode(), .equals() and .toString() as this would produce a
                         * StackOverflowError. call local implementations instead
                         */
                        if (declaringClass == Object.class) {
                            if (method.equals(hashCodeMethod)) {
                                return proxyHashCode(proxy);
                            } else if (method.equals(equalsMethod)) {
                                return proxyEquals(proxy, args[0]);
                            } else if (method.equals(toStringMethod)) {
                                return proxyToString(proxy);
                            } else {
                                throw new EventSourceAccessException("unexpected Object method dispatched: " + method);
                            }
                        } else {
                            // dispatch the event to the EventDispatcher
                            if (eventMethod.equals(method)) {
                                eventDispatcher.dispatchEvent(args[0]);
                                return null;
                            } else {
                                throw new EventSourceAccessException("unexpected listener method dispatched: " + method);
                            }
                        }
                    }
                });
    }

}
