package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import de.javax.util.eventbinding.source.EventListenerAdapter;
import de.javax.util.eventbinding.source.EventListenerProvider;
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
    
    private static final String PROPERY_USE_CACHE ="DefaultEventSourceCollector.useCache";
    private Map<Object,Set<EventSourceCandidate>> cache;

    @Override
    public boolean bindTargetToSources(Object source, final EventTarget eventTarget) {
        Filter<EventSourceCandidate> filter = new Filter<EventSourceCandidate>(getEventSourceCandidates(source))
                .filter(new Predicate<EventSourceCandidate>() {
                    @Override
                    public boolean apply(EventSourceCandidate element) {
                        return eventTarget.getEventSourceIdSelector().matches(element.getEventSourceId());
                    }
                    
                });
        Set<EventSource> boundSources = new HashSet<EventSource>();
        for(EventSourceCandidate eventSourceObject:filter.getElements()) {
            EventListenerAdapter adapter = createAdapter(eventSourceObject.getEventSource(), eventTarget.getEventType());
            if(adapter!=null) {
                EventSource eventSource = new DefaultEventSource(adapter);
                adapter.registerEventListener(eventTarget.getEventDispatcher());
                boundSources.add(eventSource);
            }
        }
        eventTarget.setBoundSources(boundSources);
        return !boundSources.isEmpty();
    }

    @SuppressWarnings("unchecked")
    private Collection<EventSourceCandidate> getEventSourceCandidates(Object source) {
        Set<EventSourceCandidate> candidates = null;
        if(Boolean.getBoolean(PROPERY_USE_CACHE)) {
            if(cache == null) {
                cache = new HashMap<Object, Set<EventSourceCandidate>>();
            }
            candidates = cache.get(source);
        }
        if(candidates == null) {
            candidates = new HashSet<EventSourceCandidate>();
            candidates.addAll(this.findEventSources(source, source.getClass(), Collections.EMPTY_LIST, null));
            if(Boolean.getBoolean(PROPERY_USE_CACHE)) {
                cache.put(source, candidates);
            }
        }
        return candidates;
    }

    private Set<EventSourceCandidate> findEventSources(
            Object source, Class<?> sourceClass, List<Field> tree, EventSourceId parentId) {
        Set<EventSourceCandidate> collectedSources = new HashSet<EventSourceCandidate>();
        Field[] declaredFields = sourceClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            EventSourceId eventSourceFieldId = getEventSourceId(declaredField, parentId);
            Object fieldValue = getFieldValue(declaredField, source);
            if (eventSourceFieldId != null && fieldValue != null) {
                collectedSources.add(createEventSourceObject(
                        fieldValue, eventSourceFieldId, tree, declaredField));
            }
            
            EventSourceId eventSourceProviderPrefix = getEventSourceProviderPrefix(declaredField, parentId);
            if (eventSourceProviderPrefix != null && fieldValue != null) {
                List<Field> fields = new ArrayList<Field>(tree);
                fields.add(declaredField);
                collectedSources.addAll(findEventSources(
                        fieldValue,
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

    private EventSourceCandidate createEventSourceObject(
            Object source, EventSourceId eventSourceId, List<Field> tree, Field declaredField) {
        List<Field> fields = new ArrayList<Field>(tree);
        fields.add(declaredField);
        return EventSourceCandidate.create(eventSourceId, source);
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

    /**
     * Creates an EventListenerAdapter handling events of the given type for the given event source by
     * checking all {@link getEventListenerAdapterProviders providers}.
     * @param eventSource
     * @param eventType
     * @return An instance of EventListenerAdapter or <code>null</code> if none of the providers
     * available supports event type or event source-
     */
    public EventListenerAdapter createAdapter(Object eventSource, Class<?> eventType) {
        Iterator<EventListenerProvider> it = getEventListenerAdapterProviders();
        while(it.hasNext()) {
            EventListenerAdapter adapter = it.next().createEventListenerAdapter(eventSource, eventType);
            if(adapter!=null) {
                // TODO cache?
                return adapter;
            }
        }
        return null;
    }

    /**
     * Retuns an Iterator of all available {@link EventListenerProvider}.
     * @return
     */
    public Iterator<EventListenerProvider> getEventListenerAdapterProviders() {
        ServiceLoader<EventListenerProvider> serviceLoader = ServiceLoader.load(EventListenerProvider.class);
        return serviceLoader.iterator();
    }

    static class EventSourceCandidate {
        
        EventSourceId eventSourceId;
        Object eventSource;
        
        static EventSourceCandidate create(EventSourceId eventSourceId, Object eventSource) {
            EventSourceCandidate eventSourceObject = new EventSourceCandidate();
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
