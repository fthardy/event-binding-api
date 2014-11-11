/**
 * 
 */
package de.javax.util.eventbinding.impl;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;

/**
 * @author Matthias Hanisch
 *
 */
public class RebuildableEventBindingDecorator implements EventBinding {

    private EventBinding binding;
    private EventBinder binder;

    public RebuildableEventBindingDecorator(EventBinder binder, EventBinding binding) {
        this.binding = binding;
        this.binder = binder;
    }

    @Override
    public Object getSource() {
        return this.binding.getSource();
    }

    @Override
    public Object getTarget() {
        return this.binding.getTarget();
    }

    @Override
    public void release() {
        this.binding.release();
    }

    @Override
    public boolean isReleased() {
        return this.binding.isReleased();
    }

    @Override
    public void rebuild() {
        Object target = this.binding.getTarget();
        Object source = this.binding.getSource();
        this.binding.release();
        this.binding = this.binder.bind(source, target);
    }
}
