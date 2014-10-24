package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * A generic implementation of a {@link EventBindingConnector}.<br/>
 * It uses a method to register an event listener and a method to unregister an
 * event listener created as proxy and calling
 * {@link EventDispatcher#dispatchEvent(Object)} when receiving an event.
 * 
 * @author Matthias Hanisch
 */
public class GenericEventBindingConnector implements EventBindingConnector {

    /**
     * The Method Object.hashCode() used to identify calls of .hashCode() on the
     * proxy used by this DefaultEventListenerAdapter.
     */
    private static Method hashCodeMethod;
    /**
     * The Method Object.equals() used to identify calls of .equals() on the
     * proxy used by this DefaultEventListenerAdapter.
     */
    private static Method equalsMethod;
    /**
     * The Method Object.toString() used to identify calls of .toString() on the
     * proxy used by this DefaultEventListenerAdapter.
     */
    private static Method toStringMethod;
    /**
     * The event source the listeners were registered/unregistered on.
     */
    private final Object eventSource;
    /**
     * The method to call to register an event listener on the event source.
     */
    private final Method addMethod;
    /**
     * The method to call to unregister an event listener from the event source.
     */
    private final Method removeMethod;
    /**
     * The type of the event listener. It is used to create a proxy instance of
     * the event listener delegating to the EventDispatcher.
     */
    private final Class<?> listenerType;
    /**
     * The event method to be proxied.
     */
    private Method eventMethod;
    /**
     * The listener if registered or <code>null</code> if no listener was
     * registered yet.
     */
    private Object listener = null;

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

    public GenericEventBindingConnector(Object eventSource, Method addMethod, Method removeMethod, Method eventMethod,
            Class<?> listenerType) {
        this.eventSource = eventSource;
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
        this.eventMethod = eventMethod;
        this.listenerType = listenerType;
    }

    /**
     * Registers an event listener for the event source of this
     * DefaultEventListenerAdapter by creating a proxy for the event listener
     * type delegating the event to the given EventDispatcher.
     * 
     * @param eventDispatcher
     *            The event dispatcher to register as event listener.
     */
    @Override
    public void connect(final EventDispatcher eventDispatcher) {
        if (listener != null) {
            throw new IllegalStateException("event already registered");
        }
        this.listener = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { listenerType }, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class<?> declaringClass = method.getDeclaringClass();
                        /*
                         * don't proxy calls of .hashCode(), .equals() and
                         * .toString() as this would produce a
                         * StackOverflowError. call local implementations
                         * instead
                         */
                        if (declaringClass == Object.class) {
                            if (method.equals(hashCodeMethod)) {
                                return proxyHashCode(proxy);
                            } else if (method.equals(equalsMethod)) {
                                return proxyEquals(proxy, args[0]);
                            } else if (method.equals(toStringMethod)) {
                                return proxyToString(proxy);
                            } else {
                                // TODO check for other calls (finalize?)
                                throw new InternalError("unexpected Object method dispatched: " + method);
                            }
                        } else {
                            // dispatch the event to the EventDispatcher
                            if (eventMethod.equals(method)) {
                                eventDispatcher.dispatchEvent(args[0]);
                                ;
                                return null;
                            } else {
                                // TODO should not be called, throw Exception
                                // instead?
                                return method.invoke(proxy, args);
                            }
                        }
                    }
                });
        try {
            this.addMethod.invoke(this.eventSource, this.listener);
        } catch (IllegalAccessException e) {
            throw new EventSourceAccessException("registering event listener failed", e);
        } catch (IllegalArgumentException e) {
            throw new EventSourceAccessException("registering event listener failed", e);
        } catch (InvocationTargetException e) {
            throw new EventSourceAccessException("registering event listener failed", e);
        }
    }

    /**
     * Unregisters the event listener by using a certain unregister method on
     * the event source object.
     */
    @Override
    public void disconnect() {
        if (listener != null) {
            try {
                this.removeMethod.invoke(eventSource, this.listener);
            } catch (IllegalAccessException e) {
                throw new EventSourceAccessException("unregistering event listener failed", e);
            } catch (IllegalArgumentException e) {
                throw new EventSourceAccessException("unregistering event listener failed", e);
            } catch (InvocationTargetException e) {
                throw new EventSourceAccessException("unregistering event listener failed", e);
            }
            this.listener = null;
        }
    }

    /**
     * Local implementation for retrieving hash code for an object by returning
     * {@link System#identityHashCode(Object)} for the given object.
     * 
     * @param proxy
     *            The proxy instance.
     * @return The hash code.
     */
    protected Integer proxyHashCode(Object proxy) {
        return new Integer(System.identityHashCode(proxy));
    }

    /**
     * Local implementation for determining equality between a proxy instance
     * and a given object comparing object identity.
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
     * Local implementation for returning a toString-value for a proxy instance
     * using concatenation of class name and hash code.
     * 
     * @param proxy
     *            The proxy instance.
     * @return Unique toString-value.
     */
    protected String proxyToString(Object proxy) {
        return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
    }
}
