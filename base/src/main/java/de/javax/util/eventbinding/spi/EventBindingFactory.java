package de.javax.util.eventbinding.spi;

import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;

public interface EventBindingFactory {

	/**
	 * Creates a new instance of an event binding.
	 * 
	 * @param eventBinder the event binder.
	 * @param sourceProvider the source provider object which was given to the binder.
	 * @param targetProvider the target provider object which was given to the binder.
	 * @param boundTargets the set of bound event targets.
	 * 
	 * @return the new instance of the binding.
	 */
	EventBinding createEventBinding(
			EventBinder eventBinder, Object sourceProvider, Object targetProvider, Set<EventTarget> boundTargets);
}
