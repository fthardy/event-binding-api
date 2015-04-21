package de.javax.util.eventbinding.spi.impl;

import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventBindingFactory;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * TODO Documentation
 *
 * @author Frank Hardy
 */
public class DefaultEventBindingFactory implements EventBindingFactory {

	@Override
	public EventBinding createEventBinding(
			EventBinder eventBinder, Object sourceProvider, Object targetProvider, Set<EventTarget> bindTargetsToSources) {
		return new DefaultEventBinding(eventBinder, sourceProvider, targetProvider, bindTargetsToSources);
	}
}
