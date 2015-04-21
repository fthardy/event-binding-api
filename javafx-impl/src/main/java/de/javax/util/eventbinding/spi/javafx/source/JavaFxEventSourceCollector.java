package de.javax.util.eventbinding.spi.javafx.source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import javafx.scene.Node;
import javafx.scene.Parent;
import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;

/**
 * The JavaFxEventSourceCollector collects the event sources by identifying all {@link Node} instances and
 * {@link Parent#getChildrenUnmodifiable()} if the nodes are instances of {@link Parent}.
 * 
 * @author Matthias Hanisch
 *
 */
public class JavaFxEventSourceCollector implements EventSourceCollector {
    private final EventSourceFactory eventSourceFactory;

    /**
     * Creates a new instance of this event source collector.
     * 
     * @param eventSourceFactory
     *            the event source factory.
     */
    @Inject
    public JavaFxEventSourceCollector(EventSourceFactory eventSourceFactory) {
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
        Node node = (Node) eventSourceProvider;
        Set<EventSource> collectedSources = new HashSet<EventSource>();
        EventSourceId newId = null;
        String id = node.getId();
        if (providerId == null) {
            if (id == null) {
                newId = new EventSourceId(EventSourceId.ROOT);
            } else {
                newId = new EventSourceId(id);
            }
        } else {
            List<String> names = new ArrayList<String>(providerId.getNames());
            if (names.size() == 1 && EventSourceId.ROOT.equals(names.get(0))) {
                names.clear();
            }
            if (id != null) {
                names.add(id);
                newId = new EventSourceId(names);
            }
        }
        if (newId != null) {
            collectedSources.add(this.eventSourceFactory.createEventSource(newId, eventSourceProvider));
        }
        if (Parent.class.isAssignableFrom(eventSourceProvider.getClass())) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                collectedSources.addAll(findEventSources(child, newId));
            }
        }
        return collectedSources;
    }
}
