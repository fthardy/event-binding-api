package de.javax.util.eventbinding.impl;

import java.util.Set;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * A abstract base class for delegating implementations like decorators and proxies. Delegates every operation to the
 * delegate. When no delegate is set all operations will throw an IllegalStateException accept #isReleased which will
 * return true.
 *
 * @author Frank Hardy
 */
public abstract class AbstractEventBindingDelegator implements EventBinding {

    private EventBinding delegateBinding;

    /**
     * Initializes the delegator with a delegate binding instance.
     *
     * @param delegateBinding
     *            the delegate binding instance.
     */
    public AbstractEventBindingDelegator(EventBinding delegateBinding) {
        if (delegateBinding == null) {
            throw new NullPointerException("Undefined delegate binding!");
        }
        this.setDelegateBinding(delegateBinding);
    }

    @Override
    public Object getSourceProvider() {
        if (this.delegateBinding == null) {
            throw new IllegalStateException("The binding has been released!");
        }
        return this.delegateBinding.getSourceProvider();
    }

    @Override
    public Object getTargetProvider() {
        if (this.delegateBinding == null) {
            throw new IllegalStateException("The binding has been released!");
        }
        return this.delegateBinding.getTargetProvider();
    }

    @Override
    public void release() {
        if (this.delegateBinding == null) {
            throw new IllegalStateException("The binding has been released!");
        }
        this.delegateBinding.release();
    }

    @Override
    public boolean isReleased() {
        return this.delegateBinding == null;
    }

    @Override
    public void rebuild() {
        if (this.delegateBinding == null) {
            throw new IllegalStateException("The binding has been released!");
        }
        this.delegateBinding.rebuild();
    }

    protected EventBinding getDelegateBinding() {
        return this.delegateBinding;
    }

    protected void setDelegateBinding(EventBinding newBinding) {
        this.delegateBinding = newBinding;
    }

    public abstract Set<EventTarget> getBoundTargets();
}
