package de.javax.util.eventbinding;

/**
 * An event binder is a utility used to create an event binding between a source
 * of events to a target which can handle the events.<br/>
 * 
 * TODO Add further detailed documentation!
 * 
 * @author Frank Hardy
 */
public interface EventBinder {

	/**
	 * Create an event binding between a given event source and target.
	 * 
	 * @param source
	 *            the object representing a source of events.
	 * @param target
	 *            the object representing the target of the events from the
	 *            source.
	 * 
	 * @return an object which represents the event binding between the given
	 *         source and target object.
	 * 
	 * @throws EventBindingException
	 *             when the binding between the source and target fails for some
	 *             reason.
	 */
	EventBinding bind(Object source, Object target) throws EventBindingException;

	/**
	 * @return <code>true</code> when this event binder is in strict binding mode. Otherwise <code>false</code>. 
	 */
	boolean isStrictBindingMode();

	/**
	 * @param strictBinding
	 *            set to <code>true</code> to activate the strict binding mode.
	 */
	void setStrictBindingMode(boolean strictBinding);

}