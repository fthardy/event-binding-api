package de.javax.util.eventbinding.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * The default implementation of an event binding context.
 *
 * @author Frank Hardy
 */
public class DefaultEventBinding implements EventBinding {

	private Object source;
	private Object target;
	private Set<EventTarget> boundTargets;
	
	/**
	 * Creates a new instance of this event binding.
	 * 
	 * @param source
	 *            the object representing the event source.
	 * @param target
	 *            the object representing the event target.
	 * @param boundTargets
	 *            the set of the bound targets.
	 */
	public DefaultEventBinding(Object source, Object target, Set<EventTarget> boundTargets) {
		if (source == null) {
			throw new NullPointerException("Undefined event source object!");
		}
		this.source = source;
		if (target == null) {
			throw new NullPointerException("Undefined event target object!");
		}
		this.target = target;
		if (boundTargets == null || boundTargets.isEmpty()) {
			throw new IllegalArgumentException("No or undefined event targets!");
		}
		this.boundTargets = new HashSet<EventTarget>(boundTargets);
	}
	
	@Override
	public Object getSource() {
		return this.source;
	}
	
	@Override
	public Object getTarget() {
		return this.target;
	}

	@Override
	public void release() {
		if (this.boundTargets == null) {
			throw new IllegalStateException("The event binding has been already released!");
		}
		for (EventTarget target : this.boundTargets) {
			target.release();
		}
		this.source = null;
		this.target = null;
		this.boundTargets = null;
	}
	
	@Override
	public boolean isReleased() {
		return this.boundTargets == null;
	}
	
	/**
	 * @return the unmodifiable set of the bound targets or <code>null</code> if
	 *         this binding has been released.
	 */
	public Set<EventTarget> getBoundTargets() {
		return this.boundTargets != null ? Collections.unmodifiableSet(this.boundTargets) : null;
	}
}
