package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;

/**
 * Encapsulates the information from the class of an event target provider object.
 * 
 * @author Frank Hardy
 */
public class TargetProviderClassInfo {

    private final Set<HandlerMethodInfo> handlerMethods;
    private final Set<NestedProviderFieldInfo> nestedProviderFieldInfos;

    public TargetProviderClassInfo(
            Set<HandlerMethodInfo> handlerMethods, Set<NestedProviderFieldInfo> nestedProviderFieldInfos) {
        this.handlerMethods = handlerMethods;
        this.nestedProviderFieldInfos = nestedProviderFieldInfos;
    }

    public Set<HandlerMethodInfo> getHandlerMethods() {
        return this.handlerMethods == null ? null : Collections.unmodifiableSet(this.handlerMethods);
    }
    
    public Set<NestedProviderFieldInfo> getNestedProviderFieldInfos() {
        return this.nestedProviderFieldInfos == null ? null : Collections.unmodifiableSet(this.nestedProviderFieldInfos);
    }

    /**
     * Encapsulates the information from an event handler method of an event target provider object.
     * 
     * @author Frank Hardy
     */
    public static class HandlerMethodInfo {

        private final Method method;
        private final EventSourceIdSelector idSelector;
        private final Object metaData;
        
        public HandlerMethodInfo(Method method, EventSourceIdSelector idSelector, Object metaData) {
        	this.method = method;
        	this.idSelector = idSelector;
        	this.metaData = metaData;
        }

        public Method getMethod() {
            return this.method;
        }

        public EventSourceIdSelector getIdSelector() {
            return this.idSelector;
        }
        
        public Object getMetaData() {
			return this.metaData;
		}
    }
    
    /**
     * Encapsulates the information from a nested target provider field of an event target provider object.
     * 
     * @author Frank Hardy
     */
    public static class NestedProviderFieldInfo {
        
        private final Field field;
        private final EventSourceIdSelector idSelector;
        
        public NestedProviderFieldInfo(Field field, EventSourceIdSelector idSelector) {
            this.field = field;
            this.idSelector = idSelector;
        }
        
        public Field getField() {
            return this.field;
        }
        
        public EventSourceIdSelector getIdSelector() {
            return this.idSelector;
        }
    }
}
