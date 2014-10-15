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
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.impl.EventBindingSpiUtils;
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

    @Override
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
            this.allEventSourceObjects.addAll(findEventSources(this.source, this.source.getClass(), "",
                    Collections.EMPTY_LIST));
        }
        return this.allEventSourceObjects;
    }

    private Set<EventSourceObject> findEventSources(Object source, Class<?> sourceClass, String sourceId, List<Field> tree) {
        Set<EventSourceObject> collectedSources = new HashSet<EventSourceObject>();
        Field[] declaredFields = sourceClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String eventSourceFieldId = getEventSourceId(declaredField);
            if (eventSourceFieldId != null) {
                // TODO care about alias
                String id = EventBindingSpiUtils.extendSourceId(sourceId, eventSourceFieldId);
                collectedSources.add(createEventSourceObject(getFieldValue(declaredField, source), id, null, tree, declaredField));
            }
            String eventSourceProviderPrefix = getEventSourceProviderPrefix(declaredField);
            if (eventSourceProviderPrefix != null) {
                String prefix = EventBindingSpiUtils.extendSourceId(sourceId, eventSourceProviderPrefix);
                List<Field> fields = new ArrayList<Field>(tree);
                fields.add(declaredField);
                collectedSources.addAll(findEventSources(getFieldValue(declaredField, source), declaredField.getType(), prefix, fields));
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

    private String getEventSourceProviderPrefix(Field declaredField) {
        de.javax.util.eventbinding.source.EventSourceProvider annotation = declaredField
                .getAnnotation(de.javax.util.eventbinding.source.EventSourceProvider.class);
        if (annotation == null) {
            return null;
        } else {
            return annotation.value().trim();
        }
    }

    private EventSourceObject createEventSourceObject(Object source, String eventSourceId, String alias, List<Field> tree,
            Field declaredField) {
        List<Field> fields = new ArrayList<Field>(tree);
        fields.add(declaredField);
        return EventSourceObject.create(eventSourceId, alias, source);
    }

    private static String getEventSourceId(Field declaredField) {
        de.javax.util.eventbinding.source.EventSource annotation = declaredField
                .getAnnotation(de.javax.util.eventbinding.source.EventSource.class);
        if (annotation == null) {
            return null;
        } else {
            return annotation.value().trim();
        }
    }

    static class EventSourceObject {
        String eventSourceId;
        String alias;
        Object eventSource;
        static EventSourceObject create(String eventSourceId, String alias, Object eventSource) {
            EventSourceObject eventSourceObject = new EventSourceObject();
            eventSourceObject.eventSourceId = eventSourceId;
            eventSourceObject.alias = alias;
            eventSourceObject.eventSource = eventSource;
            return eventSourceObject;
        }
        public String getAlias() {
            return alias;
        }
        public Object getEventSource() {
            return eventSource;
        }
        public String getEventSourceId() {
            return eventSourceId;
        }
    }

}
