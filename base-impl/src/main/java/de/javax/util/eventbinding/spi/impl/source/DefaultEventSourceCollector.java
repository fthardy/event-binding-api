package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.Predicate;

/**
 * This implementation of an event source provider is based on sources which
 * event source components are annotated with
 * {@link de.javax.util.eventbinding.source.EventSource} ,
 * {@link EventSourceCollector} and {@link NestedEventSourceAlias}.
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSourceCollector implements EventSourceCollector {

    @Override
    public void bindTargetToSources(final EventTarget eventTarget, Object eventSourceProvider) {
        Filter<EventSource> matchingEventSources = 
                new Filter<EventSource>(this.collectEventSourcesFrom(eventSourceProvider)).filter(
                        new Predicate<EventSource>() {
                            @Override
                            public boolean apply(EventSource eventSource) {
                                return eventTarget.getEventSourceIdSelector().matches(eventSource.getId());
                            }
                        });
        
        Set<EventSource> boundSources = new HashSet<EventSource>();
        for(EventSource eventSource : matchingEventSources.getElements()) {
            if (eventSource.bindTo(eventTarget)) {
                boundSources.add(eventSource);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<EventSource> collectEventSourcesFrom(Object eventSourceProvider) {
        
        //TODO cache?
        
        Set<EventSource> foundEventSources;
        foundEventSources = new HashSet<EventSource>();
        foundEventSources.addAll(this.findEventSources(eventSourceProvider, Collections.EMPTY_LIST, null));
        return foundEventSources;
    }

    private Set<EventSource> findEventSources(Object eventSourceProvider, List<Field> tree, EventSourceId providerId) {
        Set<EventSource> collectedSources = new HashSet<EventSource>();
        Field[] declaredFields = eventSourceProvider.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceFieldId = this.getEventSourceId(declaredField, providerId);
            Object fieldValue = getFieldValue(declaredField, eventSourceProvider);
            if (eventSourceFieldId != null && fieldValue != null) {
                collectedSources.add(this.createEventSourceObject(fieldValue, eventSourceFieldId, tree, declaredField));
            }
            
            EventSourceId eventSourceProviderId = this.getEventSourceProviderId(declaredField, providerId);
            if (eventSourceProviderId != null && fieldValue != null) {
                List<Field> fields = new ArrayList<Field>(tree);
                fields.add(declaredField);
                collectedSources.addAll(this.findEventSources(fieldValue, fields, eventSourceProviderId));
            }
        }
        return collectedSources;
    }

    private static Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);  
        try {
            return field.get(object);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
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

    private EventSource createEventSourceObject(
            Object source, EventSourceId eventSourceId, List<Field> tree, Field declaredField) {
        List<Field> fields = new ArrayList<Field>(tree);
        fields.add(declaredField);
        return new DefaultEventSource(eventSourceId, source);
    }

    private EventSourceId getEventSourceId(Field declaredField, EventSourceId parentId) {
        de.javax.util.eventbinding.source.EventSource annotation = declaredField.getAnnotation(
                de.javax.util.eventbinding.source.EventSource.class);
        if(annotation == null) {
            return null;
        }
        String id = annotation.value();
        
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
