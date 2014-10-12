package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.javax.util.eventbinding.source.NestedEventSourceAlias;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.impl.EventBindingSpiUtils;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodParameterTypeHasEventMethodForTypePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodNamePredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.MethodParameterCountPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.PublicMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate.StaticMethodPredicate;
import de.javax.util.eventbinding.spi.impl.reflect.NotPredicate;

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
    private Set<EventSource> allEventSources;

    public DefaultEventSourceProvider(Object source) {
        if (source == null) {
            throw new NullPointerException("Undefined source!");
        }
        this.source = source;
    }

    @Override
    public EventSource findEventSource(String id, Class<?> eventType) {
        Set<EventSource> collectedSources = getEventSources();
        collectedSources = filterByType(filterById(collectedSources, id), eventType);
        if (collectedSources.size() == 1) {
            return collectedSources.iterator().next();
        } else {
            // TODO what if more than one?
            return null;
        }
    }

    private Set<EventSource> filterById(Set<EventSource> eventSources, String id) {
        Set<EventSource> filteredSources = new HashSet<EventSource>();
        for (EventSource eventSource : eventSources) {
            if (id.equals(eventSource.getId())) {
                filteredSources.add(eventSource);
            }
        }
        return filteredSources;
    }

    @Override
    public Set<EventSource> findEventSourcesByType(Class<?> eventType) {
        Set<EventSource> collectedSources = getEventSources();
        return filterByType(collectedSources, eventType);
    }

    private Set<EventSource> filterByType(Set<EventSource> eventSources, Class<?> eventType) {
        Set<EventSource> filteredSources = new HashSet<EventSource>();
        for (EventSource eventSource : eventSources) {
            if (sourceSupportsEventType(eventSource.getType(), eventType)) {
                filteredSources.add(eventSource);
            }
        }
        return filteredSources;
    }

    private Set<EventSource> getEventSources() {
        if (this.allEventSources == null) {
            this.allEventSources = new HashSet<EventSource>();
            this.allEventSources.addAll(findEventSources(this.source, this.source.getClass(), "",
                    Collections.EMPTY_LIST));
        }
        return this.allEventSources;
    }

    private boolean sourceSupportsEventType(Class<?> type, Class<?> eventType) {
        Filter<Method> filter = new Filter<Method>(new HashSet<Method>(Arrays.asList(type.getMethods())))
                .filter(new PublicMethodPredicate()).filter(new NotPredicate<Method>(new StaticMethodPredicate()))
                .filter(new MethodParameterCountPredicate(1)).filter(new MethodNamePredicate("add.+Listener"))
                .filter(new MethodParameterTypeHasEventMethodForTypePredicate(eventType));
        return filter.getElements().size() > 0;
    }

    private Set<EventSource> findEventSources(Object source, Class<?> sourceClass, String sourceId, List<Field> tree) {
        Set<EventSource> collectedSources = new HashSet<EventSource>();
        Field[] declaredFields = sourceClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String eventSourceFieldId = getEventSourceId(declaredField);
            if (eventSourceFieldId != null) {
                // TODO care about alias
                String id = EventBindingSpiUtils.extendSourceId(sourceId, eventSourceFieldId);
                collectedSources.add(createEventSource(source, id, null, tree, declaredField));
            }
            String eventSourceProviderPrefix = getEventSourceProviderPrefix(declaredField);
            if (eventSourceProviderPrefix != null) {
                String prefix = EventBindingSpiUtils.extendSourceId(sourceId, eventSourceProviderPrefix);
                List<Field> fields = new ArrayList<Field>(tree);
                fields.add(declaredField);
                collectedSources.addAll(findEventSources(source, declaredField.getType(), prefix, fields));
            }
        }
        return collectedSources;
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

    private EventSource createEventSource(Object source, String eventSourceId, String alias, List<Field> tree,
            Field declaredField) {
        List<Field> fields = new ArrayList<Field>(tree);
        fields.add(declaredField);
        return new DefaultEventSource(eventSourceId, alias, source, fields);
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

}
