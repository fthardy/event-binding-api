package de.javax.util.eventbinding.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTargetCollector;

/**
 * The default implementation of an event binder.
 * 
 * @author Frank Hardy
 */
@Singleton
public class DefaultEventBinder implements EventBinder {

	private final EventSourceCollector eventSourceCollector;
	private final EventTargetCollector eventTargetCollector;
	private boolean inStrictBindingMode;
	
	@Inject
	public DefaultEventBinder(EventSourceCollector eventSourceCollector, EventTargetCollector eventTargetCollector) {
		this.eventSourceCollector = eventSourceCollector;
		this.eventTargetCollector = eventTargetCollector;
	}
	
    @Override
    public EventBinding bind(Object eventSourceProvider, Object eventTargetProvider) {
        return new DefaultEventBinding(
        		eventSourceCollector, eventTargetCollector,
        		new EventSourceAndTargetProvider(eventSourceProvider, eventTargetProvider),
        		inStrictBindingMode);
    }
    
    @Override
    public boolean isInStrictBindingMode() {
    	return inStrictBindingMode;
    }
    
    @Override
    public void setInStrictBindingMode(boolean strictBindingOn) {
    	inStrictBindingMode = strictBindingOn;
    }
}
