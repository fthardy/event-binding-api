package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * Encapsulates the information about an event source provider object.
 * @author Matthias Hanisch
 *
 */
public class EventSourceProviderClassInfo {
    private Set<EventSourceFieldInfo> fieldInfos;
    private Set<EventSourceFieldInfo> nestedProviders;
    EventSourceProviderClassInfo(Set<EventSourceFieldInfo> fieldInfos, Set<EventSourceFieldInfo> nestedProviders) {
        this.fieldInfos = fieldInfos;
        this.nestedProviders = nestedProviders;
    }
    public Set<EventSourceFieldInfo> getFieldInfos() {
        return fieldInfos;
    }
    
    public Set<EventSourceFieldInfo> getNestedProviders() {
        return nestedProviders;
    }
    /**
     * Encapsulates the information about a field of an event source provider object.
     * @author Matthias Hanisch
     *
     */
    public static class EventSourceFieldInfo {
        private final Field field;
        private final EventSourceId eventSourceId;
        EventSourceFieldInfo(Field field, EventSourceId eventSourceId) {
            this.field = field;
            this.eventSourceId = eventSourceId;
        }
        
        public Field getField() {
            return field;
        }
        
        public EventSourceId getEventSourceId() {
            return eventSourceId;
        }
    }
}