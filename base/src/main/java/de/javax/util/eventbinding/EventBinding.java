package de.javax.util.eventbinding;

/**
 * An even binding is returned by an {@link de.javax.util.eventbinding.EventBinder} and represents the binding between
 * a source of events and a target for processing the events.<br/>
 *
 * @author Frank Hardy
 */
public interface EventBinding {

    /**
     * @return the object which is the source of events in the receiving event binding. <code>null</code> when the
     *         binding has been released.
     */
    Object getSource();

    /**
     * @return the object which is the target of events in the receiving event binding. <code>null</code> when the
     *         binding has been released.
     */
    Object getTarget();

    /**
     * Releases the receiving event binding instance.<br/>
     * Once an event binding is released all references to the source and target object are cleared. Hence after a
     * release it can can never be rebuilt or used in any way.
     */
    void release();

    /**
     * Check if the receiving binding instance has been released.
     *
     * @return <code>true</code> if the binding has been released.
     */
    boolean isReleased();

    /**
     * Rebuilds the receiving event binding between the source and target object.<br/>
     * This is an optional method.
     *
     * @throws java.lang.UnsupportedOperationException when the rebuilding is not supported by the receiving binding instance.
     */
    void rebuild();
}
