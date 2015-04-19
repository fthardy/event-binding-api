package de.javax.util.eventbinding;

/**
 * An even binding is returned by an {@link de.javax.util.eventbinding.EventBinder} and represents the binding between
 * a source of events and a target for processing the events.<br/>
 *
 * @author Frank Hardy
 */
public interface EventBinding {

    /**
     * @return the object which provided the event sources for the binding.
     * <code>null</code> when the receiving binding instance has been released.
     */
    Object getSourceProvider();

    /**
     * @return the object which provided the event targets for the binding.
     * <code>null</code> when the receiving binding instance has been released.
     */
    Object getTargetProvider();

    /**
     * Releases the receiving event binding instance.<br/>
     * Once an event binding is released all references to the source provider and target provider object are cleared.
     * This makes it impossible to rebuild the binding after a release.
     */
    void release();

    /**
     * Check if the receiving binding instance has been released.
     *
     * @return <code>true</code> if the binding has been released.
     */
    boolean isReleased();

    /**
     * Rebuild the receiving event binding.<br/>
     * This is an optional method.
     *
     * @throws java.lang.UnsupportedOperationException when rebuilding is not supported by the receiving binding instance.
     */
    void rebuild();
}
