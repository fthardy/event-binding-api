package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.javax.util.eventbinding.source.NestedEventSourceAlias;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.Predicate;

/**
 * This implementation of an event source provider is based on sources which
 * event source components are annotated with
 * {@link de.javax.util.eventbinding.source.EventSource} ,
 * {@link EventSourceProvider} and {@link NestedEventSourceAlias}.
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSourceProvider implements EventSourceProvider {

    private final Object source;
    private Set<EventSourceObject> allEventSourceObjects;

    public DefaultEventSourceProvider(Object source) {
        if (source == null) {
            throw new NullPointerException("Undefined source!");
        }
        this.source = source;
    }
    
    @Override
    public boolean bindTargetToSources(EventTarget eventTargets) {
        // TODO Auto-generated method stub
        return false;
    }

    @Deprecated
    public EventSource findEventSource(final String id, Class<?> eventType) {
        Filter<EventSourceObject> filter = new Filter<DefaultEventSourceProvider.EventSourceObject>(getEventSourceObjects());
        filter = filter.filter(new Predicate<EventSourceObject>() {

            @Override
            public boolean apply(EventSourceObject eventSourceObject) {
                return id.equals(eventSourceObject.getEventSourceId());
            }
        });
        for(EventSourceObject eventSourceObject:filter.getElements()) {
            EventListenerAdapter adapter = EventListenerProviderFactory.createAdapter(eventSourceObject.getEventSource(), eventType);
            if(adapter!=null)  {
                return createEventSource(eventSourceObject, adapter);
            }
        }
        return null;
    }

    @Deprecated
    public Set<EventSource> findEventSourcesByType(Class<?> eventType) {
        Set<EventSource> eventSources = new HashSet<EventSource>();
        Collection<EventSourceObject> eventSourceObjects = getEventSourceObjects();
        for(EventSourceObject eventSourceObject:eventSourceObjects)  {
            EventListenerAdapter adapter = EventListenerProviderFactory.createAdapter(eventSourceObject.getEventSource(), eventType);
            if(adapter!=null) {
                eventSources.add(createEventSource(eventSourceObject, adapter));
            }
        }
        return eventSources;
    }

    private EventSource createEventSource(EventSourceObject eventSourceObject,
            EventListenerAdapter adapter) {
        return new DefaultEventSource(adapter);
    }

    private Collection<EventSourceObject> getEventSourceObjects() {
        if (this.allEventSourceObjects == null) {
            this.allEventSourceObjects = new HashSet<EventSourceObject>();
            this.allEventSourceObjects.addAll(
                    this.findEventSources(this.source, this.source.getClass(), Collections.EMPTY_LIST, null));
        }
        return this.allEventSourceObjects;
    }

    private Set<EventSourceObject> findEventSources(
            Object source, Class<?> sourceClass, List<Field> tree, EventSourceId parentId) {
        Set<EventSourceObject> collectedSources = new HashSet<EventSourceObject>();
        Field[] declaredFields = sourceClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceFieldId = getEventSourceId(declaredField, parentId);
            if (eventSourceFieldId != null) {
                // TODO care about alias
                collectedSources.add(createEventSourceObject(
                        getFieldValue(declaredField, source), eventSourceFieldId, tree, declaredField));
            }
            
            EventSourceId eventSourceProviderPrefix = getEventSourceProviderPrefix(declaredField, parentId);
            if (eventSourceProviderPrefix != null) {
                List<Field> fields = new ArrayList<Field>(tree);
                fields.add(declaredField);
                collectedSources.addAll(findEventSources(
                        getFieldValue(declaredField, source),
                        declaredField.getType(),
                        fields,
                        eventSourceProviderPrefix));
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

    private EventSourceId getEventSourceProviderPrefix(Field declaredField, EventSourceId parentId) {
        de.javax.util.eventbinding.source.EventSourceProvider annotation = declaredField.getAnnotation(
                de.javax.util.eventbinding.source.EventSourceProvider.class);
        String id = annotation == null ? null : annotation.value();
        
        EventSourceId newId = null;
        if (parentId == null) {
            newId = new EventSourceId(id); 
        } else {
            List<String> names = parentId.getNames();
            names.add(id);
            newId = new EventSourceId(names);
        }
        return newId;
    }

    private EventSourceObject createEventSourceObject(
            Object source, EventSourceId eventSourceId, List<Field> tree, Field declaredField) {
        List<Field> fields = new ArrayList<Field>(tree);
        fields.add(declaredField);
        return EventSourceObject.create(eventSourceId, source);
    }

    private EventSourceId getEventSourceId(Field declaredField, EventSourceId parentId) {
        de.javax.util.eventbinding.source.EventSource annotation = declaredField.getAnnotation(
                de.javax.util.eventbinding.source.EventSource.class);
        String id = annotation == null ? null : annotation.value();
        
        EventSourceId newId = null;
        if (parentId == null) {
            newId = new EventSourceId(id); 
        } else {
            List<String> names = parentId.getNames();
            names.add(id);
            newId = new EventSourceId(names);
        }
        return newId;
    }

    static class EventSourceObject {
        
        EventSourceId eventSourceId;
        Object eventSource;
        
        static EventSourceObject create(EventSourceId eventSourceId, Object eventSource) {
            EventSourceObject eventSourceObject = new EventSourceObject();
            eventSourceObject.eventSourceId = eventSourceId;
            eventSourceObject.eventSource = eventSource;
            return eventSourceObject;
        }
        
        public Object getEventSource() {
            return eventSource;
        }
        public EventSourceId getEventSourceId() {
            return eventSourceId;
        }
    }
}
