package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.source.EventBindingConnectorFactory;

public abstract class AbstractMethodBasedEventBindingConnectorFactory implements EventBindingConnectorFactory {

    @Override
    public EventBindingConnector createConnector(Object eventSource, Class<?> eventType) {
        Class<?> eventSourceType = eventSource.getClass();
        MethodBasedEventBindingConnectorFactoryInfo info = getInfo(eventSourceType, eventType);
        if (info != null && info.getRegisterEventBindingMethod() != null
                && info.getUnregisterEventBindingMethod() != null) {
            return createConnector(eventSource, info);
        }
        // if this provider cannot provide a EventListenerAdapter return null so
        // that the next can proceed
        return null;
    }

    protected abstract EventBindingConnector createConnector(Object eventSource,
            MethodBasedEventBindingConnectorFactoryInfo info);

    protected abstract MethodBasedEventBindingConnectorFactoryInfo getInfo(Class<?> eventSourceType, Class<?> eventType);

    public static class MethodBasedEventBindingConnectorFactoryInfo {
        private final Method registerEventBindingMethod;
        private final Method unregisterEventBindingMethod;
        private final Class<?> eventHandlerType;
        private final Method eventHandlerMethod;
        private final Class<?> eventType;

        public MethodBasedEventBindingConnectorFactoryInfo(Method registerEventBindingMethod,
                Method unregisterEventBindingMethod, Class<?> eventHandlerType, Method eventHandlerMethod,
                Class<?> eventType) {
            this.registerEventBindingMethod = registerEventBindingMethod;
            this.unregisterEventBindingMethod = unregisterEventBindingMethod;
            this.eventHandlerType = eventHandlerType;
            this.eventHandlerMethod = eventHandlerMethod;
            this.eventType = eventType;

        }

        public Method getEventHandlerMethod() {
            return eventHandlerMethod;
        }

        public Class<?> getEventHandlerType() {
            return eventHandlerType;
        }

        public Method getRegisterEventBindingMethod() {
            return registerEventBindingMethod;
        }

        public Method getUnregisterEventBindingMethod() {
            return unregisterEventBindingMethod;
        }

        public Class<?> getEventType() {
            return eventType;
        }

    }
}
