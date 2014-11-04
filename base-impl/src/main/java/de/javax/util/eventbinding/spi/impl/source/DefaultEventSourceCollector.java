package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.impl.source.EventSourceProviderClassInfo.EventSourceFieldInfo;

/**
 * This implementation of an event source provider is based on sources which
 * event source components are annotated with
 * {@link de.javax.util.eventbinding.source.EventSource} ,
 * {@link EventSourceCollector} and {@link NestedEventSourceAlias}.
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSourceCollector implements EventSourceCollector {
    
    private final EventSourceFactory eventSourceFactory;
    
    private final Map<Class<?>, EventSourceProviderClassInfo> cache = new HashMap<Class<?>, EventSourceProviderClassInfo>();
    
    /**
     * Creates a new instance of this event source collector.
     * 
     * @param eventSourceFactory
     *            the event source factory.
     */
    public DefaultEventSourceCollector(EventSourceFactory eventSourceFactory) {
        if (eventSourceFactory == null) {
            throw new NullPointerException("Undefined event source factory!");
        }
        this.eventSourceFactory = eventSourceFactory;
    }

    @Override
    public Set<EventSource> collectEventSourcesFrom(Object eventSourceProvider) {
        return this.findEventSources(eventSourceProvider, null);
    }

    private Set<EventSource> findEventSources(Object eventSourceProvider, EventSourceId providerId) {
        Class<?> eventSourceProviderClass = eventSourceProvider.getClass();
        EventSourceProviderClassInfo info = this.cache.get(eventSourceProviderClass);
        if(info==null) {
            info = new EventSourceProviderClassInfo(this.collectFieldInfos(eventSourceProviderClass, providerId), collectNestedEventSourceProviders(eventSourceProviderClass, providerId));
            cache.put(eventSourceProviderClass, info);
        }
        Set<EventSource> collectedSources = new HashSet<EventSource>();
        for(EventSourceFieldInfo fieldInfo:info.getFieldInfos()) {
            Object fieldValue = this.getFieldValue(fieldInfo.getField(), eventSourceProvider);
            if (fieldValue != null) {
                collectedSources.add(this.eventSourceFactory.createEventSource(fieldInfo.getEventSourceId(), fieldValue));
            }
        }
        for(EventSourceFieldInfo nestedProvider:info.getNestedProviders()) {
            Object fieldValue = this.getFieldValue(nestedProvider.getField(), eventSourceProvider);
            if (fieldValue != null) {
                // recursive call
                collectedSources.addAll(this.findEventSources(fieldValue, nestedProvider.getEventSourceId()));
            }
        }
        return collectedSources;
    }
    
    /**
     * Collects the nested event sources declared for a certain event source provider class.
     * @param eventSourceProviderClass The class to check for event sources.
     * @param providerId The event source id of the given event source provider class which will be the
     * parent event source id of the event sources found. 
     * @return A set of event sources encapsulated in an info object.
     */
    private Set<EventSourceFieldInfo> collectFieldInfos(Class<?> eventSourceProviderClass, EventSourceId providerId) {
        Set<EventSourceFieldInfo> collectedFields = new HashSet<EventSourceFieldInfo>();
        Field[] declaredFields = eventSourceProviderClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceFieldId = this.getEventSourceId(declaredField, providerId);
            if(eventSourceFieldId!=null){
                collectedFields.add(new EventSourceFieldInfo(declaredField, eventSourceFieldId));
            }
        }
        return collectedFields;
    }
    
    
    /**
     * Collects the nested event source providers declared for a certain event source provider class.
     * @param eventSourceProviderClass The class to check for nested event source providers.
     * @param providerId The event source id of the given event source provider class which will be the
     * parent event source id of the nested event source providers found. 
     * @return A set of nested event source providers encapsulated in an info object.
     */
    private Set<EventSourceFieldInfo> collectNestedEventSourceProviders(Class<?> eventSourceProviderClass, EventSourceId providerId) {
        Set<EventSourceFieldInfo> collectedFields = new HashSet<EventSourceFieldInfo>();
        Field[] declaredFields = eventSourceProviderClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceProviderId = this.getEventSourceProviderId(declaredField, providerId);
            if (eventSourceProviderId != null) {
                collectedFields.add(new EventSourceFieldInfo(declaredField, eventSourceProviderId));
            }
        }
        return collectedFields;
    }

    private Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);  
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new EventSourceAccessException("Failed to access field!", e);
        } finally {
            field.setAccessible(false);
        }
    }

    private EventSourceId getEventSourceProviderId(Field declaredField, EventSourceId parentId) {
        de.javax.util.eventbinding.source.EventSourceProvider annotation = declaredField.getAnnotation(
                de.javax.util.eventbinding.source.EventSourceProvider.class);
        if(annotation==null) {
            return null;
        }
        String id =  annotation.value();
        // if annotation has no value the field name is uses as id of the event source
        if("".equals(id)) {
            id = declaredField.getName();
        }
        
        EventSourceId newId = null;
        if (parentId == null) {
            newId = new EventSourceId(id); 
        } else {
            List<String> names = new ArrayList<String>(parentId.getNames());
            names.add(id);
            newId = new EventSourceId(names);
        }
        return newId;
    }

    private EventSourceId getEventSourceId(Field declaredField, EventSourceId parentId) {
        de.javax.util.eventbinding.source.EventSource annotation = declaredField.getAnnotation(
                de.javax.util.eventbinding.source.EventSource.class);
        if(annotation == null) {
            return null;
        }
        String id = annotation.value();
        // if annotation has no value the field name is uses as id of the event source
        if("".equals(id)) {
            id = declaredField.getName();
        }
        
        EventSourceId newId = null;
        if (parentId == null) {
            newId = new EventSourceId(id); 
        } else {
            List<String> names = new ArrayList<String>(parentId.getNames());
            names.add(id);
            newId = new EventSourceId(names);
        }
        return newId;
    }
    
    
}
