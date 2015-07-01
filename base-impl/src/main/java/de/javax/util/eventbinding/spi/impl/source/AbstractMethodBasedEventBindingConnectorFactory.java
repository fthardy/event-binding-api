package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Method;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import de.javax.util.eventbinding.source.EventBindingConnector;
import de.javax.util.eventbinding.source.EventBindingConnectorFactory;

public abstract class AbstractMethodBasedEventBindingConnectorFactory implements EventBindingConnectorFactory {

    private Cache<EventBindingConnectorCacheKey, EventBindingConnector> cache = CacheBuilder.newBuilder()
            .<EventBindingConnectorCacheKey, EventBindingConnector> build();

    @Override
    public EventBindingConnector createConnector(Object eventSource, Class<?> eventType) {
        EventBindingConnectorCacheKey key = new EventBindingConnectorCacheKey(eventSource, eventType);
        EventBindingConnector connector = this.cache.getIfPresent(key);
        if (connector == null) {
            Class<?> eventSourceType = eventSource.getClass();
            MethodBasedEventBindingConnectorFactoryInfo info = getInfo(eventSourceType, eventType);
            if (info != null && info.getRegisterEventBindingMethod() != null
                    && info.getUnregisterEventBindingMethod() != null) {
                connector = createConnector(eventSource, info);
                if (connector != null) {
                    this.cache.put(key, connector);
                }
            }
        }
        // if this provider cannot provide a EventListenerAdapter return null so
        // that the next can proceed
        return connector;
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

    /**
     * EventBindingConnectorCacheKey is the key for a cache used by the EventBindingConnectorFactory as combination
     * if the event source instance and the event type.
     * 
     */
    private static class EventBindingConnectorCacheKey {
        private final Object eventSource;
        private final Class<?> eventType;

        EventBindingConnectorCacheKey(Object eventSource, Class<?> eventType) {
            this.eventSource = eventSource;
            this.eventType = eventType;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((eventSource == null) ? 0 : eventSource.hashCode());
            result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EventBindingConnectorCacheKey other = (EventBindingConnectorCacheKey) obj;
            if (eventSource == null) {
                if (other.eventSource != null)
                    return false;
            } else if (!eventSource.equals(other.eventSource))
                return false;
            if (eventType == null) {
                if (other.eventType != null)
                    return false;
            } else if (!eventType.equals(other.eventType))
                return false;
            return true;
        }

    }
}
