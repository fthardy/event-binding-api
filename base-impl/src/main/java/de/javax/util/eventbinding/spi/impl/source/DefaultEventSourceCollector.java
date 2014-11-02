package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;

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
        Set<EventSource> collectedSources = new HashSet<EventSource>();
        Field[] declaredFields = eventSourceProvider.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceFieldId = this.getEventSourceId(declaredField, providerId);
            Object fieldValue = this.getFieldValue(declaredField, eventSourceProvider);
            if (eventSourceFieldId != null && fieldValue != null) {
                collectedSources.add(this.eventSourceFactory.createEventSource(eventSourceFieldId, fieldValue));
            }
            
            EventSourceId eventSourceProviderId = this.getEventSourceProviderId(declaredField, providerId);
            if (eventSourceProviderId != null && fieldValue != null) {
                // recursive call
                collectedSources.addAll(this.findEventSources(fieldValue, eventSourceProviderId));
            }
        }
        return collectedSources;
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
