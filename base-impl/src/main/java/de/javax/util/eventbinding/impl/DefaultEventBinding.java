package de.javax.util.eventbinding.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * The default implementation of an event binding which is rebuildable.
 *
 * @author Frank Hardy, Matthias Hanisch
 */
public class DefaultEventBinding extends AbstractEventBindingDelegator {

    /**
     * Creates a new instance of this event delegateBinding.
     * 
     * @param source
     *            the object representing the event source.
     * @param target
     *            the object representing the event target.
     * @param boundTargets
     *            the set of the bound targets.
     */
    public DefaultEventBinding(EventBinder binder, Object source, Object target, Set<EventTarget> boundTargets) {
        super(new RebuildableBindingDecorator(binder, new ImmutableBindingImpl(source, target, boundTargets)));
    }

    /**
     * An immutable base implementation of the event binding.
     *
     * @author Frank Hardy
     */
    public static class ImmutableBindingImpl implements EventBinding {

        private final Object source;
        private final Object target;
        private final Set<EventTarget> boundTargets;

        public ImmutableBindingImpl(Object source, Object target, Set<EventTarget> boundTargets) {
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
            this.boundTargets = Collections.unmodifiableSet(new HashSet<EventTarget>(boundTargets));
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
            for (EventTarget target : this.boundTargets) {
                target.unbindFromSources();
            }
        }

        @Override
        public boolean isReleased() {
            return this.boundTargets.isEmpty();
        }

        @Override
        public void rebuild() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A decorator which add an implementation for the #rebuild operation.
     *
     * @author Matthias Hanisch
     */
    public static class RebuildableBindingDecorator extends AbstractEventBindingDelegator {

        private EventBinder binder;

        public RebuildableBindingDecorator(EventBinder binder, EventBinding binding) {
            super(binding);
            this.binder = binder;
        }

        @Override
        public void release() {
            this.getDelegateBinding().release();
            this.setDelegateBinding(null);
            this.binder = null;
        }

        @Override
        public void rebuild() {
            if (this.isReleased()) {
                throw new IllegalStateException("The binding has been released!");
            }

            EventBinding oldBinding = this.getDelegateBinding();
            EventBinding newBinding = this.binder.bind(this.getSource(), this.getTarget());

            this.setDelegateBinding(newBinding);

            oldBinding.release();
        }
    }
}
