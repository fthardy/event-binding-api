package de.javax.util.eventbinding;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * The default implementation of an event binder.<br/>
 * This implementation defines the top level event binding process. The concrete implementation is based on the
 * interfaces of the SPI.
 * 
 * @author Frank Hardy
 */
public class DefaultEventBinder implements EventBinder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EventBindingServiceProvider serviceProvider;

    private boolean strictBindingMode;

    /**
     * Creates a new instance of an event binder.
     * 
     * @param serviceProvider
     *            the implementation instance of a {@link EventBindingServiceProvider}.
     */
    public DefaultEventBinder(EventBindingServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            throw new NullPointerException("Undefined event binder service implementation!");
        }
        this.serviceProvider = serviceProvider;
    }

    @Override
    public EventBinding bind(Object sourceProvider, Object targetProvider) throws EventBindingException {
        logger.debug("Binding sources of source provider [{}] with targets of target provider [{}]", sourceProvider, targetProvider);
        
        Set<EventSource> foundSources = this.serviceProvider.getEventSourceCollector().collectEventSourcesFrom(
                sourceProvider);
        if (foundSources.isEmpty()) {
            throw new EventBindingException("No event sources found!");
        }

        Set<EventTarget> foundTargets = this.serviceProvider.getEventTargetCollector().collectEventTargetsFrom(
                targetProvider);
        if (foundTargets.isEmpty()) {
            throw new EventBindingException("No event targets found!");
        }

        return this.serviceProvider.createEventBinding(this, sourceProvider, targetProvider,
                this.bindTargetsToSources(foundTargets, foundSources));
    }

    /**
     * @return <code>true</code> when this event binder is in strict binding mode. Otherwise <code>false</code>.
     */
    public boolean isStrictBindingMode() {
        return this.strictBindingMode;
    }

    /**
     * @param strictBinding
     *            set to <code>true</code> to activate the strict binding mode.
     */
    public void setStrictBindingMode(boolean strictBinding) {
        this.strictBindingMode = strictBinding;
    }

    private Set<EventTarget> bindTargetsToSources(Set<EventTarget> eventTargets, Set<EventSource> eventSources) {
    	logger.debug("Trying to bind {} sources with {} targets.", eventSources.size(), eventTargets.size());
    	
        Set<EventTarget> boundTargets = new HashSet<EventTarget>();
        Set<EventTarget> unboundTargets = new HashSet<EventTarget>();

        for (EventTarget eventTarget : eventTargets) {
            for (EventSource eventSource : eventSources) {
                eventSource.bindTo(eventTarget);
            }

            if (eventTarget.isBound()) {
                boundTargets.add(eventTarget);
            } else if (this.strictBindingMode) {
                unboundTargets.add(eventTarget);
            } else {
                logger.warn("Target [{}] was not bound!", eventTarget);
            }
        }

        if (boundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(eventTargets);
        }
        if (this.strictBindingMode && !unboundTargets.isEmpty()) {
            throw new UnboundEventTargetsException(unboundTargets);
        }
        return boundTargets;
    }
}
