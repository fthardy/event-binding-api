package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.SimpleClassInfoCache;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultCandidateMethodFilter;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.DefaultHandlerMethodInfoCollector;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo;
import de.javax.util.eventbinding.spi.javafx.source.JavaFxEventSourceCollector;
import de.javax.util.eventbinding.spi.javafx.target.JavaFxHandlerMethodInfoExtractor;
import de.javax.util.eventbinding.spi.javafx.target.JfxMethodEventTargetFactory;

/**
 * The JavaFxEventBindingServiceProvider is using a specific {@link JavaFxEventSourceCollector instance} to collect the
 * event sources. For collecting event targets and creating the event binding the default implementations are used.
 * 
 * @author Matthias Hanisch
 */
public class JavaFxEventBindingServiceProvider implements EventBindingServiceProvider {

    private final EventTargetCollector eventTargetCollector;
    private final EventSourceCollector eventSourceCollector = new JavaFxEventSourceCollector(
            new DefaultEventSourceFactory(new DefaultEventBindingConnectorFactory()));

    public JavaFxEventBindingServiceProvider() {
    	EventSourceIdSelectorFactory idSelectorFactory = new DefaultEventSourceIdSelectorFactory();
		this.eventTargetCollector = new DefaultEventTargetCollector(
	            new JfxMethodEventTargetFactory(), idSelectorFactory,
	            new DefaultHandlerMethodInfoCollector(new DefaultCandidateMethodFilter(), new JavaFxHandlerMethodInfoExtractor(idSelectorFactory)),
	            new SimpleClassInfoCache<TargetProviderClassInfo>());
	}
    
    @Override
    public EventTargetCollector getEventTargetCollector() {
        return this.eventTargetCollector;
    }

    @Override
    public EventSourceCollector getEventSourceCollector() {
        return this.eventSourceCollector;
    }

    @Override
    public EventBinding createEventBinding(EventBinder binder, Object source, Object target, Set<EventTarget> boundEventTargets) {
    	return new DefaultEventBinding(binder, source, target, boundEventTargets);
    }
}
