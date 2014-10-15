package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * Default implementation of an EventListenerAdapter using a method to
 * register an event listener and a method to unregister an event listener
 * created as proxy and calling {@link EventDispatcher#dispatchEvent(Object)}
 * when receiving an event.
 * @author Matthias Hanisch
 *
 */
public class DefaultEventListenerAdapter implements EventListenerAdapter {

    private static Method hashCodeMethod;
    private static Method equalsMethod;
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
     * The listener if registered or <code>null</code> if no listener was registered yet.
     */
    private Object listener = null;
    
    static {
        try {
            hashCodeMethod = Object.class.getMethod("hashCode", null);
            equalsMethod =
                Object.class.getMethod("equals", new Class[] { Object.class });
            toStringMethod = Object.class.getMethod("toString", null);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        } catch (SecurityException e) {
            throw new NoSuchMethodError(e.getMessage());
        }        
    }

    public DefaultEventListenerAdapter(Object eventSource, Method addMethod, Method removeMethod, Method eventMethod, Class<?> listenerType) {
        this.eventSource = eventSource;
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
        this.eventMethod = eventMethod;
        this.listenerType = listenerType;
    }

    @Override
    public void registerEventListener(final EventDispatcher eventDispatcher) {
        if(listener!=null) {
            throw new IllegalStateException("event already registered");
        }
        this.listener = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{listenerType}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class declaringClass = method.getDeclaringClass();

                if (declaringClass == Object.class) {
                    if (method.equals(hashCodeMethod)) {
                        return proxyHashCode(proxy);
                    } else if (method.equals(equalsMethod)) {
                        return proxyEquals(proxy, args[0]);
                    } else if (method.equals(toStringMethod)) {
                        return proxyToString(proxy);
                    } else {
                        throw new InternalError(
                            "unexpected Object method dispatched: " + method);
                    }
                } else {
                    if(eventMethod.equals(method)) {
                        eventDispatcher.dispatchEvent(args[0]);;
                        return null;
                    } else {
                        return method.invoke(proxy, args);
                    }
                }
            }
        });
        try {
            this.addMethod.invoke(this.eventSource, this.listener);
        } catch (IllegalAccessException e) {
            // TODO which type of exception
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO which type of exception
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO which type of exception
            e.printStackTrace();
        }
    }

    @Override
    public void unregisterEventListener() {
        if(listener!=null) {
            try {
                this.removeMethod.invoke(eventSource, this.listener);
            } catch (IllegalAccessException e) {
                // TODO which type of exception
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO which type of exception
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO which type of exception
                e.printStackTrace();
            }
            this.listener = null;
        }
    }

    protected Integer proxyHashCode(Object proxy) {
        return new Integer(System.identityHashCode(proxy));
    }

    protected Boolean proxyEquals(Object proxy, Object other) {
        return (proxy == other ? Boolean.TRUE : Boolean.FALSE);
    }

    protected String proxyToString(Object proxy) {
        return proxy.getClass().getName() + '@' +
            Integer.toHexString(proxy.hashCode());
    }    
}
