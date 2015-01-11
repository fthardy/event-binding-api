package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.impl.RebuildableEventBindingDecorator;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.SimpleClassInfoCache;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo;
import de.javax.util.eventbinding.spi.javafx.source.JavaFxEventSourceCollector;

/**
 * The JavaFxEventBindingServiceProvider is using a specific {@link JavaFxEventSourceCollector instance} to collect the
 * event sources. For collecting event targets and creating the event binding the default implementations are used.
 * 
 * @author Matthias Hanisch
 *
 */
public class JavaFxEventBindingServiceProvider implements EventBindingServiceProvider {

    private final EventTargetCollector eventTargetCollector = new DefaultEventTargetCollector(
            new DefaultMethodEventTargetFactory(), new DefaultEventSourceIdSelectorFactory(),
            new SimpleClassInfoCache<TargetProviderClassInfo>());

    private final EventSourceCollector eventSourceCollector = new JavaFxEventSourceCollector(
            new DefaultEventSourceFactory(new DefaultEventBindingConnectorFactory()));

    @Override
    public EventTargetCollector getEventTargetCollector() {
        return this.eventTargetCollector;
    }

    @Override
    public EventSourceCollector getEventSourceCollector() {
        return this.eventSourceCollector;
    }

    @Override
    public EventBinding createEventBinding(EventBinder binder, Object source, Object target,
            Set<EventTarget> boundEventTargets) {
        return new RebuildableEventBindingDecorator(binder, new DefaultEventBinding(source, target, boundEventTargets));
    }

}
