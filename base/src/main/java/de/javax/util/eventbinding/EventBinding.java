package de.javax.util.eventbinding;

/**
 * An event binding is a context object which represents an event binding between a source and a target object.<br/>
 * An event binding provides access to the object which represents source and target of the event binding and allows to
 * release the binding between those two by calling {@link #release()}.
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
     * Releases the receiving event binding instance.
     */
    void release();

    /**
     * Check if the receiving binding instance has been released.
     *
     * @return <code>true</code> if the binding has been released.
     */
    boolean isReleased();

    /**
     * Rebuilds the event binding by releasing it first and re-creating the event binding for the source and target of
     * this event binding.
     */
    void rebuild();

}
