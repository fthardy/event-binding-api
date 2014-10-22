package de.javax.util.eventbinding;

import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventBindingServiceProvider;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * The default implementation of an event binder.<br/>
 * This implementation defines the top level event binding process. The concrete
 * implementation is based on the interfaces of the SPI. The
 * {@link EventBindingServiceProvider} provides the implementations of the
 * services for the event binding.
 * 
 * @author Frank Hardy
 */
public class DefaultEventBinder implements EventBinder {

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

	/**
	 * Create an event binding between a given event source provider and event target provider.
	 * 
	 * @param sourceProvider
	 *            the provider of event sources for binding.
	 * @param targetProvider
	 *            the provider of event targets for binding.
	 * 
	 * @return an object which represents the event binding between the given
	 *         source and target object.
	 * 
	 * @throws EventBindingException
	 *             when the binding between the source and target fails for some
	 *             reason.
	 */
	@Override
	public EventBinding bind(Object sourceProvider, Object targetProvider) throws EventBindingException {
		Set<EventTarget> foundTargets = 
		        this.serviceProvider.getEventTargetCollector().collectEventTargetsFrom(targetProvider);
		if (foundTargets == null || foundTargets.isEmpty()) {
			throw new NoEventTargetsFoundException();
		}
		
		return this.serviceProvider.createEventBinding(
				sourceProvider, targetProvider, this.bindTargetsToSources(foundTargets, sourceProvider));
	}
	
	/**
	 * @return <code>true</code> when this event binder is in strict binding mode. Otherwise <code>false</code>. 
	 */
	@Override
	public boolean isStrictBindingMode() {
		return this.strictBindingMode;
	}

	/**
	 * @param strictBinding
	 *            set to <code>true</code> to activate the strict binding mode.
	 */
	@Override
	public void setStrictBindingMode(boolean strictBinding) {
		this.strictBindingMode = strictBinding;
	}

	/**
	 * Implements the process of binding the found event targets to the event
	 * sources from the given event source provider object.
	 * 
	 * @param eventTargets
	 *            the found event targets.
	 * @param sourceProvider
	 *            the event source provider object.
	 * 
	 * @return the set of bound event targets. Never <code>null</code>.
	 */
	protected Set<EventTarget> bindTargetsToSources(Set<EventTarget> eventTargets, Object sourceProvider) {
		Set<EventTarget> boundTargets = new HashSet<EventTarget>();
		
		Set<EventTarget> unboundTargets = new HashSet<EventTarget>();
		for (EventTarget eventTarget : eventTargets) {
			if (this.serviceProvider.getEventSourceCollector().bindTargetToSources(eventTarget, sourceProvider)) {
				boundTargets.add(eventTarget);
			} else if (this.strictBindingMode) {
				unboundTargets.add(eventTarget);
			}
		}
		
		if (boundTargets.isEmpty()) {
			throw new UnboundTargetsException(new HashSet<EventTarget>(eventTargets));
		}
		
		if (this.strictBindingMode && !unboundTargets.isEmpty()) {
			throw new UnboundTargetsException(unboundTargets);
		}
		
		return boundTargets;
	}
}
